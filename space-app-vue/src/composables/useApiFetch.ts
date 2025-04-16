import axios from 'axios'
import { ref } from 'vue'
import type Error from '@/types/Error'

export default function useApiFetch() {
  const useGet = <T>() => {
    const loading = ref<boolean | null>(null)
    const responseData = ref<T | null>(null)
    const status = ref<number | null>(null)
    const error = ref<Error | null>(null)

    const get = async (url: string, params?: Object) => {
      loading.value = true

      try {
        const response = await axios.get<T>(url, {
          params: params ?? {},
        })
        responseData.value = response.data
        status.value = response.status
      } catch (errResponse: any) {
        error.value = {
          message: errResponse.message,
          status: errResponse.response.status,
        }
        console.error(errResponse)
      } finally {
        loading.value = false
      }
    }

    return { get, responseData, status, error, loading }
  }

  const usePost = <T>() => {
    const loading = ref<boolean | null>(null)
    const responseData = ref<T | null>(null)
    const status = ref<number | null>(null)
    const error = ref<Error | null>(null)

    const get = async (url: string, params?: Object) => {
      loading.value = true

      try {
        const response = await axios.get<T>(url, {
          params: params ?? {},
        })
        responseData.value = response.data
        status.value = response.status
      } catch (errResponse: any) {
        error.value = {
          message: errResponse.message,
          status: errResponse.response.status,
        }
        console.error(errResponse)
      } finally {
        loading.value = false
      }
    }

    return { get, responseData, status, error, loading }
  }

  return { useGet }
}
