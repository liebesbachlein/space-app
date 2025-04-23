<script setup lang="ts">
import ModalWindow from '@/components/ModalWindow.vue'
</script>

<template>
  <div class="page-layout page-background">
    <ModalWindow
      ref="modal"
      header="Unauthorized Access"
      subheader="You are not authorized to access this page"
    >
      <div class="ModalWindow-buttons">
        <RouterLink to="/" class="button1 button-animation">Go to Main</RouterLink>
      </div>
    </ModalWindow>
  </div>
</template>

<style lang="postcss"></style>
