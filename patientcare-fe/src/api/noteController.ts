import axios from "./axiosConfig";
import type { AppointmentNote } from "@/types/types";

export const createNote = async (note: AppointmentNote) => {
  console.log("payload", note);
  const response = await axios.post(`/notes`, note);
  return response.data as AppointmentNote;
};

export const uploadFile = async (file, appointmentId, doctorId, patientId) => {
  const formData = new FormData();
  formData.append("file", file);
  formData.append("appointmentId", appointmentId);
  formData.append("doctorId", doctorId);
  formData.append("patientId", patientId);

  const response = await axios.post("/notes/upload", formData);

  if (!response.ok) {
    throw new Error("File upload failed");
  }

  return await response.json();
};
