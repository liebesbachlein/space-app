import { defineStore } from 'pinia'
import { ref } from 'vue'
import type User from '@/types/User.ts'
import authClient from '@/services/auth'
import { useFetch } from '@/composables/useFetch'
import { useCookies } from 'vue3-cookies'
import { jwtDecode } from 'jwt-decode'
import router from '@/router'

const { cookies } = useCookies()

export interface AuthTokens {
  accessToken: string
  refreshToken: string
}

export const useAuthStore = defineStore('auth', () => {
  const user = ref<User | null>(null)
  const token = ref<string | null>(null)

  function auth() {
    const newToken = cookies.get('accessToken')
    if (!newToken || newToken === 'undefined') return false
    decodeJwt(newToken)
    return user.value !== null
  }

  function useLogin() {
    const { fetch, response, error, loading } = useFetch<AuthTokens>({
      client: authClient,
      path: '/auth/login',
      method: 'POST',
    })

    const login = (email: string, password: string, onError: Function, onSuccess?: Function) => {
      fetch({
        payload: {
          email: email,
          password: password,
        },
      })
        .then(() => {
          if (response.value) decodeJwt(response.value.accessToken)
          onSuccess ? onSuccess() : router.push(isAdmin() ? '/admin' : '/user')
        })
        .catch(() => {
          onError()
        })
    }

    return { login, error, loading }
  }

  function useRegister() {
    const { fetch, response, error, loading } = useFetch<AuthTokens>({
      client: authClient,
      path: '/auth/register',
      method: 'POST',
    })

    const register = (
      name: string,
      email: string,
      password: string,
      onError: Function,
      onSuccess?: Function,
    ) => {
      fetch({
        payload: {
          name: name,
          email: email,
          password: password,
        },
      }).then(() => {
        if (response.value) {
          decodeJwt(response.value.accessToken)
          onSuccess ? onSuccess() : router.push('/user')
        } else if (error.value) {
          onError()
        }
      })
    }

    return { register, error, loading }
  }

  function useLogout() {
    const { fetch, response, error, loading } = useFetch<{
      refreshToken: string
    }>({
      client: authClient,
      path: '/auth/logout',
      method: 'POST',
    })

    const logout = (callback: () => void) => {
      fetch({
        payload: {
          refreshToken: cookies.get('refreshToken'),
        },
      }).then(() => {
        callback()
        cookies.remove('accessToken')
        cookies.remove('refreshToken')
        token.value = null
        user.value = null
      })
    }

    return { logout, error, loading }
  }

  function decodeJwt(jwt: string) {
    token.value = jwt
    const decoded = jwtDecode<{ sub: string; id: number; name: string; admin: boolean }>(jwt)
    user.value = {
      id: decoded.id,
      email: decoded.sub,
      name: decoded.name,
      isAdmin: decoded.admin,
    }
  }

  function isLoggedIn() {
    return user.value !== null
  }

  function isAdmin() {
    return user.value !== null && user.value.isAdmin
  }

  return { user, auth, useLogin, useRegister, useLogout, isLoggedIn, isAdmin }
})
