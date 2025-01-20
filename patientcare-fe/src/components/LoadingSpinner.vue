<script lang="ts" setup></script>
<template>
  <div class="overlay"></div>
  <div class="loading-container">
    <div class="loader"></div>
    <h3>Loading data</h3>
  </div>
</template>

<style scoped>
.overlay {
  position: fixed;
  top: 0;
  left: 0;
  width: 100vw;
  height: 100vh;
  background: rgba(255, 255, 255, 0.5);
  backdrop-filter: blur(5px);
  z-index: 1000;
}

.loading-container {
  width: 30%;
  height: 30%;
  display: flex;
  flex-direction: column;
  gap: 1em;
  justify-content: center;
  align-items: center;
  position: fixed;
  top: 35%;
  left: 35%;
  border: 1px solid var(--primary-color);
  border-radius: var(--border-radius);
  z-index: 1001;
}

.loader {
  width: 100px;
  padding: 8px;
  aspect-ratio: 1;
  border-radius: 50%;
  background: var(--primary-color);
  --_m: conic-gradient(#0000 10%, #000), linear-gradient(#000 0 0) content-box;
  -webkit-mask: var(--_m);
  mask: var(--_m);
  -webkit-mask-composite: source-out;
  mask-composite: subtract;
  animation: l3 1s infinite linear;
}

@keyframes l3 {
  to {
    transform: rotate(1turn);
  }
}
</style>
