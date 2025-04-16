<script setup lang="ts">
import CircularLoader from './CircularLoader.vue'
import { usePageScroll } from '@/composables/usePageScroll'
import { onMounted, onUnmounted } from 'vue'

const { disablePageScroll, enablePageScroll } = usePageScroll()

onMounted(() => disablePageScroll())

onUnmounted(() => enablePageScroll())
</script>

<template>
  <div class="CircularLoaderOverlay-backdrop">
    <div class="CircularLoaderOverlay-wrap">
      <div class="CircularLoaderOverlay">
        <CircularLoader />
      </div>
    </div>
  </div>
</template>

<style lang="postcss">
.CircularLoaderOverlay {
  padding: 8px;
  display: flex;
  width: 50px;
  height: 50px;
  justify-content: center;
  align-items: center;
  border-radius: 50%;
  background-color: white;
  z-index: 100000;

  &-wrap {
    width: 100%;
    height: 100%;
    display: flex;
    justify-content: center;
    align-items: center;
  }

  &-backdrop {
    top: 0;
    height: 100%;
    width: 100%;
    background-color: var(--fill1);
    display: flex;
    justify-content: center;
    position: fixed;
    z-index: 99999;
  }
}
</style>
