import date from 'date-and-time'

export default function useDateTime() {
  function parseHour(hour: string) {
    const hourNumber = date.parse(hour, 'HH:mm:ss [GMT]Z').getHours()
    return (hourNumber < 9 ? '0' : '') + hourNumber + ':00'
  }

  function parseDate(dateStr: string) {
    const monthMap = new Map<number, string>([
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

    const format1 = 'YYYY-MM-DD'

    const day = date.parse(dateStr, format1).getDate()
    const month = monthMap.get(date.parse(dateStr, format1).getMonth() + 1) ?? 'Error'
    const year = date.parse(dateStr, format1).getFullYear()

    return day + ' ' + month + (year == 2025 ? '' : ', ' + year)
  }

  return { parseDate, parseHour }
}
