import { defineStore } from "pinia";
import type { Patient, Doctor, User } from "@/types/types";

export const useUserStore = defineStore("user", {
  state: () => ({
    loggedInUser: null as User | null,
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
  },
});
