<script setup lang="ts">
import type { Appointment, Doctor } from "@/types/types";
import { ref, onMounted } from "vue";


const date = new Date();

const dummyDoctor = ref<Doctor>({
  id: 1,
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
});

const events = ref<Appointment[]>([
  {
    start: "2024-11-25 10:30",
    end: "2024-11-25 11:30",
    title: "Doctor appointment",
    content: '<i class="icon material-icons">local_hospital</i>',
    class: "health",
  },
]);

const newEvent = ref<Appointment>({
  start: "",
  end: "",
  Patient: "",
  Behandlung: "",
  class: "new-event",
});

const AppointmentDuration = 30;
const selectedEvent = ref();
const showDialog = ref(false);
const showCreateDialog = ref(false);

const saveEventsToLocalStorage = () => {
  localStorage.setItem("events", JSON.stringify(events.value));
};

const loadEventsFromLocalStorage = () => {
  const savedEvents = localStorage.getItem("events");
  if (savedEvents) {
    events.value = JSON.parse(savedEvents);
  }
};


// (event: MouseEvent, appointment: Appointment)
const onEventClick = (appointment: Appointment, mouseevent: MouseEvent) => {
  selectedEvent.value = appointment;
  showDialog.value = true;
};

const createAppointment = () => {
  /*if (!newEvent.value.title.trim() || !newEvent.start || !newEvent.end) {
    alert("Bitte füllen Sie alle Felder aus.");
    return;
  }*/

  // Kombinieren von Datum und Zeit zu ISO-Strings
  const startDateTime = new Date(`${newEvent.value.date}T${newEvent.value.start}`);
  const endDateTime = new Date(`${newEvent.value.date}T${newEvent.value.end}`);

  // Event hinzufügen
  events.value.push({
    title: newEvent.value.title,
    start: startDateTime.toISOString(),
    end: endDateTime.toISOString(),
    class: "new-event",

  });

  console.log("Titel:", newEvent.value.title);
  console.log("Startzeit:", newEvent.value.start);
  console.log("Endzeit:", newEvent.value.end);


  // Speichere in LocalStorage
  saveEventsToLocalStorage();

  // Dialog schließen und Felder zurücksetzen
  showCreateDialog.value = false;
  newEvent.value = { title: "", start: "", end: "", date: "" };
};

const onCellClick = (date: Date) => {
  const originalMinutes = date.getMinutes();
  const roundedMinutes = originalMinutes < 30 ? 30 : 0; // Runde auf die nächste halbe Stunde
  const startTime = new Date(date);

  // Setze die Stunden korrekt und die Minuten auf den gerundeten Wert
  if (roundedMinutes === 0) {
    startTime.setHours(startTime.getHours() + 1); // Nächste volle Stunde
  }
  startTime.setMinutes(roundedMinutes);
  startTime.setSeconds(0);
  startTime.setMilliseconds(0);

  // Endzeit 30 Minuten später
  const endTime = new Date(startTime.getTime() + AppointmentDuration * 60 * 1000);

  // Werte zuweisen
  newEvent.value.date = startTime.toISOString().split("T")[0]; // YYYY-MM-DD
  newEvent.value.start = startTime.toISOString().split("T")[1].slice(0, 5); // HH:mm
  newEvent.value.end = endTime.toISOString().split("T")[1].slice(0, 5); // HH:mm

  console.log("Original Datum:", date.toISOString());
  console.log("Gerundete Startzeit:", startTime.toISOString());
  console.log("Endzeit:", endTime.toISOString());
  console.log("newEvent.value:", newEvent.value);

  showCreateDialog.value = true; // Dialog öffnen
};

const bookAppointment = () => {
  showDialog.value = false;
};

onMounted(() => {
  loadEventsFromLocalStorage();
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
      style="height: 850px"
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
      <v-btn @click="bookAppointment">Termin buchen</v-btn>
    </v-card>
  </v-dialog>


  <!-- Dialog für neuen Termin -->
  <v-dialog v-model="showCreateDialog" style="width: 500px">
    <v-card>
      <v-card-title>Neuen Termin erstellen</v-card-title>
      <v-card-text>
        <v-text-field
            v-model="newEvent.title"
            label="Patient"
            required
        ></v-text-field>
        <v-text-field
            v-model="newEvent.content"
            label="Behandlung"
        ></v-text-field>
        <p>Beginn: {{ newEvent.start }}</p>
        <p>Ende: {{ newEvent.end }}</p>
      </v-card-text>
      <v-card-actions>
        <v-btn color="primary" @click="createAppointment">Speichern</v-btn>
        <v-btn color="secondary" @click="showCreateDialog = false">Abbrechen</v-btn>
      </v-card-actions>
    </v-card>
  </v-dialog>
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