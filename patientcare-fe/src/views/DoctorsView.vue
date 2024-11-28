<script setup lang="ts">
import type { Appointment, Doctor } from "@/types/types";
import { onMounted, ref } from "vue";
import { getAllDoctors } from "@/api/doctorController";
const date = new Date();
const specialities = ref<string[]>([]);
const doctors = ref<Doctor[]>([
  {
    id: 1,
    firstName: "Hans",
    lastName: "Ulrich",
    street: "Berliner Straße",
    houseNumber: "12",
    zipCode: "12345",
    city: "Berlin",
    dateOfBirth: date,
    title: "Dr. med.",
    speciality: "Dermatologie",
    medicalId: "123asd",
    profileImg: "dummy-doctor.png",
    userType: "DOCTOR",
  },
  {
    id: 2,
    firstName: "Hans",
    lastName: "Ulrich",
    street: "Berliner Straße",
    houseNumber: "12",
    zipCode: "12345",
    city: "Berlin",
    dateOfBirth: date,
    title: "Dr. med.",
    speciality: "Dentologie",
    medicalId: "123asd",
    profileImg: "dummy-doctor.png",
    userType: "DOCTOR",
  },
  {
    id: 3,
    firstName: "Hans",
    lastName: "Ulrich",
    street: "Berliner Straße",
    houseNumber: "12",
    zipCode: "12345",
    city: "Berlin",
    dateOfBirth: date,
    title: "Dr. med.",
    speciality: "Urologie",
    medicalId: "123asd",
    profileImg: "dummy-doctor.png",
    userType: "DOCTOR",
  },
  {
    id: 4,
    firstName: "Hans",
    lastName: "Ulrich",
    street: "Berliner Straße",
    houseNumber: "12",
    zipCode: "12345",
    city: "Berlin",
    dateOfBirth: date,
    title: "Dr. med.",
    speciality: "Urologie",
    medicalId: "123asd",
    profileImg: "dummy-doctor.png",
    userType: "DOCTOR",
  },
  {
    id: 5,
    firstName: "Hans",
    lastName: "Ulrich",
    street: "Berliner Straße",
    houseNumber: "12",
    zipCode: "12345",
    city: "Berlin",
    dateOfBirth: date,
    title: "Dr. med.",
    speciality: "Dentologie",
    medicalId: "123asd",
    profileImg: "dummy-doctor.png",
    userType: "DOCTOR",
  },
]);

const populateSpecialities = () => {
  for (const doctor of doctors.value) {
    if (!specialities.value.includes(doctor.speciality)) {
      specialities.value.push(doctor.speciality);
    }
  }
  console.log("specialities", specialities.value);
};

const fetchDoctors = async () => {
  const data = await getAllDoctors();
  doctors.value = data;
  populateSpecialities();
};

onMounted(() => {
  fetchDoctors();
  // populateSpecialities();
});
</script>

<template>
  <h1>Ärztesuche</h1>
  {{ doctors }}
  <v-combobox clearable label="Fachrichtung" :items="specialities"></v-combobox>

  <!-- <div class="doctor-detail">
    <h1>
      {{ dummyDoctor.title }} {{ dummyDoctor.firstName }}
      {{ dummyDoctor.lastName }}
    </h1>
    <img class="doctor-img" src="@/assets/images/dummy-doctor.png" />
    <p>{{ dummyDoctor.speciality }}</p>
    <p>{{ dummyDoctor.medicalId }}</p>
    <p>{{ dummyDoctor.street }} {{ dummyDoctor.houseNumber }}</p>
    <p>{{ dummyDoctor.zipCode }} {{ dummyDoctor.city }}</p>
  </div> -->

  <!-- <h2>Termin buchen</h2>
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
  </v-dialog> -->
</template>

<style scoped></style>
