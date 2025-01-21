<script setup>
import {Field, Form} from 'vee-validate';
import * as Yup from 'yup';

import {useAuthStore} from '@/stores';

const schema = Yup.object().shape({
  username: Yup.string().required('Username is required'),
  password: Yup.string().required('Password is required')
});

function onSubmit(values, {setErrors}) {
  const authStore = useAuthStore();
  const {username, password} = values;

  return authStore.login(username, password)
      .catch(error => setErrors({apiError: error}));
}
</script>

<template>
  <link crossorigin="anonymous" href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/css/bootstrap.min.css"
        integrity="sha384-QWTKZyjpPEjISv5WaRU9OFeRpok6YctnYmDr5pNlyT2bRjXh0JMhjY6hW+ALEwIH"
        rel="stylesheet"/>
  <div>
    <h2>Login</h2>
    <Form v-slot="{ errors, isSubmitting }" :validation-schema="schema" @submit="onSubmit">
      <div class="form-group">
        <label>Username</label>
        <Field :class="{ 'is-invalid': errors.username }" class="form-control" name="username" type="text"/>
        <div class="invalid-feedback">{{ errors.username }}</div>
      </div>
      <div class="form-group">
        <label>Password</label>
        <Field :class="{ 'is-invalid': errors.password }" class="form-control" name="password" type="password"/>
        <div class="invalid-feedback">{{ errors.password }}</div>
      </div>
      <div class="form-group">
        <button :disabled="isSubmitting" class="btn btn-primary">
          <span v-show="isSubmitting" class="spinner-border spinner-border-sm mr-1"></span>
          Login
        </button>
      </div>
      <div v-if="errors.apiError" class="alert alert-danger mt-3 mb-0">{{ errors.apiError }}</div>
    </Form>
  </div>
</template>