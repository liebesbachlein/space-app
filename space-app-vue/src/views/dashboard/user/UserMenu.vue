<script setup lang="ts">
import router from '@/router'
import { useAuthStore } from '@/stores/auth'
const { user, useLogout } = useAuthStore()
const { logout } = useLogout()

function handleLogout() {
  logout(() => {
    router.push('/')
  })
}
</script>

<template>
  <nav class="UserMenu">
    <div class="UserMenu-header UserMenu-padded">
      <h3 style="padding: 0 8px">{{ user?.name }}</h3>
    </div>
    <div class="separator"></div>
    <div class="UserMenu-links UserMenu-padded">
      <!--<RouterLink to="/user/account" class="button2 button-animation" active-class="button2--active"
        >Account</RouterLink
      >-->
      <RouterLink
        to="/user/reservations"
        class="button2 button-animation"
        active-class="button2--active"
        >My reservations
      </RouterLink>
      <RouterLink
        to="/user/reservation/create"
        class="button2 button-animation"
        active-class="button2--active"
        >Create a reservation
      </RouterLink>
    </div>
    <div class="separator"></div>
    <div class="UserMenu-links UserMenu-padded">
      <button @click="handleLogout()" class="button3 button3--red button-animation">Logout</button>
    </div>
  </nav>
</template>

<style lang="postcss">
.UserMenu {
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
