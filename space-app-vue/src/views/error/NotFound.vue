<script setup lang="ts">
import ModalWindow from '@/components/ModalWindow.vue'
</script>

<template>
  <div class="page-layout page-background">
    <ModalWindow
      ref="modal"
      header="Not Found"
      subheader="Page or a resource you've looking for cannot be found"
    >
      <div class="ModalWindow-buttons">
        <RouterLink to="/" class="button1 button-animation">Go to Main</RouterLink>
      </div>
    </ModalWindow>
  </div>
</template>

<style lang="postcss"></style>
