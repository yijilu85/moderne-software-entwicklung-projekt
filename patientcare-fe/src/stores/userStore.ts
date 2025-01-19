import { defineStore } from "pinia";
import type { User } from "@/types/types";
import { getPatient } from "@/api/patientController";
import { getDoctor } from "@/api/doctorController";
import {useAuthStore} from "@/stores/auth.store";

export const useUserStore = defineStore("user", {
  state: () => ({
    loggedInUser: useAuthStore().user as User | null,
  }),

  getters: {
    getLoggedInUser: (state) => {
      return state.loggedInUser;
    },
  },
  actions: {
    mutate(mutation: User) {
      this.loggedInUser = mutation;
    },
    async fakeLogIn(userType: "patient" | "doctor", id: number) {
      let fakedUser;
      if (userType === "patient") {
        fakedUser = await getPatient(id);
      } else if (userType === "doctor") {
        fakedUser = await getDoctor(id);
      }
      this.mutate(fakedUser!);
      console.log("fakedUser:", useUserStore().getLoggedInUser);
    },
  },
});
