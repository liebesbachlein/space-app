<script setup lang="ts">
import ModalWindow from '@/components/ModalWindow.vue'
</script>

<template>
  <div class="page-layout page-background">
    <ModalWindow
      ref="modal"
      header="Internal Server Error"
      subheader="A error occurred on the server"
    >
      <div class="ModalWindow-buttons">
        <RouterLink to="/" class="button1 button-animation">Go to Main</RouterLink>
      </div>
    </ModalWindow>
  </div>
</template>

<style lang="postcss"></style>
