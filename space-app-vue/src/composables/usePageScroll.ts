export function usePageScroll() {
  let scroll = 0
  const app = document.getElementById('app')
  const main = document.getElementById('app')?.querySelector('main')
  const footer = document.getElementById('app')?.querySelector('footer')

  const disablePageScroll = () => {
    document.body.style.position = 'fixed'
  }
  const enablePageScroll = () => {
    document.body.style.position = 'static'
  }

  return {
    disablePageScroll,
    enablePageScroll,
  }
}
