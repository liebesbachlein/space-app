<script setup lang="ts">
import { useAuthStore } from '@/stores/auth'
import router from '@/router'
const { user, useLogout } = useAuthStore()
const { logout } = useLogout()

function handleLogout() {
  logout(() => {
    router.push('/')
  })
}
</script>

<template>
  <nav class="AdminMenu">
    <div class="AdminMenu-header AdminMenu-padded">
      <h3 style="padding: 0 8px">{{ user?.name }}</h3>
    </div>
    <div class="separator"></div>
    <div class="AdminMenu-links AdminMenu-padded">
      <RouterLink
        to="/admin/spaces"
        class="button2 button-animation"
        active-class="button2--active"
      >
        Spaces
      </RouterLink>
      <RouterLink
        to="/admin/space/create"
        class="button2 button-animation"
        active-class="button2--active"
      >
        Create a space
      </RouterLink>
      <!--<RouterLink to="/admin/users" class="button2 button-animation" active-class="button2--active">
        Users
      </RouterLink>-->
    </div>
    <div class="separator"></div>
    <div class="AdminMenu-links AdminMenu-padded">
      <button @click="handleLogout()" class="button3 button3--red button-animation">Logout</button>
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
