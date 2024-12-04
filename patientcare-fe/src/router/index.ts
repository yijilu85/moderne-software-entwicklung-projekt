import { createRouter, createWebHistory } from "vue-router";
import HomeView from "../views/HomeView.vue";
import DoctorView from "@/views/DoctorView.vue";
import DoctorsView from "@/views/DoctorsView.vue";
import AppointmentView from "@/views/AppointmentView.vue";

const router = createRouter({
  history: createWebHistory(import.meta.env.BASE_URL),
  routes: [
    {
      path: "/",
      name: "home",
      component: HomeView,
    },
    {
      path: "/about",
      name: "about",
      component: () => import("../views/AboutView.vue"),
    },
    {
      path: "/doctor/:id",
      name: "doctor",
      component: DoctorView,
    },
    {
      path: "/doctors/",
      name: "doctors",
      component: DoctorsView,
    },
    {
      path: "/appointment/:id",
      name: "appointments",
      component: AppointmentView,
    },
  ],
});

export default router;
