import objectMapper from './objectMapper'

export type ReactiveForm<Type extends string> = {
  [key in Type]: {
    value: string
    error: string
  }
}

export function mapReactiveFormToQuery<Type extends string>(form: ReactiveForm<Type>) {
  return objectMapper(form, (key, value) => [key, value.value])
}
