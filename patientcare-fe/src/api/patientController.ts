import axios from "./axiosConfig";
import type { Patient } from "@/types/types";

export const getPatient = async (userId: number | undefined) => {
  const response = await axios.get(`/patients/${userId}`);
  return response.data as Patient;
};

export const getAllPatients = async (userId: number) => {
  const response = await axios.get(`/patients`);
  return response.data as Patient[];
};
