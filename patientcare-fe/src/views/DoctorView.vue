<script lang="ts" setup>
import type {Appointment, BackendAppointment, Doctor,} from "@/types/types";
import {computed, onMounted, ref, watch} from "vue";
import {getAllAppointmentsForUser} from "@/api/appointmentController";
import {getDoctor} from "@/api/doctorController";
import {useRoute} from "vue-router";
import {useUserStore} from "@/stores/userStore";
import Snackbar from "@/components/Snackbar.vue";
import {
  appointmentHasPatient,
  calculateEndTime,
  checkCanCancelAppointment,
  checkCanSeeAppointment,
  parseDate,
  roundToQuarterHour,
  useAppointmentHelpers,
} from "@/helpers/appointmentHelpers";

import LoadingSpinner from "@/components/LoadingSpinner.vue";

const {
  delAppointment,
  bookAppointment,
  cancelSelectedAppointment,
  createAppointment,
} = useAppointmentHelpers();

const date = new Date();
const doctor = ref<Doctor | null>(null);
const route = useRoute();
const events = ref<Appointment[]>([]);
const selectedDay = ref("");
const finishedLoading = ref(false);
const selectedEvent = ref();
const showDialog = ref(false);
const showCreateDialog = ref(false);
const durations = ref<number[]>([]);
const selectedDuration = ref<number>(15);
const startTimes = ref<string[]>([]);
const selectedStartTime = ref<string>("09:00");
const minDuration = ref<number>(15);

const loadDoctor = async (doctorId: number) => {
  try {
    const doctorData = await getDoctor(doctorId);
    if (!doctorData) {
      setSnackBar("Doktor-Daten konnten nicht geladen werden!", "error");
    } else {
      doctor.value = doctorData;
    }
  } catch (error) {
    setSnackBar("Fehler beim Abrufen der Doktor-Daten!", "error");
  }
};

watch(
    () => route.params.id,
    async (newId) => {
      console.log("Route ID geändert:", newId);
      const doctorId = Number(newId);
      if (!isNaN(doctorId)) {
        console.log("Validierte Doktor-ID:", doctorId);
        await loadDoctor(doctorId);
      } else {
        console.error("Ungültige ID aus Route:", newId);
      }
    }
);

const newEvent = ref<Appointment>({
  id: 0,
  start: "",
  end: "",
  patient: null,
  doctor: null,
  title: "",
});

const snackbar = ref({
  show: false,
  message: "",
  color: "success",
});

const showSnackbar = (message: string, color: string = "success") => {
  snackbar.value = {
    show: true,
    message,
    color,
  };
};

const onEventClick = (appointment: Appointment, mouseevent: MouseEvent) => {
  selectedEvent.value = appointment;
  showDialog.value = true;
};

const onCreateAppointment = async () => {
  const startDateTime = new Date(
      `${newEvent.value.date}T${selectedStartTime.value}`
  );
  const endDateTime = new Date(`${newEvent.value.date}T${newEvent.value.end}`);
  const payload = {
    doctor: {
      id: doctor.value?.id,
    },
    creator: {
      id: doctor.value?.id,
    },
    startDateTime: startDateTime,
    endDateTime: endDateTime,
    createdAt: Date.now(),
    type: newEvent.value.type,
    title: newEvent.value.title,
    notes: [],
  };

  await createAppointment(payload, showSnackbar);
  showCreateDialog.value = false;
  newEvent.value = {id: 0, title: "", start: "", end: "", date: ""};
  selectedDuration.value = minDuration.value;
  fetchAppointments();
};

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

watch(selectedStartTime, (newStartTime) => {
  if (newStartTime) {
    const [hours, minutes] = newStartTime.split(":").map(Number);
    const startDate = new Date();
    startDate.setHours(hours, minutes, 0, 0);

    const endDate = new Date(
        startDate.getTime() + selectedDuration.value * 60 * 1000
    );

    newEvent.value.start = newStartTime;
    newEvent.value.end = endDate.toTimeString().slice(0, 5);
  }
});
watch(selectedDuration, (newDuration) => {
  if (newDuration) {
    const [hours, minutes] = selectedStartTime.value.split(":").map(Number);
    const startDate = new Date();
    startDate.setHours(hours, minutes, 0, 0);

    const endDate = new Date(
        startDate.getTime() + selectedDuration.value * 60 * 1000
    );

    newEvent.value.start = selectedStartTime.value;
    newEvent.value.end = endDate.toTimeString().slice(0, 5);
  }
});

const onCellClick = (date: Date) => {
  if (doctor.value !== null && useUserStore().getLoggedInUser !== null) {
    if (useUserStore().getLoggedInUser?.id !== doctor.value.id) {
      alert("NO SOUP FOUR YOU");
      return;
    }
  }
  const startHour = 7;
  const endHour = 18;

  let startTime = roundToQuarterHour(new Date(date));

  if (startTime.getHours() < startHour) {
    startTime.setHours(startHour, 0, 0, 0);
  } else if (startTime.getHours() >= endHour) {
    startTime.setHours(endHour, 0, 0, 0);
  }

  const endTime = new Date(
      startTime.getTime() + selectedDuration.value * 60 * 1000
  );

  newEvent.value.date = startTime.toISOString().split("T")[0];
  newEvent.value.start = startTime.toLocaleTimeString().slice(0, 5);
  newEvent.value.end = endTime.toLocaleTimeString().slice(0, 5);
  selectedStartTime.value = newEvent.value.start;

  showCreateDialog.value = true;
};

const onBookAppointment = async () => {
  const loggedInUser = useUserStore().getLoggedInUser;
  await bookAppointment(selectedEvent.value.id, loggedInUser?.id, showSnackbar);
  fetchAppointments();
  showDialog.value = false;
};

const fetchAppointments = async () => {
  if (!doctor.value?.id) {
    console.error("Doctor ID nicht verfügbar");
    return;
  }

  events.value = [];
  finishedLoading.value = false;

  try {
    const data = await getAllAppointmentsForUser(doctor.value.id);

    if (!data || data.length === 0) {
      console.log("Keine Termine gefunden.");
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

      events.value.push(appointment);
    }
  } catch (error) {
    console.error("Fehler beim Abrufen der Termine:", error);
    setSnackBar("Fehler beim Abrufen der Termine!", "error");
  } finally {
    finishedLoading.value = true;
  }
};

const checkAppointmentHasPatient = computed(() => {
  return appointmentHasPatient(selectedEvent.value);
});

const deleteSelectedAppointment = async () => {
  if (!selectedEvent.value) {
    console.error("Kein Termin ausgewählt");
    return;
  }
  await delAppointment(
      selectedEvent.value.id,
      useUserStore().getLoggedInUser?.id,
      showSnackbar
  );
  fetchAppointments();
  showDialog.value = false;
};

const generateTimeOptions = () => {
  for (let i = 15; i <= 60; i += 15) {
    durations.value.push(i);
  }
  for (let hour = 7; hour < 18; hour++) {
    for (let minutes = 0; minutes < 60; minutes += 15) {
      const time = `${hour.toString().padStart(2, "0")}:${minutes
          .toString()
          .padStart(2, "0")}`;
      startTimes.value.push(time);
    }
  }
};

const loadInitialData = async () => {
  try {
    const doctorId = Number(route.params.id);
    console.log("Doctor ID aus der Route:", doctorId);
    if (!isNaN(doctorId)) {
      await loadDoctor(doctorId);
    } else {
      console.error("Ungültige ID in der URL:", route.params.id);
      setSnackBar("Ungültige ID in der URL!", "error");
    }
  } catch (error) {
    console.error("Fehler bei der Initialisierung:", error);
    setSnackBar("Fehler bei der Initialisierung!", "error");
  }
  await fetchAppointments();
};

const canCancelAppointment = computed(() => {
  return checkCanCancelAppointment(selectedEvent.value);
});

const onCancelSelectedAppointment = async () => {
  await cancelSelectedAppointment(
      selectedEvent.value,
      useUserStore().getLoggedInUser?.id,
      showSnackbar
  );
  fetchAppointments();
  showDialog.value = false;
};

const filterAppointmentsVisibility = computed(() => {
  return events.value.filter(checkCanSeeAppointment);
});

onMounted(async () => {
  generateTimeOptions();
  await loadInitialData();
});
</script>

<template>
  <div v-if="doctor" class="doctor-detail">
    <h1>{{ doctor.title }} {{ doctor.firstName }} {{ doctor.lastName }}</h1>
    <img
        :src="doctor.profileImg || 'https://cdn.vuetifyjs.com/images/john.png'"
        alt="Doctor profile image"
        class="doctor-img"
    />
    <p>{{ doctor.speciality }}</p>
    <p>{{ doctor.licenseId }}</p>
    <p>{{ doctor.street }} {{ doctor.houseNumber }}</p>
    <p>{{ doctor.zipCode }} {{ doctor.city }}</p>
  </div>
  <div v-else>
    <p>Loading doctor details...</p>
  </div>
  <h2 class="mt-8">Termin buchen</h2>
  <vue-cal
      v-if="finishedLoading"
      :events="filterAppointmentsVisibility"
      :on-event-click="onEventClick"
      :time-cell-height="80"
      style="height: 850px"
      @cell-click="onCellClick"
  />
  <v-dialog v-model="showDialog" style="width: 500px">
    <v-card>
      <v-card-title>
        <span>{{ selectedEvent.title }}</span>
        <v-spacer/>
        <strong>{{
            selectedEvent.start && selectedEvent.start.format("DD/MM/YYYY")
          }}</strong>
      </v-card-title>
      <v-card-text>
        <p v-html="selectedEvent.contentFull"/>
        <strong>Termindetails</strong> -
        <a :href="`/appointment/${selectedEvent.id}`">Termin öffnen</a>
        <ul>
          <li v-if="checkAppointmentHasPatient">
            Patient: {{ selectedEvent.patient.firstName }}
            {{ selectedEvent.patient.lastName }}
          </li>
          <li>
            Beginn:
            {{ selectedEvent.start && selectedEvent.start.formatTime() }}
          </li>
          <li>
            Ende:
            {{ selectedEvent.end && selectedEvent.end.formatTime() }}
          </li>
        </ul>
      </v-card-text>
      <v-btn v-if="!checkAppointmentHasPatient" @click="onBookAppointment"
      >Termin buchen
      </v-btn
      >

      <v-btn
          v-if="canCancelAppointment"
          color="red-lighten-2"
          @click="onCancelSelectedAppointment"
      >
        Termin stornieren
      </v-btn
      >

      <v-btn
          v-if="selectedEvent?.doctor?.id === useUserStore().getLoggedInUser?.id"
          color="red"
          @click="deleteSelectedAppointment"
      >
        Termin löschen
      </v-btn>
    </v-card>
  </v-dialog>

  <v-dialog v-model="showCreateDialog" style="width: 500px">
    <v-card>
      <v-card-title>Neuen Termin erstellen</v-card-title>
      <v-card-text>
        <v-text-field v-model="newEvent.title" label="Titel"></v-text-field>

        <v-select
            v-model="newEvent.type"
            :items="['OFFLINE', 'ONLINE']"
            label="Termintyp"
        ></v-select>

        <v-select
            v-model="selectedStartTime"
            :items="startTimes"
            label="Startzeit"
        ></v-select>

        <v-select
            v-model="selectedDuration"
            :items="durations"
            item-text="value"
            item-value="value"
            label="Termindauer (Minuten)"
        ></v-select>
        <p>Ende: {{ calculateEndTime(newEvent.start, selectedDuration) }}</p>
      </v-card-text>
      <v-card-actions>
        <v-btn color="primary" @click="onCreateAppointment">Speichern</v-btn>
        <v-btn color="secondary" @click="showCreateDialog = false"
        >Abbrechen
        </v-btn
        >
      </v-card-actions>
    </v-card>
  </v-dialog>

  <Snackbar
      :color="snackbar.color"
      :message="snackbar.message"
      :show="snackbar.show"
      @update:show="snackbar.show = $event"
  />
  <LoadingSpinner v-if="!finishedLoading"/>
</template>

<style scoped>
.doctor-img {
  width: 150px;
  height: 150px;
}

.vuecal__event {
  cursor: pointer;
}

.vuecal__event-title {
  font-size: 1.2em;
  font-weight: bold;
  margin: 4px 0 8px;
}

.vuecal__event-time {
  display: inline-block;
  margin-bottom: 12px;
  padding-bottom: 12px;
  border-bottom: 1px solid rgba(0, 0, 0, 0.2);
}

.vuecal__event-content {
  font-style: italic;
}
</style>
