import "./assets/main.css";

import { createApp } from "vue";
import { createPinia } from "pinia";

import App from "./App.vue";
//import router from "./router";
import VueCal from "vue-cal";
import "vue-cal/dist/vuecal.css";
import "vuetify/styles";
import { createVuetify } from "vuetify";
import * as components from "vuetify/components";
import * as directives from "vuetify/directives";
import { useUserStore } from "@/stores/userStore";

import { router } from './helpers';
import { fakeBackend } from './helpers';
fakeBackend();

const app = createApp(App);
const vuetify = createVuetify({
  components,
  directives,
});

app.component("VueCal", VueCal);

app.use(createPinia());

app.use(router);
app.use(vuetify);
useUserStore().fakeLogIn("patient", 21);
app.mount("#app");
