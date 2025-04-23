import date from 'date-and-time'

export const monthMap = new Map<number, string>([
  [1, 'January'],
  [2, 'February'],
  [3, 'March'],
  [4, 'April'],
  [5, 'May'],
  [6, 'June'],
  [7, 'July'],
  [8, 'August'],
  [9, 'September'],
  [10, 'October'],
  [11, 'Nobember'],
  [12, 'December'],
])

export const today = new Date()
today.setTime(Date.now())

export const dateFormat = 'YYYY-MM-DD'
export const timeFormat = 'HH:mm:ss'

export function parseHourStrToInt(hourStr: string): number {
  return date.parse(hourStr, timeFormat).getHours()
}

export function parseDateStrToObj(dateStr: string): Date {
  return date.parse(dateStr, dateFormat)
}

export function prettyHourStr(hourStr: string): string {
  const hourNumber = date.parse(hourStr, timeFormat).getHours()
  return (hourNumber < 10 ? '0' : '') + hourNumber + ':00'
}

export function prettyHourInt(hourInt: number): string {
  return (hourInt < 10 ? '0' : '') + hourInt + ':00'
}

export function prettyDateMonthYearFromStr(dateMonthYearStr: string) {
  const day = date.parse(dateMonthYearStr, dateFormat).getDate()
  const month = monthMap.get(date.parse(dateMonthYearStr, dateFormat).getMonth() + 1) ?? 'Error'
  const year = date.parse(dateMonthYearStr, dateFormat).getFullYear()

  return day + ' ' + month + (year == 2025 ? '' : ', ' + year)
}

export function prettyDateMonthYearFromObj(dateMonthYearObj: Date) {
  const day = dateMonthYearObj.getDate()
  const month = monthMap.get(dateMonthYearObj.getMonth() + 1) ?? 'Error'
  const year = dateMonthYearObj.getFullYear()

  return day + ' ' + month + (year == 2025 ? '' : ', ' + year)
}

export function stringifyDateObj(dateObj: Date) {
  return date.format(dateObj, dateFormat)
}

export function getMonthNameFromInt(month: number) {
  return monthMap.get(month) ?? 'Error'
}

export function getNumberOfDaysInMonth(monthIndex: number, yearInt: number) {
  const d = new Date(yearInt, monthIndex + 1, 0)
  return d.getDate()
}

export function getNumberOfOffsetDays(monthIndex: number, yearInt: number) {
  const d = new Date(yearInt, monthIndex, 0)
  return d.getDay()
}
