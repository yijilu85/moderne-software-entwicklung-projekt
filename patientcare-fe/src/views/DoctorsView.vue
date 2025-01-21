<script lang="ts" setup>
import type {Doctor} from "@/types/types";
import {computed, onMounted, ref, watch} from "vue";
import {getAllDoctors} from "@/api/doctorController";
import {useRouter} from "vue-router";
import LoadingSpinner from "@/components/LoadingSpinner.vue";

const specialities = ref<string[]>([]);
const cities = ref<string[]>([]);
const doctors = ref<Doctor[]>([]);
const cityFilter = ref<string | null>(null);
const specialtyFilter = ref<string | null>(null);
const router = useRouter();
const finishedLoading = ref(false);

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
  finishedLoading.value = false;
  const data = await getAllDoctors();
  doctors.value = data;
  finishedLoading.value = true;
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
  router.push({name: "doctor", params: {id: doctor.id}});
};

const clearField = (fieldname: string) => {
  if (fieldname === "speciality") {
    specialtyFilter.value = null;
  }
  if (fieldname === "city") {
    cityFilter.value = null;
  }
};

watch(cityFilter, (newCity, oldCity) => {
  populateSpecialities();
});

watch(specialtyFilter, (newSpecialty, oldSpecialty) => {
  populateCities();
});

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
        v-model="specialtyFilter"
        :items="specialities"
        class="filter-field"
        clearable
        label="Fachrichtung"
    >
      <template v-slot:append-inner>
        <img
            v-if="specialtyFilter"
            class="clear-icon"
            src="@/assets/icons/x-circle.svg"
            @click.prevent="clearField('speciality')"
        />
      </template>
    </v-combobox>
    <v-combobox
        v-model="cityFilter"
        :items="cities"
        class="filter-field"
        clearable
        label="Stadt"
    >
      <template v-slot:append-inner>
        <img
            v-if="cityFilter"
            class="clear-icon"
            src="@/assets/icons/x-circle.svg"
            @click.prevent="clearField('city')"
        /></template
      >
    </v-combobox>
  </div>

  <div class="doctor-grid">
    <h2>Gefundene Ärtze ({{ filteredDoctorList.length }})</h2>
    <v-row dense justify="start">
      <v-col
          v-for="doctor in filteredDoctorList"
          class="doctor-card"
          cols="12"
          md="4"
      >
        <v-card
            :subtitle="doctorCardSubtitle(doctor)"
            :title="doctorCardTitle(doctor)"
            class="mx-auto"
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
  <LoadingSpinner v-if="!finishedLoading"/>
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
