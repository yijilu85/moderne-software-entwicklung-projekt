<script setup lang="ts">
import { onMounted, ref } from "vue";
import type { Patient } from "@/types/types";
import axios from "axios";

const ROOT_URL = "http://localhost";
const SERVERPORT = 8080;

const patients = ref<Patient[]>([]);

onMounted(async () => {
  console.log("init patientlist");
  const res = await axios.get(`${ROOT_URL}:${SERVERPORT}/patients/1`);
  const patient = res.data as Patient;
  patients.value.push(patient);
});
</script>

<template>
  <h1>Patienten</h1>
  <ul>
    <li v-for="patient in patients">
      {{ patient.firstName }} {{ patient.lastName }}
    </li>
  </ul>
</template>

<style scoped></style>
