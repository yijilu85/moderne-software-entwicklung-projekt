import { createRouter, createWebHistory } from 'vue-router';

import { useAuthStore } from '@/stores';
import { HomeView, LoginView } from '@/views';
import DoctorView from "@/views/DoctorView.vue";
import DoctorsView from "@/views/DoctorsView.vue";
import AppointmentView from "@/views/AppointmentView.vue";

export const router = createRouter({
    history: createWebHistory(import.meta.env.BASE_URL),
    linkActiveClass: 'active',
    routes: [
        { path: '/login', component: LoginView },
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
    ]
});

router.beforeEach(async (to) => {
    // redirect to login page if not logged in and trying to access a restricted page
    const publicPages = ['/login'];
    const authRequired = !publicPages.includes(to.path);
    const auth = useAuthStore();

    if (authRequired && !auth.user) {
        auth.returnUrl = to.fullPath;
        return '/login';
    }
});