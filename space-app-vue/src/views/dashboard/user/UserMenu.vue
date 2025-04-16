<script setup lang="ts">
import { useRoute } from 'vue-router'
const route = useRoute()
</script>

<template>
  <nav class="UserMenu">
    <div class="UserMenu-header UserMenu-padded">
      <h3 style="padding: 0 8px">Aigerim</h3>
    </div>
    <div class="separator"></div>
    <div class="UserMenu-links UserMenu-padded">
      <RouterLink to="/user/account" v-slot="{ href }" custom>
        <a
          :href="href"
          class="button2 button-animation"
          :class="{ 'button2--active': route.path == href }"
        >
          <span>Account</span>
        </a>
      </RouterLink>
      <RouterLink to="/user/reservations" v-slot="{ href }" custom>
        <a
          :href="href"
          class="button2 button-animation"
          :class="{ 'button2--active': route.path == href }"
        >
          <span>My reservations</span>
        </a>
      </RouterLink>
      <RouterLink to="/user/reservation/create" v-slot="{ href }" custom>
        <a
          :href="href"
          class="button2 button-animation"
          :class="{ 'button2--active': route.path == href }"
        >
          <span>Create a reservation</span>
        </a>
      </RouterLink>
    </div>
    <div class="separator"></div>
    <div class="UserMenu-links UserMenu-padded">
      <RouterLink to="/user/logout" v-slot="{ href }" custom>
        <a :href="href" class="button3 button3--red button-animation">
          <span>Logout</span>
        </a>
      </RouterLink>
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
