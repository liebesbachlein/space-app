<template>
  <div class="f2c-CircularLoader-wrap">
    <div class="f2c-CircularLoader" />
  </div>
</template>

<style lang="postcss">
.f2c-CircularLoader {
  &-wrap {
    width: inherit;
    height: inherit;
    display: flex;
    justify-content: center;
    align-items: center;
    cursor: inherit;
  }

  & {
    position: relative;
    width: 20px;
    height: 20px;
    border: 2.5px solid var(--red);
    border-bottom-color: transparent;
    border-radius: 50%;
    display: block;
    box-sizing: border-box;
    animation: rotation 1s linear infinite;
  }
}

@keyframes rotation {
  0% {
    transform: rotate(0deg);
  }
  100% {
    transform: rotate(360deg);
  }
}
</style>
