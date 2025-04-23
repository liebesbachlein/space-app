<script setup lang="ts">
import { computed, ref } from 'vue'
import Icon from './Icon.vue'
import { icons } from '@/assets/icons/icons'
import { getMonthNameFromInt, getNumberOfDaysInMonth, today } from '@/util/dateTime'
import { getNumberOfOffsetDays } from '../util/dateTime'
import date from 'date-and-time'

const dateModel = defineModel<Date>({ default: new Date() })

const day = computed(() => {
  return dateModel.value.getDate()
})

const monthName = computed(() => {
  return getMonthNameFromInt(dateModel.value.getMonth() + 1)
})

const monthIndex = computed(() => {
  return dateModel.value.getMonth()
})

const year = computed(() => {
  return dateModel.value.getFullYear()
})

const isMonthDecrementAllowed = computed(
  () =>
    dateModel.value.getMonth() > today.getMonth() ||
    dateModel.value.getFullYear() > today.getFullYear(),
)

function navigateMonth(increment = true) {
  const newDate = new Date(dateModel.value.getFullYear(), dateModel.value.getMonth(), 1)
  if (increment) {
    dateModel.value = date.addMonths(newDate, 1)
  } else if (isMonthDecrementAllowed.value) dateModel.value = date.addMonths(newDate, -1)
}

function selectDate(d: number) {
  dateModel.value = new Date(dateModel.value.getFullYear(), dateModel.value.getMonth(), d)
}
</script>

<template>
  <div class="Calendar">
    <div class="Calendar-header">
      <button
        class="Calendar-header-nav"
        @click="navigateMonth(false)"
        :class="{ 'Calendar-header-nav--disabled': !isMonthDecrementAllowed }"
      >
        <Icon :svg="icons.chevronLeft" />
      </button>
      <h4 class="Calendar-header-title">
        {{ monthName + ' ' + day + (year !== 2025 ? ' ' + year : '') }}
      </h4>
      <button class="Calendar-header-nav" @click="navigateMonth()">
        <Icon :svg="icons.chevronRight" />
      </button>
    </div>
    <div class="Calendar-week">
      <h5
        v-for="weekday in ['Mo', 'Tu', 'We', 'Th', 'Fr', 'Sa', 'Su']"
        :key="weekday"
        class="Calendar-weekday"
      >
        {{ weekday }}
      </h5>
    </div>
    <div class="Calendar-main">
      <div
        v-for="offset in getNumberOfOffsetDays(monthIndex, year)"
        :key="`offsetDay${offset}`"
        class="Calendar-date--offset"
      >
        {{ offset }}
      </div>
      <button
        v-for="d in getNumberOfDaysInMonth(monthIndex, year)"
        :key="`day${d}`"
        class="Calendar-date button-animation"
        :class="{ 'Calendar-date--active': d == day }"
        @click="selectDate(d)"
      >
        {{ d }}
      </button>
    </div>
  </div>
</template>

<style lang="postcss">
.Calendar {
  width: inherit;
  height: fit-content;

  &-header {
    height: fit-content;
    display: flex;
    justify-content: space-between;
    align-items: center;

    &-nav {
      height: 28px;
      width: 28px;
      border-radius: var(--radius);
      border: 1px solid var(--stroke);
      display: flex;
      justify-content: center;
      align-items: center;
    }

    &-nav--disabled {
      cursor: not-allowed;
    }

    &-title {
      font: 500 14px/20px var(--font-family);
    }

    &-nav:hover {
      background-color: var(--fill);
    }
  }

  &-week {
    width: 100%;
    display: flex;
    justify-content: space-between;
    padding: 16px 0 8px 0;
  }

  &-weekday {
    font: 400 12px/20px var(--font-family);
    color: var(--grey);
    width: 36px;
    text-align: center;
  }

  &-date {
    cursor: pointer;
    height: 36px;
    width: 36px;
    border-radius: var(--radius);
    display: flex;
    justify-content: center;
    align-items: center;
    font: 400 14px/20px var(--font-family);
  }

  &-date:hover {
    background-color: var(--fill);
  }

  &-date--active {
    background-color: var(--black);
    color: white;
  }

  &-date--active:hover {
    background-color: var(--black);
  }

  &-date--offset,
  &-date--offset:hover {
    cursor: default;
    background-color: transparent;
    color: transparent;
  }

  &-main {
    display: grid;
    grid-template-columns: repeat(7, auto);
    justify-content: space-between;
    gap: 8px 0;
  }
}
</style>
