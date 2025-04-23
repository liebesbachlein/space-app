<script setup lang="ts">
import { icons } from '@/assets/icons/icons'
import Icon from '@/components/Icon.vue'
import DashboardLayoutContentAlt from '@/layouts/DashboardLayoutContentAlt.vue'
import router from '@/router'
import { computed, onMounted, reactive, ref, watch } from 'vue'
import { useRoute } from 'vue-router'
import ModalWindow from '@/components/ModalWindow.vue'
import Calendar from '@/components/Calendar.vue'
import { useFetch } from '@/composables/useFetch'
import apiClient from '@/services/api'
import { mapReactiveFormToQuery, type ReactiveForm } from '@/util/reactiveForm'
import {
  parseDateStrToObj,
  parseHourStrToInt,
  prettyDateMonthYearFromObj,
  prettyHourInt,
  prettyHourStr,
  stringifyDateObj,
} from '@/util/dateTime'

interface Space {
  id: number
  name: string
  price: number
  typeName: string
}

interface Reservation {
  hour: string
}

interface ReservationWithStatus {
  hour: string
  status: 'UNAVAILABLE' | 'SELECTED' | 'AVAILABLE'
}

const route = useRoute()

const {
  fetch: fetchContent,
  response: content,
  error: contentError,
  loading: contentLoading,
} = useFetch<{
  selectedSpace: Space
  spaces: Space[]
  userReservationsOnDate: Reservation[]
  spaceReservationsOnDate: Reservation[]
}>({
  client: apiClient,
  path: '/reservation/form',
  method: 'GET',
})

const {
  fetch: createReservation,
  error: createReservationError,
  loading: createReservationLoading,
} = useFetch({
  client: apiClient,
  path: '/checkout',
  method: 'POST',
})

const dateModel = ref<Date>(
  route.query.date ? parseDateStrToObj(route.query.date as string) : new Date(),
)
const spaceIdModel = ref<string>(route.query.spaceId as string)

const selectedReservationSpace = ref<Reservation | null>(null)

const nameOfSelectedSpace = computed(() => {
  if (content.value) {
    const res = content.value.spaces.find((e) => e.id.toString() == spaceIdModel.value)?.name
    return res ?? 'Select space'
  } else return 'Select space'
})
const visibilityOfSpaceSelect = ref(false)

const reservations = computed<ReservationWithStatus[]>(() => {
  const map = new Map<number, 'UNAVAILABLE' | 'SELECTED' | 'AVAILABLE'>()
  if (content.value) {
    const openingHour = 8
    const closingHour = 20

    for (let hour = openingHour; hour <= closingHour; hour++) {
      map.set(hour, 'AVAILABLE')
    }
    content.value.spaceReservationsOnDate.forEach((e) =>
      map.set(parseHourStrToInt(e.hour), 'UNAVAILABLE'),
    )
    content.value.userReservationsOnDate.forEach((e) =>
      map.set(parseHourStrToInt(e.hour), 'SELECTED'),
    )
  }
  return [...map]
    .sort((a, b) => a[0] - b[0])
    .map((e): ReservationWithStatus => ({ hour: prettyHourInt(e[0]), status: e[1] }))
})

function selectSpace(space: Space) {
  spaceIdModel.value = space.id.toString()
  visibilityOfSpaceSelect.value = false
}

type CheckoutForm = 'email' | 'cvc'

const checkoutForm: ReactiveForm<CheckoutForm> = reactive({
  email: {
    value: '',
    error: '',
  },
  cvc: {
    value: '',
    error: '',
  },
})

function validateEmail() {
  const RegEx =
    /^(([^<>()[\]\.,;:\s@\"]+(\.[^<>()[\]\.,;:\s@\"]+)*)|(\".+\"))@(([^<>()[\]\.,;:\s@\"]+\.)+[^<>()[\]\.,;:\s@\"]{2,})$/i
  checkoutForm.email.error = RegEx.test(checkoutForm.email.value)
    ? ''
    : 'Required field, enter valid email'
  return checkoutForm.email.error.length == 0
}

function validateCVC() {
  const RegEx = /^[0-9]{3}$/
  checkoutForm.cvc.error = RegEx.test(checkoutForm.cvc.value)
    ? ''
    : 'Required field, enter valid CVC (3 digits)'
  return checkoutForm.cvc.error.length == 0
}

const createReservationSuccess = ref(false)

function handleCreateReservation() {
  if (selectedReservationSpace.value && validateCVC() && validateEmail()) {
    createReservation({
      payload: {
        date: stringifyDateObj(dateModel.value),
        hour: selectedReservationSpace.value.hour,
        spaceId: spaceIdModel.value,
        cvc: checkoutForm.cvc.value,
        email: checkoutForm.email.value,
      },
    }).then(() => {
      fetchContent({
        params: {
          date: stringifyDateObj(dateModel.value),
          spaceId: spaceIdModel.value,
        },
      })
      createReservationSuccess.value = true
      setTimeout(() => {
        selectedReservationSpace.value = null
        createReservationError.value = null
      }, 1000)
    })
  }
}

const spaceDetails = ref<HTMLElement | null>(null)

watch(
  [spaceIdModel, dateModel],
  () => {
    router.replace({
      query: {
        date: stringifyDateObj(dateModel.value),
        spaceId: spaceIdModel.value,
      },
    })
    fetchContent({
      params: {
        date: stringifyDateObj(dateModel.value),
        spaceId: spaceIdModel.value,
      },
    })
    if (spaceDetails.value) {
      spaceDetails.value.classList.add('box--shimmer')
      setTimeout(() => {
        if (spaceDetails.value) {
          spaceDetails.value.classList.remove('box--shimmer')
        }
      }, 1000)
    }
  },
  { immediate: true },
)

const selectWrap = ref<Element | null>(null)

onMounted(() => {
  document.addEventListener('click', (event: Event) => {
    if (selectWrap.value && event) {
      if (!selectWrap.value.contains(event.target as Node)) {
        visibilityOfSpaceSelect.value = false
      }
    }
  })
})
</script>

<template>
  <DashboardLayoutContentAlt>
    <template #main>
      <ModalWindow
        v-if="selectedReservationSpace"
        :header="`You are about to reserve the space (Total $${content?.selectedSpace.price})`"
        :subheader="`Space '${nameOfSelectedSpace}' on ${prettyDateMonthYearFromObj(dateModel)}, ${prettyHourStr(selectedReservationSpace.hour)} for 1 hour. Hint: enter test_johnwick@gmail.com with CVC 123 (has $15.75) or test_belova@gmail.com with CVC 456 (has $8515.5)`"
      >
        <div class="form">
          <div class="form-inputs">
            <div class="input-wrap">
              <label for="email" class="label">Email</label>
              <input
                @blur="validateEmail"
                type="email"
                name="email"
                id="email"
                class="input"
                v-model="checkoutForm.email.value"
              />
              <label v-if="checkoutForm.email.error" class="error-label">{{
                checkoutForm.email.error
              }}</label>
            </div>
            <div class="input-wrap">
              <label for="cvc" class="label">CVC</label>
              <input
                @blur="validateCVC"
                type="text"
                name="cvc"
                id="password"
                class="input"
                v-model="checkoutForm.cvc.value"
              />
              <label v-if="checkoutForm.cvc.error" class="error-label">{{
                checkoutForm.cvc.error
              }}</label>
            </div>
          </div>
          <div class="form-buttons">
            <button
              @click="
                () => {
                  selectedReservationSpace = null
                  createReservationError = null
                }
              "
              class="button1 button1--outlined button-animation"
            >
              Abort
            </button>
            <button
              @click="handleCreateReservation()"
              class="button1 button-animation"
              :class="{
                'button--loading': createReservationLoading,
              }"
            >
              Pay
            </button>
          </div>
          <label v-if="createReservationError" class="error-label">
            Error occurred while creating the reservation and payment process. Check if you have
            enough money pn card
          </label>
          <label v-else-if="createReservationSuccess" class="success-label">
            Successfully created reservation and processed a payment
          </label>
        </div>
      </ModalWindow>
      <h1 class="Dashboard-headline">Create a Reservation</h1>
      <h4 v-if="contentError">{{ contentError.message }}</h4>
      <div
        ref="spaceDetails"
        class="Dashboard-SpaceDetails"
        v-if="content && content.selectedSpace"
      >
        <h4 class="Dashboard-SpaceDetails-title">Info on the selected space</h4>
        <div class="Dashboard-SpaceDetails-row">
          <h4 class="Dashboard-SpaceDetails-key">Name:</h4>
          <h4 class="Dashboard-SpaceDetails-value">{{ content.selectedSpace.name }}</h4>
        </div>
        <div class="Dashboard-SpaceDetails-row">
          <h4 class="Dashboard-SpaceDetails-key">Type:</h4>
          <h4 class="Dashboard-SpaceDetails-value">{{ content.selectedSpace.typeName }}</h4>
        </div>
        <div class="Dashboard-SpaceDetails-row">
          <h4 class="Dashboard-SpaceDetails-key">Price:</h4>
          <h4 class="Dashboard-SpaceDetails-value">{{ content.selectedSpace.price }}$</h4>
        </div>
        <div class="Dashboard-SpaceDetails-row">
          <h4 class="Dashboard-SpaceDetails-key">Date:</h4>
          <h4 class="Dashboard-SpaceDetails-value">
            {{ prettyDateMonthYearFromObj(dateModel) }}
          </h4>
        </div>
      </div>
      <div class="Dashboard-SpaceDetails" v-else>
        <h4 class="Dashboard-SpaceDetails-title">Pick a date and a space</h4>
      </div>
      <table class="table" v-if="content && content.selectedSpace">
        <tbody>
          <tr class="table__header">
            <th class="table__header-name table__header--medium">Time</th>
            <th class="table__header-name table__header--short table__header--button">Action</th>
          </tr>
          <tr v-for="reservation in reservations" class="table__row">
            <td class="table__entry">{{ prettyHourStr(reservation.hour) }}</td>
            <td class="table__entry">
              <button
                v-if="reservation.status == 'UNAVAILABLE'"
                class="button3 button3--red"
                style="cursor: not-allowed; width: 80px"
              >
                Reserved
              </button>
              <button
                v-if="reservation.status == 'SELECTED'"
                class="button3 button3--grey"
                style="cursor: not-allowed; width: 80px"
              >
                Selected
              </button>
              <button
                v-if="reservation.status == 'AVAILABLE'"
                class="button3 button3--green button-animation"
                @click="selectedReservationSpace = reservation"
                style="width: 80px"
              >
                Reserve
              </button>
            </td>
          </tr>
        </tbody>
      </table>
    </template>
    <template #panel>
      <div class="form">
        <div class="form-inputs">
          <Calendar ref="calendar" v-model="dateModel" />
          <div class="input-wrap">
            <input type="hidden" name="spaceId" id="spaceId" class="input" v-model="spaceIdModel" />
            <div
              ref="selectWrap"
              class="select-wrap"
              :class="{
                'select-wrap--active': visibilityOfSpaceSelect,
              }"
            >
              <div
                class="input select-input"
                @click="visibilityOfSpaceSelect = !visibilityOfSpaceSelect"
              >
                <span>{{ nameOfSelectedSpace }}</span>
                <div class="input--icon">
                  <Icon :svg="icons.chevronDown" />
                </div>
              </div>
              <ul class="select-option-list" v-if="content && content.spaces">
                <li
                  v-for="space in content.spaces"
                  class="select-option-item button2"
                  @click="selectSpace(space)"
                >
                  {{ space.name }}
                </li>
              </ul>
            </div>
            <label v-if="!spaceIdModel" class="error-label">Choose a space</label>
          </div>
        </div>
      </div>
    </template>
  </DashboardLayoutContentAlt>
</template>

<style lang="postcss">
.Dashboard-SpaceDetails {
  background-color: var(--fill);
  border: 1px solid var(--stroke);
  border-radius: var(--radius);
  padding: 12px;
  display: flex;
  flex-direction: column;
  gap: 8px;

  &-row,
  &-key {
    font: 500 14px/20px var(--font-family);
  }

  &-row {
    display: flex;
  }

  &-row:first-child {
    padding-top: 8px;
  }

  &-key {
    color: var(--grey);
    width: 80px;
  }

  &-title {
    font: 400 14px/20px var(--font-family);
    color: var(--grey);
  }
}
</style>
