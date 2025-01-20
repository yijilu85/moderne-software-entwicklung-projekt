import {defineStore} from "pinia";
import type {User} from "@/types/types";
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
});
