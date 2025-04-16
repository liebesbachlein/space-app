<script setup lang="ts">
import ModalForm from '@/components/ModalForm.vue'
import DashboardLayoutContent from '@/layouts/DashboardLayoutContent.vue'
import { reactive } from 'vue'

const account = reactive<{
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
    input: 'ai@gmail.com',
    error: '',
  },
  password: {
    input: 'password',
    error: '',
  },
  name: {
    input: 'Jello',
    error: '',
  },
})

function validateName() {
  const RegEx = /^[a-zA-Z]{1,64}$/
  account.name.error = RegEx.test(account.name.input)
    ? ''
    : 'Required field, enter valid name (latin only, 1-64 length)'
  return account.name.error.length == 0
}

function validateEmail() {
  const RegEx =
    /^(([^<>()[\]\.,;:\s@\"]+(\.[^<>()[\]\.,;:\s@\"]+)*)|(\".+\"))@(([^<>()[\]\.,;:\s@\"]+\.)+[^<>()[\]\.,;:\s@\"]{2,})$/i
  account.email.error = RegEx.test(account.email.input) ? '' : 'Required field, enter valid email'
  return account.email.error.length == 0
}

function validatePassword() {
  const RegEx = /^[a-zA-Z0-9!@#$%^&*]{5,16}$/
  account.password.error = RegEx.test(account.password.input)
    ? ''
    : 'Required field, enter valid password (latin/numbers/special symbols only, 5-16 length)'
  return account.password.error.length == 0
}

function handleAccount() {
  if (validateName() && validateEmail() && validatePassword()) {
  }
}
</script>

<template>
  <DashboardLayoutContent>
    <template #main>
      <h1 class="Dashboard-headline">Account</h1>
      <ModalForm header="Account info" subheader="Alter your details">
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
                v-model="account.name.input"
              />
              <label v-if="account.name.error" class="error-label">{{ account.name.error }}</label>
            </div>
            <div class="input-wrap">
              <label for="email" class="label">Email</label>
              <input
                @blur="validateEmail"
                type="email"
                name="email"
                id="email"
                class="input"
                v-model="account.email.input"
              />
              <label v-if="account.email.error" class="error-label">{{
                account.email.error
              }}</label>
            </div>
            <div class="input-wrap">
              <label for="password" class="label">Password</label>
              <input
                @blur="validatePassword"
                type="text"
                name="password"
                id="password"
                class="input"
                v-model="account.password.input"
              />
              <label v-if="account.password.error" class="error-label">{{
                account.password.error
              }}</label>
            </div>
          </div>
          <div class="form-buttons">
            <button @click="handleAccount" class="button1 button-animation">Save changes</button>
          </div>
        </div>
      </ModalForm>
    </template>
  </DashboardLayoutContent>
</template>
