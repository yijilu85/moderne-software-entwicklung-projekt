<script setup lang="ts">
import { RouterLink, RouterView } from "vue-router";
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

  <RouterView />
</template>

<style scoped></style>
