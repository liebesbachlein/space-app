<script setup lang="ts">
import { onBeforeMount, onMounted, reactive, ref, useTemplateRef } from 'vue'

type Vector = {
  x: number
  y: number
  xDirection: boolean
  yDirection: boolean
  size: number
}
const shapeWrap = ref<HTMLElement>()
let shapeVectors = reactive<Vector[]>([])

function randomIntFromInterval(min: number, max: number) {
  return Math.floor(Math.random() * (max - min + 1) + min)
}

function randomBoolean() {
  return Math.random() % 2 == 0
}

function initShapeVector(width: number, height: number, offset: number): Vector {
  const size = randomIntFromInterval(80, 320)
  return {
    x: randomIntFromInterval(-offset, width + offset - size),
    y: randomIntFromInterval(-offset, height + offset - size),
    xDirection: randomBoolean(),
    yDirection: randomBoolean(),
    size: size,
  }
}

function moveShapeVector(vector: Vector, width: number, height: number, offset: number): Vector {
  const newVector: Vector = vector

  if (vector.x > width + offset - vector.size) vector.xDirection = false
  else if (vector.x < -offset) vector.xDirection = true

  if (vector.y > height + offset - vector.size) vector.yDirection = false
  else if (vector.y < -offset) vector.yDirection = true

  newVector.x += vector.xDirection ? 1 : -1
  newVector.y += vector.yDirection ? 1 : -1

  return newVector
}

function startShapeAnimation(width: number, height: number, offset: number) {
  const numberoOfShapes = 16
  for (let i = 0; i <= numberoOfShapes; i++) {
    shapeVectors.push(initShapeVector(width, height, offset))
  }

  setInterval(() => {
    shapeVectors = shapeVectors.map((vector) => moveShapeVector(vector, width, height, offset))
  }, 50)
}

onBeforeMount(() => {})

onMounted(() => {
  const width = shapeWrap.value?.clientWidth ?? window.innerWidth
  const height = shapeWrap.value?.clientHeight ?? window.innerHeight
  const offset = 16

  startShapeAnimation(width, height, offset)
})
</script>

<template>
  <div ref="shapeWrap" class="page-layout Landing">
    <div class="Landing-ShapeWrap">
      <div
        v-for="(shapeVector, i) in shapeVectors"
        :key="`landingShape${i}`"
        class="Landing-Shape"
        :style="{
          left: shapeVector.x + 'px',
          top: shapeVector.y + 'px',
          width: shapeVector.size + 'px',
          height: shapeVector.size + 'px',
        }"
      ></div>
    </div>
    <div class="page-center-lock Landing-BannerWrap">
      <div class="Landing-Banner">
        <h1 class="Landing-Banner-headline">
          A place where you can book a space to work in team or solo up to your TASTE
        </h1>
        <p class="Landing-Banner-info">
          Lorem ipsum dolor sit amet, consectetur adipiscing elit, sed do eiusmod tempor incididunt
          ut labore et dolore magna aliqua. Ut enim ad minim veniam, quis nostrud exercitation
          ullamco laboris nisi ut aliquip ex ea commodo consequat. Duis aute irure dolor in
          reprehenderit in voluptate velit esse cillum dolore eu fugiat nulla pariatur. Excepteur
          sint occaecat cupidatat non proident, sunt in culpa qui officia deserunt mollit anim id
          est laborum.
        </p>
        <div class="Landing-Banner-buttons">
          <RouterLink to="/register" class="button1 button-animation button1--outlined"
            >Create an account
          </RouterLink>
          <RouterLink to="/login" class="button1 button-animation">Log in </RouterLink>
        </div>
      </div>
    </div>
  </div>
</template>

<style lang="postcss">
.Landing {
  position: relative;
  display: grid;
  grid-template-columns: 100%;
  grid-template-rows: 100%;
  overflow: hidden;

  &-Shape {
    width: 160px;
    height: 160px;
    border-radius: 50%;
    border: 10px solid var(--brown);
    background-color: var(--black);
    position: fixed;

    &Wrap {
      position: relative;
      grid-row: 1;
      grid-column: 1;
      z-index: 0;
      display: flex;
      flex-wrap: wrap;
      gap: 32px;
      overflow: hidden;
      filter: blur(30px);
    }
  }

  &-Banner {
    display: relative;
    width: 100%;
    max-width: 1000px;
    height: fit-content;
    min-height: 500px;
    background-color: #d4e6fa17;
    backdrop-filter: blur(50px);
    border-radius: var(--radius);
    border: 1px solid #ffffffcc;
    box-shadow: 0 0 32px #0000001f;
    padding: 32px;
    display: flex;
    flex-direction: column;
    gap: 32px;

    &Wrap {
      display: relative;
      grid-row: 1;
      grid-column: 1;
      z-index: 1;
      padding: 100px;
    }

    &-headline {
      font: 800 48px/48px var(--font-family);
    }

    &-info {
      font: 400 16px/20px var(--font-family);
      color: var(--grey);
      width: 75%;
    }

    &-buttons {
      flex: 1;
      display: flex;
      gap: 8px;
      align-items: flex-end;
    }
  }
}
</style>
