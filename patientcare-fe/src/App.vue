<script setup lang="ts">
import { RouterLink, RouterView } from "vue-router";
import HelloWorld from "./components/HelloWorld.vue";
import PatientList from "./components/PatientList.vue";

import { useAuthStore } from '@/stores';

const authStore = useAuthStore();
</script>

<template>
  <header>
    <div class="wrapper">
      <nav>
        <RouterLink to="/">Dashboard</RouterLink>
        <RouterLink to="/doctors">Ärztesuche</RouterLink>
        <RouterLink to="#">Meine Patientenakte</RouterLink>
        <RouterLink to="#">Meine Praxisseite</RouterLink>
      </nav>
    </div>
  </header>
  <div class="app-container bg-light">
    <nav v-show="authStore.user" class="navbar navbar-expand navbar-dark bg-dark">
      <div class="navbar-nav">
        <RouterLink to="/" class="nav-item nav-link">Home</RouterLink>
        <a @click="authStore.logout()" class="nav-item nav-link">Logout</a>
      </div>
    </nav>
    <div class="container pt-4 pb-4">
      <RouterView />
    </div>
  </div>
</template>

<style scoped>
@import '@/assets/base.css';
header {
  line-height: 1.5;
  max-height: 100vh;
}

.logo {
  display: block;
  margin: 0 auto 2rem;
}

nav {
  width: 100%;
  font-size: 12px;
  text-align: center;
  margin-top: 2rem;
}

nav a.router-link-exact-active {
  color: var(--color-text);
}

nav a.router-link-exact-active:hover {
  background-color: transparent;
}

nav a {
  display: inline-block;
  padding: 0 1rem;
  border-left: 1px solid var(--color-border);
}

nav a:first-of-type {
  border: 0;
}

@media (min-width: 1024px) {
  header {
    display: flex;
    place-items: center;
    padding-right: calc(var(--section-gap) / 2);
  }

  .logo {
    margin: 0 2rem 0 0;
  }

  header .wrapper {
    display: flex;
    place-items: flex-start;
    flex-wrap: wrap;
  }

  nav {
    text-align: left;
    margin-left: -1rem;
    font-size: 1rem;

    padding: 1rem 0;
    margin-top: 1rem;
  }
}
</style>
