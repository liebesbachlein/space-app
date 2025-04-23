import { ref } from 'vue'
import type { AxiosInstance } from 'axios'

export interface UserFetchRequest {
  client: AxiosInstance
  path?: string
  method?: 'GET' | 'POST' | 'PUT' | 'DELETE'
  payload?: Object
  params?: Object
}

interface OverrideUserFetchRequest {
  path?: string
  method?: 'GET' | 'POST' | 'PUT' | 'DELETE'
  payload?: Object
  params?: Object
}

export interface ApiError {
  message: string
  status: number
  details: string
}

export function useFetch<T>(request: UserFetchRequest) {
  const loading = ref<boolean | null>(null)
  const response = ref<T | null>(null)
  const error = ref<ApiError | null>(null)

  const fetch = async (localRequest: OverrideUserFetchRequest) => {
    loading.value = true
    try {
      const apiResponse = await request.client({
        url: localRequest.path ?? request.path ?? '/',
        method: localRequest.method ?? request.method ?? 'GET',
        params: localRequest.params ?? request.params ?? {},
        data: localRequest.payload ?? request.payload ?? {},
      })
      response.value = apiResponse.data
      error.value = null
      return Promise.resolve<T>(response.value)
    } catch (resError: any) {
      response.value = null
      if (resError.response) {
        error.value = {
          status: resError.response.data.status ?? resError.status,
          message: resError.response.data.message ?? resError.message,
          details: resError.response.data.details ?? '',
        }
      } else if (resError.request) {
        error.value = {
          status: resError.request.data.status ?? resError.status,
          message: resError.request.data.message ?? resError.message,
          details: '',
        }
      } else {
        error.value = {
          status: resError.status,
          message: resError.message,
          details: '',
        }
      }

      return Promise.reject<ApiError>(error.value)
    } finally {
      loading.value = false
    }
  }

  return { fetch, response, error, loading }
}
