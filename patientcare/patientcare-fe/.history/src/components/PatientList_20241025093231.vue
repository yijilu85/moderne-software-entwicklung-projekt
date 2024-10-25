<script setup lang="ts">
import { onMounted, ref } from "vue";
import type { Patient } from "@/types/types";

const ROOT_URL = "http://localhost";
const SERVERPORT = 8080;

const patients = ref<Patient[]>([]);
onMounted(() => {
  console.log("hallo");
  const { data } = await axios.get(`${ROOT_URL}:${SERVERPORT}/csvdata`);
});
</script>

<template>
  <h1>Patienten</h1>
</template>

<style scoped></style>
