<script setup lang="ts">
import type {
  Appointment,
  BackendAppointment,
  Doctor,
  Patient,
  User,
  AppointmentNote,
  Measurement,
  Diagnosis,
  Treatment,
  NoteFile,
} from "@/types/types";
import { ref, onMounted, computed } from "vue";
import {
  createAppointmentSlot,
  getAllAppointmentsForUser,
  sendBookingAppointment,
  deleteAppointment,
  cancelAppointment,
  getAppointment,
} from "@/api/appointmentController";
import { createNote, uploadFile } from "@/api/noteController";
import { watch } from "vue";
import { getPatient } from "@/api/patientController";
import { getDoctor } from "@/api/doctorController";
import { useRouter, useRoute } from "vue-router";
import {
  useAppointmentHelpers,
  parseDate,
  roundToQuarterHour,
  checkCanCancelAppointment,
  checkCanSeeAppointment,
  calculateEndTime,
  formatDate,
  mapBackendToFrontend,
  appointmentHasPatient,
} from "@/helpers/appointmentHelpers";

import { lookupNoteType } from "@/helpers/noteHelpers";
import { useUserStore } from "@/stores/userStore";
import LoadingSpinner from "@/components/LoadingSpinner.vue";

const measurement = ref<Measurement>({
  type: undefined,
  value: 0,
});
const diagnosis = ref<Diagnosis>({
  icdCode: "",
  recommendation: "",
});
const treatment = ref<Treatment>({
  action: "",
  diagnosis: diagnosis.value,
});
const router = useRouter();
const route = useRoute();
const doctor = ref<Doctor | null>(null);
const patient = ref<Patient | null>(null);
const finishedLoading = ref(false);
const selectedEvent = ref();
const {
  delAppointment,
  bookAppointment,
  cancelSelectedAppointment,
  createAppointment,
} = useAppointmentHelpers();
const event = ref<Appointment>();

const newNote = ref<AppointmentNote>({
  id: undefined,
  timestamp: "",
  appointmentId: undefined,
  doctorId: undefined,
  patientId: undefined,
  creator: "Doctor",
  file: null,
  body: undefined,
  noteType: "DIAGNOSIS",
  payload: undefined,
  type: undefined,
});

const fileDescriptions = ref<string[]>([]);
const showNoteDetails = ref(false);
const detailNote = ref<AppointmentNote | null>(null);

const snackbar = ref({
  show: false,
  message: "",
  color: "success",
});
const showCreateNoteDialog = ref(false);
const showSnackbar = (message: string, color: string = "success") => {
  snackbar.value = {
    show: true,
    message,
    color,
  };
};

const fetchAppointment = async () => {
  const routeId = route.params.id;
  if (!routeId) {
    showSnackbar("Termin nicht gefunden", "error");
    router.push("/");
  }
  finishedLoading.value = false;
  const appointment = (await getAppointment(
    Number(routeId)
  )) as any as BackendAppointment;

  if (appointment) {
    event.value = mapBackendToFrontend(appointment);
    console.log("appointment", appointment);
    doctor.value = await getDoctor(appointment.doctor?.id);
    if (appointment.patient?.id) {
      patient.value = await getPatient(appointment.patient?.id);
    }
  }
  finishedLoading.value = true;
};

const canCancelAppointment = computed(() => {
  return checkCanCancelAppointment(event.value);
});

const noteTypes = ["DIAGNOSIS", "MEASUREMENT", "TREATMENT", "FILE"];

const doctorCardTitle = (doctor: Doctor) => {
  let titleStr = "";
  if (doctor.title) {
    titleStr = doctor.title;
  }

  return `${titleStr} ${doctor.firstName} ${doctor.lastName}`;
};
const patientCardTitle = (patient: Patient) => {
  return `${patient.firstName} ${patient.lastName}`;
};
const doctorCardSubtitle = (doctor: Doctor) => {
  return `${doctor.speciality}, ${doctor.city}`;
};

const deleteSelectedAppointment = async () => {
  if (!event.value) {
    console.error("Kein Termin ausgewählt");
    return;
  }
  await delAppointment(
    event.value.id,
    useUserStore().getLoggedInUser?.id,
    showSnackbar
  );
};

const onCancelSelectedAppointment = async () => {
  if (event.value) {
    await cancelSelectedAppointment(
      event.value,
      useUserStore().getLoggedInUser?.id,
      showSnackbar
    );

    fetchAppointment();
  }
};

const onBookAppointment = async () => {
  const loggedInUser = useUserStore().getLoggedInUser;
  if (event.value?.id) {
    await bookAppointment(event.value.id, loggedInUser?.id, showSnackbar);
  }
  fetchAppointment();
};

const onAddNote = () => {
  showCreateNoteDialog.value = true;
  newNote.value.doctorId = event.value?.doctor?.id;
  newNote.value.patientId = event.value?.patient?.id;
  newNote.value.appointmentId = event.value?.id;
  newNote.value.timestamp = Date.now().toString();
};

const onSaveNote = async () => {
  try {
    switch (newNote.value.noteType) {
      case "MEASUREMENT":
        newNote.value.payload = {
          type: measurement.value.type,
          value: measurement.value.value,
        };
        break;

      case "DIAGNOSIS":
        newNote.value.payload = {
          icdCode: diagnosis.value.icdCode,
          recommendation: diagnosis.value.recommendation,
        };
        break;

      case "TREATMENT":
        newNote.value.payload = {
          action: treatment.value.action,
          diagnosis: {
            icdCode: diagnosis.value.icdCode,
            recommendation: diagnosis.value.recommendation,
          },
        };
        break;

      case "FILE":
        console.log("file");
        break;
      default:
        console.error("Unsupported note type");
        showSnackbar("Ungültiger Notiztyp", "error");
        return;
    }

    const response = await createNote(newNote.value);
    if (response) {
      showSnackbar("Notiz erfolgreich gespeichert");
      showCreateNoteDialog.value = false;
    } else {
      showSnackbar("Fehler beim Speichern der Notiz", "error");
    }
  } catch (error) {
    console.error("Error saving note:", error);
    showSnackbar("Fehler beim Speichern der Notiz", "error");
  }

  fetchAppointment();
};

const closeSaveNoteScreen = () => {
  showCreateNoteDialog.value = false;
  newNote.value = {
    id: undefined,
    timestamp: "",
    appointmentId: undefined,
    doctorId: undefined,
    patientId: undefined,
    creator: "Doctor",
    file: null,
    body: undefined,
    noteType: "DIAGNOSIS",
    payload: undefined,
    type: undefined,
  };
};

const onFileChange = async (selectedFile: File) => {
  console.log("hallo", selectedFile);
  const response = await uploadFile(
    selectedFile,
    newNote.value.appointmentId,
    newNote.value.doctorId,
    newNote.value.patientId
  );
  console.log("response", response);
};

const openNoteDetailModal = (note: AppointmentNote) => {
  detailNote.value = note;
  showNoteDetails.value = true;
};

const closeNoteDetailModal = () => {
  showNoteDetails.value = false;
};

onMounted(async () => {
  useUserStore().fakeLogIn("doctor", 1);
  fetchAppointment();
});
</script>

<template>
  <div>
    <h1>Termin {{ event?.title }}</h1>
    <h3>
      am {{ formatDate(event?.start, "date") }}
      {{ formatDate(event?.start, "time") }} bis
      {{ formatDate(event?.end, "time") }}
    </h3>
    <v-btn
      v-if="event?.doctor?.id === useUserStore().getLoggedInUser?.id"
      color="red"
      @click="deleteSelectedAppointment"
    >
      Termin löschen
    </v-btn>
  </div>
  <v-snackbar v-model="snackbar.show" :color="snackbar.color" timeout="3000">
    {{ snackbar.message }}
  </v-snackbar>
  <v-row justify="start" dense>
    <v-col cols="12" md="4" class="doctor-card">
      <v-card
        class="mx-auto"
        variant="text"
        :subtitle="doctorCardSubtitle(doctor)"
        :title="doctorCardTitle(doctor)"
        v-if="doctor"
      >
        <template v-slot:append>
          <v-avatar size="60">
            <v-img
              alt="John"
              src="https://cdn.vuetifyjs.com/images/john.png"
            ></v-img>
          </v-avatar>
        </template>
        <v-card-text>
          <p>{{ doctor.street }} {{ doctor.houseNumber }}</p>
          <p>{{ doctor.zipCode }} {{ doctor.city }}</p>
          <p>{{ doctor.phoneNumber }}</p>
          <p>{{ doctor.email }}</p>
        </v-card-text>
      </v-card>
    </v-col>
    <v-divider vertical></v-divider>
    <v-col cols="12" md="4" class="doctor-card">
      <v-card
        variant="text"
        class="mx-auto"
        title="Patient"
        :subtitle="patientCardTitle(patient)"
        v-if="patient"
      >
        <template v-slot:append>
          <v-avatar size="60">
            <v-img
              alt="John"
              src="https://cdn.vuetifyjs.com/images/john.png"
            ></v-img>
          </v-avatar>
        </template>
        <v-card-text>
          <p>{{ patient.street }} {{ patient.houseNumber }}</p>
          <p>{{ patient.zipCode }} {{ patient.city }}</p>
          <p>{{ patient.phoneNumber }}</p>
          <p>{{ patient.email }}</p>
        </v-card-text>
        <v-btn v-if="!appointmentHasPatient(event!)" @click="onBookAppointment"
          >Termin buchen</v-btn
        >
        <v-btn
          v-if="canCancelAppointment"
          color="red-lighten-2"
          @click="onCancelSelectedAppointment"
        >
          Termin stornieren</v-btn
        >
      </v-card>
    </v-col>
  </v-row>
  <v-row justify="start" dense>
    <h2>Terminanhänge</h2>
    <div class="d-flex ml-4 cursor-pointer" @click="onAddNote">
      <img
        src="@/assets/icons/file-plus.svg"
        class="clear-icon ml-4 align-self-center"
      /><span class="mt-2 ml-1">hinzufügen</span>
    </div>
  </v-row>
  <v-row dense>
    <v-table>
      <thead>
        <tr>
          <th class="text-left">Datum</th>
          <th class="text-left">Notiztyp</th>
          <th class="text-left">Titel</th>
          <th class="text-left"></th>
        </tr>
      </thead>
      <tbody>
        <tr v-for="item in event?.notes" :key="item.id" class="note-row">
          <td>{{ formatDate(item.timestamp!, "dateTime") }}</td>
          <td>{{ lookupNoteType(item.noteType) }}</td>
          <td>{{ item.type }}</td>
          <button class="note-button" @click="openNoteDetailModal(item)">
            Details
          </button>
        </tr>
      </tbody>
    </v-table>
  </v-row>
  <v-dialog v-model="showCreateNoteDialog" style="width: 500px">
    <v-card>
      <v-card-title>Terminanhang erstellen</v-card-title>
      <v-card-text>
        <v-select
          label="Notiztyp"
          :items="noteTypes"
          v-model="newNote.noteType"
        ></v-select>
        <div v-if="newNote.noteType == 'MEASUREMENT'">
          <v-select
            v-model="measurement.type"
            label="Messtyp"
            :items="[
              'BLOOD_SUGAR',
              'BLOOD_PRESSURE',
              'HEART_RATE',
              'BODY_FAT',
              'OXYGEN_BLOOD_SATURATION',
              'BODY_TEMPERATURE',
              'BODY_WEIGHT',
              'BODY_HEIGHT',
            ]"
          ></v-select>
          <v-text-field
            label="Wert"
            v-model="measurement.value"
            hide-details
            single-line
            type="number"
          />
        </div>
        <div
          v-if="
            newNote.noteType == 'DIAGNOSIS' || newNote.noteType == 'TREATMENT'
          "
        >
          <v-text-field
            label="Icd Code"
            v-model="diagnosis.icdCode"
            hide-details
            single-line
            type="text"
          />
          <v-textarea
            label="Empfehlung"
            v-model="diagnosis.recommendation"
            hide-details
            single-line
            type="text"
          />
        </div>
        <div v-if="newNote.noteType == 'TREATMENT'">
          <v-text-field
            label="Action"
            v-model="treatment.action"
            hide-details
            single-line
            type="text"
          />
        </div>
        <div v-if="newNote.noteType == 'FILE'">
          <v-file-input
            label="Datei hochladen"
            accept="image/*,application/pdf"
            @change="onFileChange"
          ></v-file-input>
          <v-text-field placeholder="Beschreibung hinzufügen"></v-text-field>
        </div>
      </v-card-text>
      <v-card-actions>
        <v-btn color="primary" @click="onSaveNote">Speichern</v-btn>
        <v-btn color="secondary" @click="closeSaveNoteScreen">Abbrechen</v-btn>
      </v-card-actions>
    </v-card>
  </v-dialog>
  <v-dialog v-model="showNoteDetails" style="width: 500px">
    <v-card>
      <v-card-title>Notizdetails</v-card-title>
      <v-card-subtitle
        >{{ lookupNoteType(detailNote?.noteType) }} erstellt am
        {{ formatDate(detailNote?.timestamp!, "date") }} um
        {{ formatDate(detailNote?.timestamp!, "time") }}</v-card-subtitle
      >
      <v-card-text>
        <div v-if="detailNote?.noteType === 'MEASUREMENT'">
          <p>{{ detailNote.type }}: {{ detailNote.value }}</p>
        </div>
        <div v-if="detailNote?.noteType === 'DIAGNOSIS'">
          <p>Icd Code:{{ detailNote.icdCode }}</p>
          <p>Empfehlung: {{ detailNote.recommendation }}</p>
        </div>
        <div v-if="detailNote?.noteType === 'TREATMENT'">
          <p>Icd Code: {{ detailNote.icdCode }}</p>
          <p>Maßnahme: {{ detailNote.action }}</p>
          <p>Empfehlung: {{ detailNote.recommendation }}</p>
        </div>
      </v-card-text>
      <v-card-actions>
        <v-btn color="primary" @click="closeNoteDetailModal">Schließen</v-btn>
      </v-card-actions>
    </v-card>
  </v-dialog>
  <LoadingSpinner v-if="!finishedLoading" />
</template>

<style scoped>
.note-button {
  margin-top: 1.1em;
  margin-left: 1em;
  color: var(--primary-color);
}
</style>
