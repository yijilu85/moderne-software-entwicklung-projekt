import axios from "./axiosConfig";
import type { Patient } from "@/types/types";

export const getPatient = async (userId: number) => {
  const response = await axios.get(`/patients/${userId}`);
  return response.data as Patient;
};
