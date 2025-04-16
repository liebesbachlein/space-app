import axios from 'axios'
import { defineStore } from 'pinia'
import { ref } from 'vue'
import router from '@/router'
import apiClient from '@/services/api'
import type User from '@/types/User.ts'
import type Error from '@/types/Error'

export const useAuthStore = defineStore('auth', () => {
  const user = ref<User | null>(null)
  const loading = ref<boolean>(false)

  async function auth(): Promise<Error | null> {
    let error: Error | null = null
    loading.value = true
    try {
      await apiClient.get('/sanctum/csrf-cookie')
      const response = await apiClient.get('/api/user/me')
      user.value = response.data
    } catch (errResponse: any) {
      error = {
        message: errResponse.message,
        status: errResponse.response.status,
      }
      console.log(errResponse)
    } finally {
      loading.value = false
    }
    return error
  }

  async function login(email: string, password: string, callback: () => {}): Promise<Error | null> {
    let error: Error | null = null
    try {
      await apiClient.get('/sanctum/csrf-cookie')
      const response = await apiClient.post('/api/login', {
        email: email,
        password: password,
      })
      user.value = response.data

      callback()
    } catch (errResponse: any) {
      error = {
        message: errResponse.message,
        status: errResponse.response.status,
      }
      console.log(errResponse)
    }
    return error
  }

  async function logout(callback: () => void): Promise<Error | null> {
    let error: Error | null = null
    try {
      //await authClient.get("/sanctum/csrf-cookie");
      await apiClient.post('/api/logout')
      user.value = null

      callback()
      router.push('/')
    } catch (errResponse: any) {
      error = {
        message: errResponse.message,
        status: errResponse.response.status,
      }
      console.log(errResponse)
    }
    return error
  }

  return { user, loading, login, auth, logout }
})
