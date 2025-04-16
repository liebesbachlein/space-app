<script setup lang="ts">
import { useRoute } from 'vue-router'
const route = useRoute()
</script>

<template>
  <nav class="AdminMenu">
    <div class="AdminMenu-header AdminMenu-padded">
      <h3 style="padding: 0 8px">Admin1</h3>
    </div>
    <div class="separator"></div>
    <div class="AdminMenu-links AdminMenu-padded">
      <RouterLink to="/admin/spaces" v-slot="{ href }" custom>
        <a
          :href="href"
          class="button2 button-animation"
          :class="{ 'button2--active': route.path == href }"
        >
          <span>Spaces</span>
        </a>
      </RouterLink>
      <RouterLink to="/admin/space/create" v-slot="{ href }" custom>
        <a
          :href="href"
          class="button2 button-animation"
          :class="{ 'button2--active': route.path == href }"
        >
          <span>Create a space</span>
        </a>
      </RouterLink>
      <RouterLink to="/admin/users" v-slot="{ href }" custom>
        <a
          :href="href"
          class="button2 button-animation"
          :class="{ 'button2--active': route.path == href }"
        >
          <span>Users</span>
        </a>
      </RouterLink>
    </div>
    <div class="separator"></div>
    <div class="AdminMenu-links AdminMenu-padded">
      <button @click="" class="button3 button3--red button-animation">
        <span>Logout</span>
      </button>
    </div>
  </nav>
</template>

<style lang="postcss">
.AdminMenu {
  width: 100%;
  padding: 20px 0;

  &-padded {
    padding: 0 12px;
  }

  &-header {
    font: 600 16px/20px var(--font-family);
    color: var(--black);
    padding-bottom: 4px;
  }

  &-links {
    display: flex;
    flex-direction: column;
    gap: 5px;
  }
}
</style>
