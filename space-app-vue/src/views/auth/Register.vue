<script setup lang="ts">
import ModalWindow from '@/components/ModalWindow.vue'
import router from '@/router'
import { useAuthStore } from '@/stores/auth'
import type { ReactiveForm } from '@/util/reactiveForm'
import { reactive, useTemplateRef } from 'vue'

type RegisterForm = 'email' | 'password' | 'name'

const registerForm: ReactiveForm<RegisterForm> = reactive({
  email: {
    value: '',
    error: '',
  },
  password: {
    value: '',
    error: '',
  },
  name: {
    value: '',
    error: '',
  },
})

function validateName() {
  const RegEx = /^[a-zA-Z]{1,64}$/
  registerForm.name.error = RegEx.test(registerForm.name.value)
    ? ''
    : 'Required field, enter valid name (latin only, 1-64 length)'
  return registerForm.name.error.length == 0
}

function validateEmail() {
  const RegEx =
    /^(([^<>()[\]\.,;:\s@\"]+(\.[^<>()[\]\.,;:\s@\"]+)*)|(\".+\"))@(([^<>()[\]\.,;:\s@\"]+\.)+[^<>()[\]\.,;:\s@\"]{2,})$/i
  registerForm.email.error = RegEx.test(registerForm.email.value)
    ? ''
    : 'Required field, enter valid email'
  return registerForm.email.error.length == 0
}

function validatePassword() {
  const RegEx = /^[a-zA-Z0-9!@#$%^&*]{5,16}$/
  registerForm.password.error = RegEx.test(registerForm.password.value)
    ? ''
    : 'Required field, enter valid password (latin/numbers/special symbols only, 5-16 length)'
  return registerForm.password.error.length == 0
}

const modal = useTemplateRef<{ triggerAnimation: () => void }>('modal')

const { useRegister } = useAuthStore()
const { register, error, loading } = useRegister()

function handleRegister() {
  if (validateName() && validateEmail() && validatePassword()) {
    register(registerForm.name.value, registerForm.email.value, registerForm.password.value, () => {
      modal.value?.triggerAnimation()
    })
  }
}
</script>

<template>
  <div class="page-layout page-background">
    <ModalWindow
      ref="modal"
      header="Cretate an account"
      subheader="Some important register info here so read please"
    >
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
              v-model="registerForm.name.value"
            />
            <label v-if="registerForm.name.error" class="error-label">{{
              registerForm.name.error
            }}</label>
          </div>
          <div class="input-wrap">
            <label for="email" class="label">Email</label>
            <input
              @blur="validateEmail"
              type="email"
              name="email"
              id="email"
              class="input"
              v-model="registerForm.email.value"
            />
            <label v-if="registerForm.email.error" class="error-label">{{
              registerForm.email.error
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
              v-model="registerForm.password.value"
            />
            <label v-if="registerForm.password.error" class="error-label">{{
              registerForm.password.error
            }}</label>
          </div>
        </div>
        <div class="form-buttons">
          <RouterLink to="/login" class="button1 button-animation button1--outlined"
            >Have an account?</RouterLink
          >
          <button
            @click="handleRegister"
            class="button1 button-animation"
            :class="{ 'button--loading': loading }"
          >
            Create an account
          </button>
        </div>
        <label class="error-label" v-if="error">{{ error.message }}</label>
      </div>
    </ModalWindow>
  </div>
</template>

<style lang="postcss"></style>
