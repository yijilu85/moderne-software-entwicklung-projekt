<script setup lang="ts">
import { ref, watch, computed } from "vue";
import type {
  Appointment,
  BackendAppointment,
  Doctor,
  User,
} from "@/types/types";

import { useUserStore } from "@/stores/userStore";
import { onMounted } from "vue";

const loggedInUser = ref<User | null>(null);

const todayAppointments = ref<Appointment[]>([]);
const pastAppointments = ref<Appointment[]>([]);
const futureAppointments = ref<Appointment[]>([]);

onMounted(() => {
  loggedInUser.value = useUserStore().getLoggedInUser;
});

watch(
  () => useUserStore().getLoggedInUser,
  (newUser) => {
    loggedInUser.value = newUser;
  }
);

const welcomeMessage = computed(() => {
  if (loggedInUser.value) {
    if (loggedInUser.value.userType === "DOCTOR") {
      return `Hallo ${loggedInUser.value.firstName}, hier sind deine Patiententermine`;
    } else if (loggedInUser.value.userType === "PATIENT") {
      return `Hallo ${loggedInUser.value.firstName}, hier sind deine Arzttermine`;
    }
  }
  return "Hallo, melde dich an, um deine Termine zu sehen";
});
</script>

<template>
  <main>
    <h1>Willkommen bei PatientCare</h1>
    <h2>
      {{ welcomeMessage }}
    </h2>
    <div>
      <h3>Heute</h3>
      <ul></ul>
    </div>
  </main>
</template>
