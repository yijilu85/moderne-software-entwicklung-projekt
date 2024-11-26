import axios from "./axiosConfig";
import type { Doctor } from "@/types/types";

export const getDoctor = async (userId: number) => {
  const response = await axios.get(`/doctors/${userId}`);
  return response.data as Doctor;
};

export const getAllDoctors = async (userId: number) => {
  const response = await axios.get(`/doctors`);
  return response.data as Doctor[];
};
