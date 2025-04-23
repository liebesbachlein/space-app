<template>
  <main class="Dashboard-Main Dashboard-Main-alt">
    <slot name="main" />
  </main>
  <aside class="Dashboard-Menu">
    <slot name="panel" />
  </aside>
</template>

<style lang="postcss">
.Dashboard:has(main.Dashboard-Main-alt) {
  grid-template-columns: 238px 1fr 320px;
}

.Dashboard-Main-alt ~ .Dashboard-Menu {
  padding: 16px;
}

.Dashboard-Main-alt ~ .Dashboard-Menu .input,
.Dashboard-Main-alt ~ .Dashboard-Menu .input-wrap {
  width: 100%;
}
</style>
