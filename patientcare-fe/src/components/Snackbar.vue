<script setup lang="ts">
import { ref, watch } from "vue";

const props = defineProps({
  show: Boolean,
  message: String,
  color: {
    type: String,
    default: "success",
  },
});

const emit = defineEmits(["update:show"]);

const hideSnackbar = () => {
  emit("update:show", false);
};

watch(
  () => props.show,
  (newVal) => {
    if (newVal) {
      setTimeout(() => emit("update:show", false), 3000);
    }
  }
);
</script>

<template>
  <div v-if="show" :class="`snackbar snackbar--${color}`" @click="hideSnackbar">
    {{ message }}
  </div>
</template>

<style>
.snackbar {
  position: fixed;
  bottom: 20px;
  left: 50%;
  transform: translateX(-50%);
  padding: 10px 20px;
  border-radius: 5px;
  color: white;
  font-weight: bold;
  z-index: 1000;
  transition: opacity 0.3s ease, transform 0.3s ease;
}

.snackbar--success {
  background-color: green;
}

.snackbar--error {
  background-color: red;
}

.snackbar--info {
  background-color: blue;
}
</style>
