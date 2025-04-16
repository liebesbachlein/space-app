<script setup lang="ts">
import ModalWindow from '@/components/ModalWindow.vue'
import axios from 'axios'
import { reactive } from 'vue'

const login = reactive<{
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
})

function validateEmail() {
  const RegEx =
    /^(([^<>()[\]\.,;:\s@\"]+(\.[^<>()[\]\.,;:\s@\"]+)*)|(\".+\"))@(([^<>()[\]\.,;:\s@\"]+\.)+[^<>()[\]\.,;:\s@\"]{2,})$/i
  login.email.error = RegEx.test(login.email.input) ? '' : 'Required field, enter valid email'
  return login.email.error.length == 0
}

function validatePassword() {
  const RegEx = /^[a-zA-Z0-9!@#$%^&*]{5,16}$/
  login.password.error = RegEx.test(login.password.input)
    ? ''
    : 'Required field, enter valid password (latin/numbers/special symbols only, 5-16 length)'
  return login.password.error.length == 0
}

function handleLogin() {
  if (validateEmail() && validatePassword()) {
  }
}
</script>

<template>
  <div class="page-layout page-background">
    <ModalWindow header="Login" subheader="Some important login info here so read please">
      <div class="form">
        <div class="form-inputs">
          <div class="input-wrap">
            <label for="email" class="label">Email</label>
            <input
              @blur="validateEmail"
              type="email"
              name="email"
              id="email"
              class="input"
              v-model="login.email.input"
            />
            <label v-if="login.email.error" class="error-label">{{ login.email.error }}</label>
          </div>
          <div class="input-wrap">
            <label for="password" class="label">Password</label>
            <input
              @blur="validatePassword"
              type="password"
              name="password"
              id="password"
              class="input"
              v-model="login.password.input"
            />
            <label v-if="login.password.error" class="error-label">{{
              login.password.error
            }}</label>
          </div>
        </div>
        <div class="form-buttons">
          <RouterLink to="/register" v-slot="{ href }" custom>
            <a :href="href" class="button1 button-animation button1--outlined">
              <span>Create an account</span>
            </a>
          </RouterLink>
          <RouterLink to="/login" v-slot="{ href }" custom>
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
