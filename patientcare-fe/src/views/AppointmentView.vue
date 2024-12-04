<script setup lang="ts">
import type {
  Appointment,
  BackendAppointment,
  Doctor,
  Patient,
  User,
} from "@/types/types";
import { ref, onMounted, computed } from "vue";
import {
  createAppointmentSlot,
  getAllAppointmentsForUser,
  sendBookingAppointment,
  deleteAppointment,
  cancelAppointment,
  getAppointment,
} from "@/api/appointmentController";
import { watch } from "vue";
import { getPatient } from "@/api/patientController";
import { getDoctor } from "@/api/doctorController";
import { useRouter, useRoute } from "vue-router";
import {
  mapBackendToFrontend,
  formatDate,
  checkCanCancelAppointment,
} from "@/helpers/appointmentHelpers";
import { useUserStore } from "@/stores/userStore";

const router = useRouter();
const route = useRoute();
const doctor = ref<Doctor | null>(null);
const patient = ref<Patient | null>(null);
const events = ref<Appointment[]>([]);
const finishedLoading = ref(false);
const selectedEvent = ref();
const showDialog = ref(false);

const event = ref<Appointment>();

const snackbar = ref({
  show: false,
  message: "",
  color: "success",
});

const setSnackBar = (message: string, type: string) => {
  if (type === "success") {
    snackbar.value = {
      show: true,
      message: message,
      color: "success",
    };
  } else if (type === "error") {
    snackbar.value = {
      show: true,
      message: message,
      color: "error",
    };
  }
};

const fetchAppointment = async () => {
  const routeId = route.params.id;
  if (!routeId) {
    setSnackBar("Termin nicht gefunden", "error");
    router.push("/");
  }
  finishedLoading.value = false;
  const appointment = (await getAppointment(
    Number(routeId)
  )) as any as BackendAppointment;

  if (appointment) {
    event.value = mapBackendToFrontend(appointment);
    console.log("appointment", appointment);
    doctor.value = await getDoctor(appointment.doctor?.id);
    patient.value = await getPatient(appointment.patient?.id);
  }
  finishedLoading.value = true;
};

const canCancelAppointment = computed(() => {
  return checkCanCancelAppointment(selectedEvent.value);
});

const appointmentHasPatient = computed(() => {
  if (event.value?.patient?.id) {
    return true;
  } else {
    return false;
  }
});
const doctorCardTitle = (doctor: Doctor) => {
  let titleStr = "";
  if (doctor.title) {
    titleStr = doctor.title;
  }

  return `${titleStr} ${doctor.firstName} ${doctor.lastName}`;
};
const patientCardTitle = (patient: Patient) => {
  return `${patient.firstName} ${patient.lastName}`;
};
const doctorCardSubtitle = (doctor: Doctor) => {
  return `${doctor.speciality}, ${doctor.city}`;
};

onMounted(async () => {
  fetchAppointment();
});
</script>

<template>
  <div>
    <h1>Termin {{ event?.title }}</h1>
    <h3>
      am {{ formatDate(event?.start, "date") }}
      {{ formatDate(event?.start, "time") }} bis
      {{ formatDate(event?.end, "time") }}
    </h3>
  </div>
  <v-snackbar v-model="snackbar.show" :color="snackbar.color" timeout="3000">
    {{ snackbar.message }}
  </v-snackbar>
  <v-row justify="start" dense>
    <v-col cols="12" md="4" class="doctor-card">
      <v-card
        class="mx-auto"
        variant="text"
        :subtitle="doctorCardSubtitle(doctor)"
        :title="doctorCardTitle(doctor)"
        v-if="doctor"
      >
        <template v-slot:append>
          <v-avatar size="60">
            <v-img
              alt="John"
              src="https://cdn.vuetifyjs.com/images/john.png"
            ></v-img>
          </v-avatar>
        </template>
        <v-card-text>
          <p>{{ doctor.street }} {{ doctor.houseNumber }}</p>
          <p>{{ doctor.zipCode }} {{ doctor.city }}</p>
          <p>{{ doctor.phoneNumber }}</p>
          <p>{{ doctor.email }}</p>
        </v-card-text>
      </v-card>
    </v-col>
    <v-divider vertical></v-divider>
    <v-col cols="12" md="4" class="doctor-card">
      <v-card
        variant="text"
        class="mx-auto"
        title="Patient"
        :subtitle="patientCardTitle(patient)"
        v-if="patient"
      >
        <template v-slot:append>
          <v-avatar size="60">
            <v-img
              alt="John"
              src="https://cdn.vuetifyjs.com/images/john.png"
            ></v-img>
          </v-avatar>
        </template>
        <v-card-text>
          <p>{{ patient.street }} {{ patient.houseNumber }}</p>
          <p>{{ patient.zipCode }} {{ patient.city }}</p>
          <p>{{ patient.phoneNumber }}</p>
          <p>{{ patient.email }}</p>
        </v-card-text>
        <v-btn
          v-if="canCancelAppointment"
          color="red-lighten-2"
          @click="cancelSelectedAppointment"
        >
          Termin stornieren</v-btn
        >
      </v-card>
    </v-col>
  </v-row>
</template>

<style scoped></style>
