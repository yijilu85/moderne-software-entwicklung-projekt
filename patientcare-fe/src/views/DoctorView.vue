<script setup lang="ts">
import type { Appointment, Doctor } from "@/types/types";
import { ref, onMounted, computed } from "vue";
import {
  createAppointmentSlot,
  getAllAppointments,
  sendBookingAppointment,
} from "@/api/appointmentController";
import { watch } from "vue";


const date = new Date();

const dummyDoctor = ref<Doctor>({
  id: 2,
  firstName: "Hans",
  lastName: "Ulrich",
  street: "Berliner Straße",
  houseNumber: "12",
  zipCode: "12345",
  city: "Berlin",
  dateOfBirth: date,
  title: "Dr. med.",
  speciality: "Zahnarzt",
  medicalId: "123asd",
  profileImg: "dummy-doctor.png",
  userType: "DOCTOR",
});

// const events = ref<Appointment[]>([
//   {
//     start: "2024-11-25 10:30",
//     end: "2024-11-25 11:30",
//     title: "Doctor appointment",
//   },
// ]);
const events = ref<Appointment[]>([]);

const selectedDay = ref("");

const newEvent = ref<Appointment>({
  id: 0,
  start: "",
  end: "",
  patient: null,
  doctor: null,
  title: "",
});

const finishedLoading = ref(false);
const AppointmentDuration = 30;
const selectedEvent = ref();
const showDialog = ref(false);
const showCreateDialog = ref(false);
const durations = ref<number[]>([]); // Liste für Zeitoptionen
const selectedDuration = ref<number>(15); // Standarddauer 30 Minuten
const startTimes = ref<string[]>([]); // Liste aller möglichen Startzeiten
const selectedStartTime = ref<string>("09:00"); // Standardstartzeit

const snackbar = ref({
  show: false,
  message: "",
  color: "success",
});

// (event: MouseEvent, appointment: Appointment)
const onEventClick = (appointment: Appointment, mouseevent: MouseEvent) => {
  selectedEvent.value = appointment;
  showDialog.value = true;
};

const createAppointment = async () => {
  // Kombinieren von Datum und Zeit zu ISO-Strings
  const startDateTime = new Date(
    `${newEvent.value.date}T${selectedStartTime.value}`
  );
  const endDateTime = new Date(startDateTime.getTime() + selectedDuration.value * 60 * 1000);
  const payload = {
    doctor: {
      id: 2,
    },
    creator: {
      id: 2,
    },
    startDateTime: startDateTime,
    endDateTime: endDateTime,
    createdAt: Date.now(),
    type: newEvent.value.type,
    title: newEvent.value.title,
    notes: [],
  };

  try {
    const appointment = await createAppointmentSlot(payload);
    setSnackBar("Termin erfolgreich erstellt!", "success");
  } catch {
    setSnackBar("Fehler beim Erstellen eines Termins!", "error");
  }

  //Resets
  showCreateDialog.value = false;
  newEvent.value = { id: 0, title: "", start: "", end: "", date: "" };
  selectedDuration.value = 15; // Standarddauer zurücksetzen
  selectedStartTime.value = "09:00"; // Standardstartzeit
  fetchAppointments();
};

const setSnackBar = (message: string, type: string) => {
  if (type === "success") {
    snackbar.value = {
      show: true,
      message: message,
      color: "success",
    };
  } else if (type === "error") {
    snackbar.value = {
      show: true,
      message: message,
      color: "error",
    };
  }
};
// Beobachte Änderungen an der ausgewählten Startzeit
watch(selectedStartTime, (newStartTime) => {
  if (newStartTime) {
    const [hours, minutes] = newStartTime.split(":").map(Number);
    const startDate = new Date();
    startDate.setHours(hours, minutes, 0, 0);

    // Berechne die Endzeit basierend auf der Dauer
    const endDate = new Date(
        startDate.getTime() + selectedDuration.value * 60 * 1000
    );

    // Aktualisiere die Anzeige
    newEvent.value.start = newStartTime; // Startzeit
    newEvent.value.end = endDate.toTimeString().slice(0, 5); // Endzeit (HH:mm)
  }
});

const roundToQuarterHour = (date: Date): Date => {
  const minutes = date.getMinutes();
  const roundedMinutes = Math.ceil(minutes / 15) * 15; // Aufrunden auf nächsten 15-Minuten-Schritt
  date.setMinutes(roundedMinutes, 0, 0); // Minuten, Sekunden und Millisekunden setzen
  return date;
};


const onCellClick = (date: Date) => {

  // Zeit auf den nächsten 15-Minuten-Schritt runden
  const startTime = roundToQuarterHour(new Date(date));

  // Lokale Zeit direkt nutzen (keine doppelte Konvertierung)
  const localStartTime = new Date(startTime.getTime());

  // Berechnung der Endzeit basierend auf der Standarddauer oder ausgewählten Dauer
  const endTime = new Date(
      localStartTime.getTime() + selectedDuration.value * 60 * 1000
  );

  // Lokale Zeit korrekt formatieren
  const formatTime = (date: Date) => date.toTimeString().slice(0, 5); // HH:mm

  // toISOString formatiert wieder in UTC zurück deswegen lieber toTimeString
  newEvent.value.date = localStartTime.toISOString().split("T")[0]; // Datum: YYYY-MM-DD
  newEvent.value.start = localStartTime.toTimeString().slice(0, 5); // Startzeit: HH:mm
  newEvent.value.end = endTime.toTimeString().slice(0, 5); // Endzeit: HH:mm

  // Synchronisiere mit dem Dropdown für die Startzeit
  selectedStartTime.value = newEvent.value.start;

  showCreateDialog.value = true; // Dialog öffnen

  console.log("Original Date:", date.toISOString());
  console.log("Rounded Date:", startTime.toISOString());
  console.log("Localized Date:", localStartTime.toLocaleString());
};


const bookAppointment = async () => {
  const payload = {
    appointmentId: selectedEvent.value.id,
    patientId: 1,
  };

  try {
    await sendBookingAppointment(payload);

    setSnackBar("Termin erfolgreich gebucht!", "success");
  } catch (error) {
    console.error("Error booking appointment:", error);
    setSnackBar("Fehler beim Buchen des Termins!", "error");
  }

  fetchAppointments();
  showDialog.value = false;
};

const fetchAppointments = async () => {
  events.value = [];
  finishedLoading.value = false;
  const data = await getAllAppointments();

  for (const event of data) {
    if (event === undefined) {
      return;
    }

    const appointment = {
      id: event.id,
      start: parseDate(event.startDateTime),
      end: parseDate(event.endDateTime),
      patient: event.patient,
      title: event.title,
      doctor: event.doctor,
      notes: event.notes,
      type: event.type,
    } as Appointment;

    events.value.push(appointment);
  }
  finishedLoading.value = true;
};

const parseDate = (date: string) => {
  if (date === undefined) return;
  let parsedDate = date.replace("T", " ").slice(0, -3);

  return parsedDate;
};

const appointmentHasPatient = computed(() => {
  if (selectedEvent.value?.patient?.id) {
    return true;
  } else {
    return false;
  }
});

const calculateEndTime = (start: string, duration: number) => {
  if (!start || !duration) return "";
  const [hours, minutes] = start.split(":").map(Number);
  const startDate = new Date();
  startDate.setHours(hours, minutes, 0, 0);
  const endDate = new Date(startDate.getTime() + duration * 60 * 1000); // Dauer hinzufügen
  return endDate.toTimeString().slice(0, 5); // HH:mm
};

onMounted(() => {
  // Generiere Zeitoptionen von 15 bis 60 Minuten
  for (let i = 15; i <= 60; i += 15) {
    durations.value.push(i);
  }
  // Generiere Zeitoptionen von 00:00 bis 23:45 in 15-Minuten-Schritten
  for (let hour = 7; hour < 18; hour++) {
    for (let minutes = 0; minutes < 60; minutes += 15) {
      const time = `${hour.toString().padStart(2, "0")}:${minutes
          .toString()
          .padStart(2, "0")}`;
      startTimes.value.push(time);
    }
  }
  fetchAppointments();
});
</script>

<template>
  <div class="doctor-detail">
    <h1>
      {{ dummyDoctor.title }} {{ dummyDoctor.firstName }}
      {{ dummyDoctor.lastName }}
    </h1>
    <img class="doctor-img" src="@/assets/images/dummy-doctor.png" />
    <p>{{ dummyDoctor.speciality }}</p>
    <p>{{ dummyDoctor.medicalId }}</p>
    <p>{{ dummyDoctor.street }} {{ dummyDoctor.houseNumber }}</p>
    <p>{{ dummyDoctor.zipCode }} {{ dummyDoctor.city }}</p>
  </div>

  <h2>Termin buchen</h2>
  <vue-cal
    v-if="finishedLoading"
    style="height: 850px"
    :time-cell-height="80"
    :events="events"
    :on-event-click="onEventClick"
    @cell-click="onCellClick"
  />
  <v-dialog v-model="showDialog" style="width: 500px">
    <v-card>
      <v-card-title>
        <span>{{ selectedEvent.title }}</span>
        <v-spacer />
        <strong>{{
          selectedEvent.start && selectedEvent.start.format("DD/MM/YYYY")
        }}</strong>
      </v-card-title>
      <v-card-text>
        <p v-html="selectedEvent.contentFull" />
        <strong>Termindetails</strong>
        <ul>
          <li v-if="appointmentHasPatient">
            Patient: {{ selectedEvent.patient.firstName }}
            {{ selectedEvent.patient.lastName }}
          </li>
          <li>
            Beginn:
            {{ selectedEvent.start && selectedEvent.start.formatTime() }}
          </li>
          <li>
            Ende:
            {{ selectedEvent.end && selectedEvent.end.formatTime() }}
          </li>
        </ul>
      </v-card-text>
      <v-btn v-if="!appointmentHasPatient" @click="bookAppointment"
        >Termin buchen</v-btn
      >
    </v-card>
  </v-dialog>

  <!-- Dialog für neuen Termin -->
  <!-- @TODO: implement timepicker, appointment duration and calculate endtime -->
  <v-dialog v-model="showCreateDialog" style="width: 500px">
    <v-card>
      <v-card-title>Neuen Termin erstellen</v-card-title>
      <v-card-text>
        <v-text-field v-model="newEvent.title" label="Titel"></v-text-field>

        <v-select
          label="Termintyp"
          :items="['OFFLINE', 'ONLINE']"
          v-model="newEvent.type"
        ></v-select>

        <!-- Dropdown für Startzeit -->
        <v-select
            v-model="selectedStartTime"
            :items="startTimes"
            label="Startzeit"
        ></v-select>

        <!-- Dropdown für die Dauer -->
        <v-select
            v-model="selectedDuration"
            :items="durations"
            label="Termindauer (Minuten)"
            item-text="value"
            item-value="value"
        ></v-select>
        <p>Beginn: {{ newEvent.start }}</p>
        <p>Ende: {{ calculateEndTime(newEvent.start, selectedDuration) }}</p>
      </v-card-text>
      <v-card-actions>
        <v-btn color="primary" @click="createAppointment">Speichern</v-btn>
        <v-btn color="secondary" @click="showCreateDialog = false"
          >Abbrechen</v-btn
        >
      </v-card-actions>
    </v-card>
  </v-dialog>

  <v-snackbar v-model="snackbar.show" :color="snackbar.color" timeout="3000">
    {{ snackbar.message }}
  </v-snackbar>
</template>

<style scoped>
.doctor-img {
  width: 150px;
  height: 150px;
}
.vuecal__event {
  cursor: pointer;
}

.vuecal__event-title {
  font-size: 1.2em;
  font-weight: bold;
  margin: 4px 0 8px;
}

.vuecal__event-time {
  display: inline-block;
  margin-bottom: 12px;
  padding-bottom: 12px;
  border-bottom: 1px solid rgba(0, 0, 0, 0.2);
}

.vuecal__event-content {
  font-style: italic;
}


</style>
