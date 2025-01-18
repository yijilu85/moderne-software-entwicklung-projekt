<script setup lang="ts">
import type {
  Appointment,
  BackendAppointment,
  Doctor,
  Patient,
  User,
  AppointmentNote,
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
import { useUserStore } from "@/stores/userStore";

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
  files: undefined,
  body: undefined,
});
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
          <th class="text-left">Typ</th>
          <th class="text-left">Datum</th>
          <th class="text-left"></th>
        </tr>
      </thead>
      <tbody>
        <tr v-for="item in event?.notes" :key="item.id">
          <td>{{ item.timestamp }}</td>
          <td>{{ item.type }}</td>
          <a href="#">Mehr Details</a>
        </tr>
      </tbody>
    </v-table>
  </v-row>
  <v-dialog v-model="showCreateNoteDialog" style="width: 500px">
    <v-card>
      <v-card-title>Neuen Termin erstellen</v-card-title>
      <v-card-text>
        <!-- <v-text-field v-model="newEvent.title" label="Titel"></v-text-field> -->
        <v-select
          label="Termintyp"
          :items="['DIAGNOSIS', 'MEASUREMENT', 'TREATMENT', 'FILE']"
          v-model="newNote.type"
        ></v-select>
      </v-card-text>
      <v-card-actions>
        <v-btn color="primary" @click="">Speichern</v-btn>
        <v-btn color="secondary" @click="">Abbrechen</v-btn>
      </v-card-actions>
    </v-card>
  </v-dialog>
</template>

<style scoped></style>
