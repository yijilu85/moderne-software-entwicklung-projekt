import axios from "./axiosConfig";
import type { User } from "@/types/types";

export const authenticate = async (payload: any) => {
  const response = await axios.post(`/users/login`, payload);
  return response.data;
};
