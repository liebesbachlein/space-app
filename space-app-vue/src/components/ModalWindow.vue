<script setup lang="ts">
const props = defineProps<{
  header: string
  subheader: string
}>()
</script>

<template>
  <div class="page-center-lock ModalWindow-backdrop">
    <div class="ModalWindow">
      <div class="ModalWindow-top">
        <h3 class="ModalWindow-header">{{ props.header }}</h3>
        <p class="ModalWindow-subheader">{{ props.subheader }}</p>
      </div>
      <div class="ModalWindow-content">
        <slot />
      </div>
    </div>
  </div>
</template>

<style lang="postcss">
@keyframes ModalWindow-appearance {
  0% {
    transform: scale(0.95);
  }
  50% {
    transform: scale(1.02);
  }
  75% {
    transform: scale(0.98);
  }
  90% {
    transform: scale(1.01);
  }
  100% {
    transform: scale(1);
  }
}

.ModalWindow {
  width: 400px;
  min-width: fit-content;
  border-radius: var(--radius);
  border: 1px solid var(--stroke);
  padding: 25px;
  background-color: white;
  display: flex;
  flex-direction: column;
  align-items: flex-start;
  gap: 16px;
  animation: 0.5s ease-out 0s 1 ModalWindow-appearance;

  &-backdrop {
    backdrop-filter: blur(5px);
  }

  &-top {
    width: 100%;
  }

  &-header {
    font: 600 18px/28px var(--font-family);
    color: var(--black);
    margin-bottom: 8px;
  }

  &-subheader {
    font: 400 14px/20px var(--font-family);
    color: var(--grey);
  }

  &-content {
    display: flex;
    flex-direction: column;
    align-items: center;
    gap: 8px;
    width: 100%;
  }

  &-buttons {
    display: flex;
    gap: 8px;
    justify-content: flex-end;
    width: 100%;
  }
}
</style>
