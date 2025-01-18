import type {
  Appointment,
  BackendAppointment,
  Doctor,
  Patient,
  User,
} from "@/types/types";

import {
  createAppointmentSlot,
  getAllAppointmentsForUser,
  sendBookingAppointment,
  deleteAppointment,
  cancelAppointment,
} from "@/api/appointmentController";
import { useUserStore } from "@/stores/userStore";

export function mapBackendToFrontend(
  backendData: BackendAppointment
): Appointment {
  return {
    id: backendData.id,
    date: backendData.date,
    start: backendData.startDateTime, // Map startDateTime to start
    end: backendData.endDateTime, // Map endDateTime to end
    title: backendData.title,
    content: backendData.content,
    class: backendData.class,
    patient: backendData.patient,
    doctor: backendData.doctor,
    type: backendData.type,
  };
}

export function formatDate(dateTimeString: string, output: string) {
  const date = new Date(dateTimeString);

  switch (output) {
    case "date":
      return date.toLocaleDateString();
    case "time": {
      const hours = String(date.getHours()).padStart(2, "0");
      const minutes = String(date.getMinutes()).padStart(2, "0");
      return `${hours}:${minutes}`;
    }
  }
  return dateTimeString;
}

export const useAppointmentHelpers = () => {
  const delAppointment = async (
    appointmentId: number,
    userId: number | undefined,
    showSnackbar: (message: string, color?: string) => void
  ) => {
    try {
      const confirmDeletion = confirm(
        "Möchten Sie diesen Termin wirklich löschen?"
      );
      if (!confirmDeletion) return;

      await deleteAppointment(appointmentId, userId); // Mock API call
      showSnackbar("Termin erfolgreich gelöscht!", "success");
    } catch (error) {
      console.error("Fehler beim Löschen des Termins:", error);
      showSnackbar("Fehler beim Löschen des Termins!", "error");
    }
  };

  const bookAppointment = async (
    appointmentId: number,
    userId: number | undefined,
    showSnackbar: (message: string, color?: string) => void
  ) => {
    const payload = {
      appointmentId: appointmentId,
      patientId: userId,
    };

    try {
      await sendBookingAppointment(payload);

      showSnackbar("Termin erfolgreich gebucht!", "success");
    } catch (error) {
      console.error("Error booking appointment:", error);
      showSnackbar("Fehler beim Buchen des Termins!", "error");
    }
  };

  const cancelSelectedAppointment = async (
    appointment: Appointment,
    userId: number | undefined,
    showSnackbar: (message: string, color?: string) => void
  ) => {
    if (!appointment.id) {
      console.error("Kein Termin ausgewählt");
      return;
    }
    const loggedInUser = useUserStore().getLoggedInUser;
    console.log(appointment.id, userId);
    if (
      !(
        (loggedInUser?.userType === "DOCTOR" &&
          appointment.doctor?.id === loggedInUser?.id) ||
        (loggedInUser?.userType === "PATIENT" &&
          appointment.patient?.id === loggedInUser?.id)
      )
    ) {
      console.warn("Der Benutzer ist nicht der Patient dieses Termins.");
      alert("Sie können nur Termine stornieren, die Sie gebucht haben.");
      return;
    }

    try {
      const confirmCancellation = confirm(
        "Möchten Sie diesen Termin wirklich stornieren?"
      );
      if (!confirmCancellation) return;

      const payload = {
        appointmentId: appointment.id,
        userId: loggedInUser?.id as number, // Explicit assertion to number
      };

      if (payload.userId !== undefined) {
        await cancelAppointment(payload);
      } else {
        console.error("Fehlender Benutzer-ID für die Termin-Stornierung");
      }
      showSnackbar("Termin erfolgreich storniert!", "success");
    } catch (error) {
      console.error("Fehler beim Stornieren des Termins:", error);
      showSnackbar("Fehler beim Stornieren des Termins!", "error");
    }
  };

  const createAppointment = async (
    appointment: any,
    showSnackbar: (message: string, color?: string) => void
  ) => {
    try {
      console.log("event payload", appointment);
      await createAppointmentSlot(appointment);
      showSnackbar("Termin erfolgreich erstellt!", "success");
    } catch {
      showSnackbar("Fehler beim Erstellen eines Termins!", "error");
    }
  };

  return {
    delAppointment,
    bookAppointment,
    cancelSelectedAppointment,
    createAppointment,
  };
};

export function parseDate(date: string) {
  if (date === undefined) return;
  let parsedDate = date.replace("T", " ").slice(0, -3);
  return parsedDate;
}

export function roundToQuarterHour(date: Date) {
  const minutes = date.getMinutes();
  const roundedMinutes = Math.ceil(minutes / 15) * 15;
  date.setMinutes(roundedMinutes, 0, 0);
  return date;
}

export function checkCanCancelAppointment(
  selectedEvent: Appointment | undefined
) {
  if (!selectedEvent || !selectedEvent.patient) {
    return false;
  }
  const loggedInUser = useUserStore().getLoggedInUser;
  return (
    selectedEvent.patient.id === loggedInUser?.id ||
    selectedEvent.doctor!.id === loggedInUser?.id
  );
}

export function checkCanSeeAppointment(appointment: Appointment) {
  const loggedInUser = useUserStore().getLoggedInUser;

  if (!appointment.patient?.id) {
    return true;
  } else if (
    loggedInUser !== null &&
    (appointment.patient?.id === loggedInUser.id ||
      appointment.doctor?.id === loggedInUser.id)
  ) {
    return true;
  }
  return false;
}

export function calculateEndTime(start: string, duration: number) {
  if (!start || !duration) return "";
  const [hours, minutes] = start.split(":").map(Number);
  const startDate = new Date();
  startDate.setHours(hours, minutes, 0, 0);
  const endDate = new Date(startDate.getTime() + duration * 60 * 1000);
  return endDate.toTimeString().slice(0, 5);
}

export function appointmentHasPatient(appointment: Appointment) {
  if (appointment.patient?.id) {
    return true;
  } else {
    return false;
  }
}
