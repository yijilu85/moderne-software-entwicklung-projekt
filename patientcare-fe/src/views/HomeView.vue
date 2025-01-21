<script lang="ts" setup>

import {computed, onMounted, ref, watch} from "vue";
import type {Appointment, BackendAppointment, User,} from "@/types/types";
import {getAllTodayAppointmentsForUserWithTimeranges,} from "@/api/appointmentController";

import LoadingSpinner from "@/components/LoadingSpinner.vue";

import {formatDate, parseDate,} from "@/helpers/appointmentHelpers";

import {useUserStore} from "@/stores/userStore";

const loggedInUser = ref<User | null>(null);
const finishedLoading = ref(false);
const todayAppointments = ref<Appointment[]>([]);
const pastAppointments = ref<Appointment[]>([]);
const futureAppointments = ref<Appointment[]>([]);

const fetchAppointments = async () => {
  if (!loggedInUser.value) {
    return;
  }
  const data = loggedInUser.value.id
      ? await getAllTodayAppointmentsForUserWithTimeranges(loggedInUser.value.id)
      : [];

  finishedLoading.value = false;

  const mapping = [
    {
      list: pastAppointments,
      listKey: "past",
      descriptor: "vergangenen",
    },
    {
      list: todayAppointments,
      listKey: "today",
      descriptor: "heutigen",
    },
    {
      list: futureAppointments,
      listKey: "future",
      descriptor: "zukünftigen",
    },
  ];

  Object.entries(data).forEach(([key, value]) => {
    if (Array.isArray(value)) {
      value.forEach((item) => {
        let mapped = item as any as BackendAppointment;
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

        for (const mappingItem of mapping) {
          if (mappingItem.listKey === key) {
            console.log(
                `Termine zur ${mappingItem.descriptor} Liste hinzugefügt`
            );
            mappingItem.list.value.push(appointment);
          }
        }
      });
    }
  });
  finishedLoading.value = true;
};

onMounted(async () => {
  loggedInUser.value = JSON.parse(localStorage.getItem('user') || 'null') as User | null;
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

    <div class="mt-10">
      <div class="today-future">
        <v-card class="mr-10 pa-3" title="Heute" width="500">
          <v-list lines="two">
            <v-list-item
                v-for="item in todayAppointments"
                :subtitle="formatSubTitle(item, true)"
                :title="formatTitle(item, true)"
            >
              <template v-slot:append>
                <v-icon>
                  <a :href="`/appointment/${item.id}`" class="card-link">
                    <img
                        alt=""
                        class="clear-icon ml-4 align-self-center" src="@/assets/icons/edit.svg"/></a
                  ></v-icon>
              </template>
            </v-list-item>
          </v-list>
        </v-card>
        <v-card class="pa-3" title="Zukünftige" width="500">
          <v-list lines="two">
            <v-list-item
                v-for="item in futureAppointments"
                :subtitle="formatSubTitle(item, false)"
                :title="formatTitle(item, false)"
            >
              <template v-slot:append>
                <v-icon>
                  <a :href="`/appointment/${item.id}`" class="card-link">
                    <img
                        alt=""
                        class="clear-icon ml-4 align-self-center" src="@/assets/icons/edit.svg"/></a
                  ></v-icon>
              </template>
            </v-list-item>
          </v-list>
        </v-card>
      </div>
      <v-card class="mt-10 pa-3" title="Vergangene" width="500">
        <v-list lines="two">
          <v-list-item
              v-for="item in pastAppointments"
              :subtitle="formatSubTitle(item, false)"
              :title="formatTitle(item, false)"
          >
            <template v-slot:append>
              <v-icon>
                <a :href="`/appointment/${item.id}`" class="card-link">
                  <img
                      alt=""
                      class="clear-icon ml-4 align-self-center" src="@/assets/icons/edit.svg"/></a
                ></v-icon>
            </template>
          </v-list-item>
        </v-list>
      </v-card>
    </div>
    <LoadingSpinner v-if="!finishedLoading"/>
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