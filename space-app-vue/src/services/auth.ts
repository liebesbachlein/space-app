import axios from 'axios'
import { useCookies } from 'vue3-cookies'

const { cookies } = useCookies()

const authClient = axios.create({
  baseURL: import.meta.env.VITE_API_URL,
  timeout: 5000,
  withXSRFToken: true,
  headers: {
    'Content-Type': 'application/json',
  },
})

authClient.interceptors.response.use(
  (response) => {
    cookies.set('accessToken', response.data.accessToken, '10m', '/', '', true, 'None')
    cookies.set('refreshToken', response.data.refreshToken, '10m', '/', '', true, 'None')
    return response
  },
  (error) => {
    return Promise.reject(error)
  },
)

export default authClient
