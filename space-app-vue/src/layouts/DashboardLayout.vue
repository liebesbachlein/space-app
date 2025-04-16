<script setup lang="ts">
import DashboardLayoutContent from './DashboardLayoutContent.vue'
</script>

<template>
  <div class="page-layout page-background">
    <div class="Dashboard">
      <aside class="Dashboard-Menu">
        <slot name="menu" />
      </aside>
      <slot name="content" />
    </div>
  </div>
</template>

<style lang="postcss">
.Dashboard {
  position: relative;
  display: grid;
  grid-template-columns: 238px 1fr;
  height: 100%;
  overflow: auto;

  &-Menu {
    top: 0;
    position: sticky;
    width: 100%;
    height: 100vh;
    min-height: fit-content;
    border-right: 1px solid var(--stroke);
    border-left: 1px solid var(--stroke);
    background-color: white;
  }

  &-Main {
    width: 100%;
    padding: 16px;
    display: flex;
    flex-direction: column;
    gap: 32px;
  }

  &-headline {
    font: 800 48px/48px var(--font-family);
  }
}
</style>
