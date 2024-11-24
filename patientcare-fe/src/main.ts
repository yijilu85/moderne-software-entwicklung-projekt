import "./assets/main.css";

import { createApp } from "vue";
import { createPinia } from "pinia";

import App from "./App.vue";
import router from "./router";
import VueCal from "vue-cal";
import "vue-cal/dist/vuecal.css";

const app = createApp(App);

app.component("VueCal", VueCal);

app.use(createPinia());
app.use(router);

app.mount("#app");
