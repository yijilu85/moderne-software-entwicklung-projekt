<script lang="ts" setup>
import {onMounted, ref} from "vue";
import type {Patient} from "@/types/types";
import {getPatient} from "@/api/patientController";
import axios from "axios";

const ROOT_URL = "http://localhost";
const SERVERPORT = 8080;

const patients = ref<Patient[]>([]);

onMounted(async () => {
  const patient = await getPatient(1);
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
