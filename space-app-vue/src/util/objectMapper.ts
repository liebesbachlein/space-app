export default function objectMapper(
  object: Object,
  mappingStrategy: (key: string, value: any) => [key: string, value: any],
) {
  return Object.fromEntries(
    Object.entries(object).map(([key, value]) => mappingStrategy(key, value)),
  )
}
