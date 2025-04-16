export default function useUrl() {
  const serializeParams = (params?: Object | string): URLSearchParams | string => {
    if (typeof params === 'string') return params

    const serializedParams = new URLSearchParams()

    if (typeof params === 'object') {
      Object.entries(params).forEach(([property, value]) => {
        if (value !== null && value !== undefined && typeof value !== 'object') {
          serializedParams.append(property, value)
        }
      })
    }

    return serializedParams
  }

  const deserializeParams = () => {}

  const getParamValue = (param: string): string | undefined => {
    const urlParams = new URLSearchParams(window.location.search)
    if (urlParams.has(param)) return urlParams.get(param) ?? undefined
    return undefined
  }

  const replaceUrl = (baseUrl: string, params?: Object) => {
    history.replaceState({}, '', baseUrl + '?' + serializeParams(params))
  }

  const navigateTo = (baseUrl: string, params?: string | Object) => {
    window.location.href = baseUrl + '?' + serializeParams(params)
  }

  return {
    navigateTo,
    replaceUrl,
    serializeParams,
    deserializeParams,
    getParamValue,
  }
}
