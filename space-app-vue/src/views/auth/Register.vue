<script setup lang="ts">
import ModalWindow from '@/components/ModalWindow.vue'
import axios from 'axios'
import { reactive } from 'vue'

const register = reactive<{
  name: {
    input: string
    error: string
  }
  email: {
    input: string
    error: string
  }
  password: {
    input: string
    error: string
  }
}>({
  email: {
    input: '',
    error: '',
  },
  password: {
    input: '',
    error: '',
  },
  name: {
    input: '',
    error: '',
  },
})

function validateName() {
  const RegEx = /^[a-zA-Z]{1,64}$/
  register.name.error = RegEx.test(register.name.input)
    ? ''
    : 'Required field, enter valid name (latin only, 1-64 length)'
  return register.name.error.length == 0
}

function validateEmail() {
  const RegEx =
    /^(([^<>()[\]\.,;:\s@\"]+(\.[^<>()[\]\.,;:\s@\"]+)*)|(\".+\"))@(([^<>()[\]\.,;:\s@\"]+\.)+[^<>()[\]\.,;:\s@\"]{2,})$/i
  register.email.error = RegEx.test(register.email.input) ? '' : 'Required field, enter valid email'
  return register.email.error.length == 0
}

function validatePassword() {
  const RegEx = /^[a-zA-Z0-9!@#$%^&*]{5,16}$/
  register.password.error = RegEx.test(register.password.input)
    ? ''
    : 'Required field, enter valid password (latin/numbers/special symbols only, 5-16 length)'
  return register.password.error.length == 0
}

function handleRegister() {
  if (validateName() && validateEmail() && validatePassword()) {
  }
}
</script>

<template>
  <div class="page-layout page-background">
    <ModalWindow header="register" subheader="Some important register info here so read please">
      <div class="form">
        <div class="form-inputs">
          <div class="input-wrap">
            <label for="name" class="label">Name</label>
            <input
              @blur="validateName"
              type="text"
              name="name"
              id="name"
              class="input"
              v-model="register.name.input"
            />
            <label v-if="register.name.error" class="error-label">{{ register.name.error }}</label>
          </div>
          <div class="input-wrap">
            <label for="email" class="label">Email</label>
            <input
              @blur="validateEmail"
              type="email"
              name="email"
              id="email"
              class="input"
              v-model="register.email.input"
            />
            <label v-if="register.email.error" class="error-label">{{
              register.email.error
            }}</label>
          </div>
          <div class="input-wrap">
            <label for="password" class="label">Password</label>
            <input
              @blur="validatePassword"
              type="password"
              name="password"
              id="password"
              class="input"
              v-model="register.password.input"
            />
            <label v-if="register.password.error" class="error-label">{{
              register.password.error
            }}</label>
          </div>
        </div>
        <div class="form-buttons">
          <RouterLink to="/register" v-slot="{ href }" custom>
            <a :href="href" class="button1 button-animation button1--outlined">
              <span>Create an account</span>
            </a>
          </RouterLink>
          <RouterLink to="/register" v-slot="{ href }" custom>
            <a :href="href" class="button1 button-animation">
              <span>Log in</span>
            </a>
          </RouterLink>
        </div>
      </div>
    </ModalWindow>
  </div>
</template>

<style lang="postcss"></style>
