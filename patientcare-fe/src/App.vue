<script setup lang="ts">
import { RouterLink, RouterView } from "vue-router";

import { useAuthStore } from '@/stores';

const authStore = useAuthStore();
</script>

<template>
  <header>
    <div class="wrapper">
      <nav>

      </nav>
    </div>
  </header>
  <div class="app-container bg-dark">
    <nav v-show="authStore.user" class="navbar navbar-expand navbar-dark bg-dark">
      <div class="navbar-nav">
        <RouterLink to="/" class="nav-item nav-link">Home</RouterLink>
        <RouterLink to="/" class="nav-item nav-link">Dashboard</RouterLink>
        <RouterLink to="/doctors" class="nav-item nav-link">Ärztesuche</RouterLink>
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
