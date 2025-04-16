<script setup lang="ts">
const props = defineProps<{
  svg: string
  inlineClass?: string
  isClickable?: boolean
}>()
</script>

<template>
  <div
    class="Icon"
    v-html="props.svg"
    :class="[props.inlineClass ?? '', { 'Icon--clickable': props.isClickable }]"
  />
</template>

<style>
.Icon {
  line-height: 0;
  display: block;
  width: fit-content;
  height: fit-content;
}

.Icon--clickable {
  cursor: pointer;
}
</style>
