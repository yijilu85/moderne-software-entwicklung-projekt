<script setup lang="ts">
import { onMounted, ref } from "vue";
import type { Patient } from "@/types/types";

const patients = ref<Patient[]>([]);
onMounted(() => {
  console.log("hallo");
});
</script>

<template>
  <h1>Patienten</h1>
</template>

<style scoped></style>
