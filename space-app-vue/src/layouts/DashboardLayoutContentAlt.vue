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
  position: relative;
  display: grid;
  grid-template-columns: 238px 1fr 320px;
  height: 100%;
  overflow: auto;
}

.Dashboard-Main-alt ~ .Dashboard-Menu {
  padding: 16px;
}

.Dashboard-Main-alt ~ .Dashboard-Menu .input,
.Dashboard-Main-alt ~ .Dashboard-Menu .input-wrap {
  width: 100%;
}
</style>
