<script setup>
import { storeToRefs } from 'pinia';

import { useAuthStore, useUsersStore } from '@/stores';

const authStore = useAuthStore();
const { user: authUser } = storeToRefs(authStore);

const usersStore = useUsersStore();
const { users } = storeToRefs(usersStore);

usersStore.getAll();
<script setup lang="ts">
import { ref, watch, computed } from "vue";
import type {
  Appointment,
  BackendAppointment,
  Doctor,
  User,
} from "@/types/types";
import {
  getAllAppointmentsForUser,
  getAllPastAppointmentsForUser,
  getAllFutureAppointmentsForUser,
  getAllTodayAppointmentsForUser,
} from "@/api/appointmentController";
import { authenticate } from "@/api/authentificationController";

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
import { onMounted } from "vue";

const loggedInUser = ref<User | null>(null);
const finishedLoading = ref(false);
const todayAppointments = ref<Appointment[]>([]);
const pastAppointments = ref<Appointment[]>([]);
const futureAppointments = ref<Appointment[]>([]);

const fetchAppointments = async () => {
  finishedLoading.value = false;

  const mapping = [
    {
      list: pastAppointments,
      function: getAllPastAppointmentsForUser,
      descriptor: "vergangenen",
    },
    {
      list: todayAppointments,
      function: getAllTodayAppointmentsForUser,
      descriptor: "heutigen",
    },
    {
      list: futureAppointments,
      function: getAllFutureAppointmentsForUser,
      descriptor: "zukünftigen",
    },
  ];

  for (const { list, function: fetchFunction, descriptor } of mapping) {
    fetchFunction(loggedInUser.value.id)
      .then((data) => {
        if (!data || data.length === 0) {
          console.log(`Keine ${descriptor} Termine gefunden.`);
          return;
        }

        for (const event of data) {
          if (!event) continue;

          let mapped = event as any as BackendAppointment;
          const appointment = {
            id: mapped.id,
            start: parseDate(mapped.startDateTime),
            end: parseDate(mapped.endDateTime),
            patient: mapped.patient,
            title: mapped.title,
            doctor: mapped.doctor,
            notes: mapped.notes,
            type: mapped.type,
          } as Appointment;

          list.value.push(appointment);
        }
      })
      .catch((error) => {
        console.error("Fehler beim Abrufen der Termine:", error);
        setSnackBar("Fehler beim Abrufen der Termine!", "error");
      })
      .finally(() => {
        finishedLoading.value = true;
      });
  }
};

onMounted(async () => {
  loggedInUser.value = useUserStore().getLoggedInUser;
  fetchAppointments();
});

watch(
  () => useUserStore().getLoggedInUser,
  (newUser) => {
    loggedInUser.value = newUser;
    fetchAppointments();
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

const formatTitle = (appointment: Appointment, today: boolean) => {
  if (today) {
    return `${formattedAppointmentTime(
      appointment
    )}: ${formattedAppointmentPatient(appointment)}`;
  } else {
    return `${formatDate(
      appointment.start,
      "date"
    )} um ${formattedAppointmentTime(
      appointment
    )}: ${formattedAppointmentPatient(appointment)}`;
  }
};
const formatSubTitle = (appointment: Appointment, today: boolean) => {
  if (today) {
    return appointment.title;
  } else {
    return `${appointment.title}: ${formattedAppointmentPatient(appointment)}`;
  }
};

const formattedAppointmentTime = (appointment: Appointment) => {
  return `${formatDate(appointment.start, "time")} - ${formatDate(
    appointment.end,
    "time"
  )}`;
};

const formattedAppointmentPatient = (appointment: Appointment) => {
  if (appointment.patient) {
    return `${appointment.patient.firstName} ${appointment.patient.lastName}`;
  } else {
    return "nicht gebucht";
  }
};
</script>

<template>
  <main>
    <h1>Willkommen bei PatientCare</h1>
    <h2>
      {{ welcomeMessage }}
    </h2>
     <div>
    <h1>Hi {{authUser?.firstName}}!</h1>
    <p>You're logged in with Vue 3 + Pinia & JWT!!</p>
    <h3>Users from secure api end point:</h3>
    <ul v-if="users.length">
      <li v-for="user in users" :key="user.id">{{user.firstName}} {{user.lastName}}</li>
    </ul>
    <div v-if="users.loading" class="spinner-border spinner-border-sm"></div>
    <div v-if="users.error" class="text-danger">Error loading users: {{users.error}}</div>
  </div>
    <div class="mt-10">
      <div class="today-future">
        <v-card title="Heute" class="mr-10 pa-3" width="500">
          <v-list lines="two">
            <v-list-item
              v-for="item in todayAppointments"
              :title="formatTitle(item, true)"
              :subtitle="formatSubTitle(item, true)"
            >
              <template v-slot:append>
                <v-icon>
                  <a :href="`/appointment/${item.id}`" class="card-link">
                    <img
                      src="@/assets/icons/edit.svg"
                      class="clear-icon ml-4 align-self-center" /></a
                ></v-icon>
              </template>
            </v-list-item>
          </v-list>
        </v-card>
        <v-card title="Zukünftige" class="pa-3" width="500">
          <v-list lines="two">
            <v-list-item
              v-for="item in futureAppointments"
              :title="formatTitle(item, false)"
              :subtitle="formatSubTitle(item, false)"
            >
              <template v-slot:append>
                <v-icon>
                  <a :href="`/appointment/${item.id}`" class="card-link">
                    <img
                      src="@/assets/icons/edit.svg"
                      class="clear-icon ml-4 align-self-center" /></a
                ></v-icon>
              </template>
            </v-list-item>
          </v-list>
        </v-card>
      </div>
      <v-card title="Vergangene" class="mt-10 pa-3" width="500">
        <v-list lines="two">
          <v-list-item
            v-for="item in pastAppointments"
            :title="formatTitle(item, false)"
            :subtitle="formatSubTitle(item, false)"
          >
            <template v-slot:append>
              <v-icon>
                <a :href="`/appointment/${item.id}`" class="card-link">
                  <img
                    src="@/assets/icons/edit.svg"
                    class="clear-icon ml-4 align-self-center" /></a
              ></v-icon>
            </template>
          </v-list-item>
        </v-list>
      </v-card>
    </div>
  </main>
 
</template>

<style scoped>
.card-link {
  margin-top: 15px;
}
.today-future {
  display: flex;
  flex-flow: row nowrap;
}
</style>
