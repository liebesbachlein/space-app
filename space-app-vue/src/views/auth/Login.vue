<script setup lang="ts">
import ModalWindow from '@/components/ModalWindow.vue'
import router from '@/router'
import { useAuthStore } from '@/stores/auth'
import type { ReactiveForm } from '@/util/reactiveForm'
import { reactive, useTemplateRef } from 'vue'

type LoginForm = 'email' | 'password'

const loginForm: ReactiveForm<LoginForm> = reactive({
  email: {
    value: '',
    error: '',
  },
  password: {
    value: '',
    error: '',
  },
})

function validateEmail() {
  const RegEx =
    /^(([^<>()[\]\.,;:\s@\"]+(\.[^<>()[\]\.,;:\s@\"]+)*)|(\".+\"))@(([^<>()[\]\.,;:\s@\"]+\.)+[^<>()[\]\.,;:\s@\"]{2,})$/i
  loginForm.email.error = RegEx.test(loginForm.email.value)
    ? ''
    : 'Required field, enter valid email'
  return loginForm.email.error.length == 0
}

function validatePassword() {
  const RegEx = /^[a-zA-Z0-9!@#$%^&*]{5,16}$/
  loginForm.password.error = RegEx.test(loginForm.password.value)
    ? ''
    : 'Required field, enter valid password (latin/numbers/special symbols only, 5-16 length)'
  return loginForm.password.error.length == 0
}

const modal = useTemplateRef<{ triggerAnimation: () => void }>('modal')

const { useLogin } = useAuthStore()
const { login, error, loading } = useLogin()

function handleLogin() {
  if (validateEmail() && validatePassword()) {
    login(loginForm.email.value, loginForm.password.value, () => {
      modal.value?.triggerAnimation()
    })
  }
}
</script>

<template>
  <div class="page-layout page-background">
    <ModalWindow
      ref="modal"
      header="Login"
      subheader="Some important login info here so read please"
    >
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
              v-model="loginForm.email.value"
            />
            <label v-if="loginForm.email.error" class="error-label">{{
              loginForm.email.error
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
              v-model="loginForm.password.value"
            />
            <label v-if="loginForm.password.error" class="error-label">{{
              loginForm.password.error
            }}</label>
          </div>
        </div>
        <div class="form-buttons">
          <RouterLink to="/register" class="button1 button-animation button1--outlined"
            >Create an account</RouterLink
          >
          <button
            @click="handleLogin"
            class="button1 button-animation"
            :class="{ 'button--loading': loading }"
          >
            Log in
          </button>
        </div>
        <label class="error-label" v-if="error">{{ error.message }}</label>
      </div>
    </ModalWindow>
  </div>
</template>

<style lang="postcss"></style>
