<script setup lang="ts">
import type { Appointment, Doctor } from "@/types/types";
import { ref } from "vue";

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

const selectedEvent = ref();
const showDialog = ref(false);

// (event: MouseEvent, appointment: Appointment)
const onEventClick = (appointment, mouseevent) => {
  selectedEvent.value = appointment;
  showDialog.value = true;
};

const bookAppointment = () => {
  showDialog.value = false;
};
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
