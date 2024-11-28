<script setup lang="ts">
import type { Appointment, Doctor } from "@/types/types";
import { onMounted, ref, computed, watch } from "vue";
import type { Ref } from "vue";
import { getAllDoctors } from "@/api/doctorController";
import { useRouter } from "vue-router";

const specialities = ref<string[]>([]);
const cities = ref<string[]>([]);
const doctors = ref<Doctor[]>([]);
const cityFilter = ref<string | null>(null);
const specialtyFilter = ref<string | null>(null);
const router = useRouter();

const filteredDoctorList = computed(() => {
  let filteredList = doctors.value;

  if (cityFilter.value) {
    filteredList = filteredList.filter(
      (doctor) => doctor.city === cityFilter.value
    );
  }
  if (specialtyFilter.value) {
    filteredList = filteredList.filter(
      (doctor) => doctor.speciality === specialtyFilter.value
    );
  }
  return filteredList;
});

const populateSpecialities = () => {
  specialities.value = [];
  for (const doctor of filteredDoctorList.value) {
    if (!specialities.value.includes(doctor.speciality)) {
      specialities.value.push(doctor.speciality);
    }
  }
};
const populateCities = () => {
  cities.value = [];
  for (const doctor of filteredDoctorList.value) {
    if (doctor.city) {
      if (!cities.value.includes(doctor.city)) {
        cities.value.push(doctor.city);
      }
    }
  }
};

const fetchDoctors = async () => {
  const data = await getAllDoctors();
  doctors.value = data;
};

const doctorCardTitle = (doctor: Doctor) => {
  let titleStr = "";
  if (doctor.title) {
    titleStr = doctor.title;
  }

  return `${titleStr} ${doctor.firstName} ${doctor.lastName}`;
};
const doctorCardSubtitle = (doctor: Doctor) => {
  return `${doctor.speciality}, ${doctor.city}`;
};

const handleClick = (doctor: Doctor) => {
  router.push({ name: "doctor", params: { id: doctor.id } });
};

const clearField = (fieldname: string) => {
  if (fieldname === "speciality") {
    specialtyFilter.value = null;
  }
  if (fieldname === "city") {
    cityFilter.value = null;
  }
};

onMounted(async () => {
  await fetchDoctors();
  populateSpecialities();
  populateCities();
});
</script>

<template>
  <h1>Ärztesuche</h1>
  <div class="filter-header">
    <v-combobox
      clearable
      label="Fachrichtung"
      :items="specialities"
      v-model="specialtyFilter"
      class="filter-field"
    >
      <template v-slot:append-inner>
        <img
          v-if="specialtyFilter"
          src="@/assets/icons/x-circle.svg"
          class="clear-icon"
          @click.prevent="clearField('speciality')"
        />
      </template>
    </v-combobox>
    <v-combobox
      clearable
      label="Stadt"
      :items="cities"
      v-model="cityFilter"
      class="filter-field"
    >
      <template v-slot:append-inner>
        <img
          v-if="cityFilter"
          src="@/assets/icons/x-circle.svg"
          class="clear-icon"
          @click.prevent="clearField('city')"
        /> </template
    ></v-combobox>
  </div>

  <div class="doctor-grid">
    <h2>Gefundene Ärtze ({{ filteredDoctorList.length }})</h2>
    <v-row justify="start" dense>
      <v-col
        v-for="doctor in filteredDoctorList"
        cols="12"
        md="4"
        class="doctor-card"
      >
        <v-card
          class="mx-auto"
          :subtitle="doctorCardSubtitle(doctor)"
          :title="doctorCardTitle(doctor)"
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
          <v-btn
            color="teal-accent-4"
            text="Mehr Infos"
            variant="text"
            @click="handleClick(doctor)"
          ></v-btn>
        </v-card>
      </v-col>
    </v-row>
  </div>
</template>

<style scoped>
.filter-header {
  display: flex;
  gap: 10px;
}

.clear-icon {
  height: 20px;
  width: 20px;
  cursor: pointer;
}

.filter-field {
  width: 50%;
}
</style>
