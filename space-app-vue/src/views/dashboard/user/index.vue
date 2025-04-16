<script setup lang="ts">
import DashboardLayout from '@/layouts/DashboardLayout.vue'
import UserMenu from './UserMenu.vue'
import { RouterView } from 'vue-router'
</script>

<template>
  <DashboardLayout>
    <template #menu><UserMenu /></template>
    <template #content><RouterView /></template>
  </DashboardLayout>
</template>
