import axios from "./axiosConfig";
import type { Appointment, Doctor, Patient } from "@/types/types";

export const getAllAppointments = async () => {
  const response = await axios.get(`/appointments`);
  return response.data as Appointment[];
};

export const getAppointment = async (id: number) => {
  const response = await axios.get(`/appointments/${id}`);
  return response.data as Appointment;
};

export const getAllAppointmentsForUser = async (id: number) => {
  const response = await axios.get(`/appointments?userId=${id}`);
  return response.data as Appointment[];
};

export const createAppointmentSlot = async (payload: Appointment) => {
  const response = await axios.post(`/appointments`, payload);
  return response.data as Appointment;
};

export const sendBookingAppointment = async (payload: {
  appointmentId: number;
  patientId: number;
}) => {
  const response = await axios.post(`/appointments/book`, payload);
  return response.data as Appointment;
};
