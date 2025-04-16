<script setup lang="ts">
const props = defineProps<{
  header: string
  subheader: string
}>()
</script>

<template>
  <div class="ModalForm">
    <div class="ModalForm-top">
      <h3 class="ModalForm-header">{{ props.header }}</h3>
      <p class="ModalForm-subheader">{{ props.subheader }}</p>
    </div>
    <div class="ModalForm-content">
      <slot />
    </div>
  </div>
</template>

<style lang="postcss">
.ModalForm {
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
