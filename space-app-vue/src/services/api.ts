import axios from 'axios'
import { useCookies } from 'vue3-cookies'
import authClient from './auth'

const { cookies } = useCookies()

const apiClient = axios.create({
  baseURL: import.meta.env.VITE_API_URL,
  timeout: 5000,
  //withCredentials: true,
  withXSRFToken: true,
  headers: {
    'Content-Type': 'application/json',
  },
})

apiClient.interceptors.request.use((config) => {
  const accessToken = cookies.get('accessToken')
  if (accessToken !== 'undefined')
    config.headers.Authorization = `Bearer ${cookies.get('accessToken')}`
  return config
})

apiClient.interceptors.response.use(
  (response) => {
    return response
  },
  async (error) => {
    const status = error.response ? error.response.status : null

    if (status === 401) {
      try {
        await authClient.post('/auth/refresh-token', {
          refreshToken: cookies.get('refreshToken'),
        })

        if (error.config.headers && error.config.headers['x-no-retry']) {
          return Promise.reject(error)
        }

        error.config.headers['x-no-retry'] = 'true'
        return apiClient(error.config)
      } catch (refreshError) {
        cookies.remove('refreshToken')
        cookies.remove('accessToken')
      }
    }
    return Promise.reject(error)
  },
)

export default apiClient
