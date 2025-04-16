<script setup lang="ts">
import { icons } from '@/assets/icons/icons'
import Icon from '@/components/Icon.vue'
import useUrl from '@/composables/useUrl'
import DashboardLayoutContentAlt from '@/layouts/DashboardLayoutContentAlt.vue'
import router from '@/router'
import { computed, reactive, ref, watch } from 'vue'
import { useRoute } from 'vue-router'
import useDateTime from '@/composables/useDateTime'
import ModalWindow from '@/components/ModalWindow.vue'

const { parseDate, parseHour } = useDateTime()

const spaceDateInput = reactive<{
  spaceId: {
    input: string
    error: string
  }
  date: {
    input: string
    error: string
  }
}>({
  spaceId: {
    input: '',
    error: '',
  },
  date: {
    input: '',
    error: '',
  },
})

function validateDate() {
  spaceDateInput.date.error = spaceDateInput.date.input.length > 0 ? '' : 'Required field'
  return spaceDateInput.date.error.length == 0
}

function validateSpaceId() {
  const RegEx = /^[0-9]+$/
  spaceDateInput.spaceId.error = RegEx.test(spaceDateInput.spaceId.input) ? '' : 'Required field'
  return spaceDateInput.spaceId.error.length == 0
}

interface Space {
  id: number
  name: string
}

const spaceList: Space[] = [
  {
    id: 1,
    name: 'Hello',
  },
  {
    id: 1,
    name: 'Hello',
  },
  {
    id: 1,
    name: 'Hello',
  },
  {
    id: 1,
    name: 'Hello',
  },
]

function selectSpace(space: Space) {
  spaceDateInput.spaceId.input = space.id.toString()
  spaceListVisibility.value = false
}

const selectedSpaceName = computed(() => {
  const res = spaceList
    .filter((e) => e.id.toString() == spaceDateInput.spaceId.input)
    .map((e) => e.name)
  return res.pop() ?? 'Select space'
})

const spaceListVisibility = ref(false)

const { getParamValue } = useUrl()

const route = useRoute()

watch(
  () => route.fullPath,
  () => {
    spaceDateInput.date.input = getParamValue('date') ?? ''
    spaceDateInput.spaceId.input = getParamValue('spaceId') ?? ''
    /*fetchGet(
            `/api/products/${categorySlug.value}?${serializeParams(buildRequestParams())}`,
        );*/
  },
  { immediate: true },
)

function handleSpaceDateInput() {
  if (validateDate() && validateSpaceId()) {
    router.replace({
      query: {
        spaceId: spaceDateInput.spaceId.input,
        date: spaceDateInput.date.input,
      },
    })
  }
}

interface SpaceDetails {
  id: number
  name: string
  price: number
  typeName: string
}

const spaceDetails: SpaceDetails = {
  id: 0,
  name: 'Hello',
  price: 100,
  typeName: 'Private',
}

interface ReservationSpace {
  hour: string
  status: string
}

const reservations: ReservationSpace[] = [
  {
    hour: '11:00:00 GMT+0900',
    status: 'AVAILABLE',
  },
  {
    hour: '11:00:00 GMT+0900',
    status: 'AVAILABLE',
  },
  {
    hour: '11:00:00 GMT+0900',
    status: 'UNAVAILABLE',
  },
  {
    hour: '11:00:00 GMT+0900',
    status: 'RESERVED',
  },
]

const selectedReservationSpace = ref<ReservationSpace | null>(null)
</script>

<template>
  <DashboardLayoutContentAlt>
    <template #main>
      <ModalWindow
        v-if="selectedReservationSpace"
        header="You are about to reserve the space"
        :subheader="`Space '${selectedSpaceName}' on ${parseDate(spaceDateInput.date.input)}, ${parseHour(selectedReservationSpace.hour)} for 1 hour`"
      >
        <div class="ModalWindow-buttons">
          <button
            @click="selectedReservationSpace = null"
            class="button1 button1--outlined button-animation"
          >
            Abort
          </button>
          <button @click="selectedReservationSpace = null" class="button1 button-animation">
            Create reservation
          </button>
        </div>
      </ModalWindow>
      <h1 class="Dashboard-headline">Create a Reservation</h1>
      <div class="Dashboard-SpaceDetails" v-if="spaceDetails">
        <h4 class="Dashboard-SpaceDetails-title">Info on the selected space</h4>
        <div class="Dashboard-SpaceDetails-row">
          <h4 class="Dashboard-SpaceDetails-key">Name:</h4>
          <h4 class="Dashboard-SpaceDetails-value">{{ spaceDetails.name }}</h4>
        </div>
        <div class="Dashboard-SpaceDetails-row">
          <h4 class="Dashboard-SpaceDetails-key">Type:</h4>
          <h4 class="Dashboard-SpaceDetails-value">{{ spaceDetails.typeName }}</h4>
        </div>
        <div class="Dashboard-SpaceDetails-row">
          <h4 class="Dashboard-SpaceDetails-key">Price:</h4>
          <h4 class="Dashboard-SpaceDetails-value">{{ spaceDetails.price }}$</h4>
        </div>
        <div class="Dashboard-SpaceDetails-row">
          <h4 class="Dashboard-SpaceDetails-key">Date:</h4>
          <h4 class="Dashboard-SpaceDetails-value">{{ parseDate(spaceDateInput.date.input) }}</h4>
        </div>
      </div>
      <table class="table">
        <tr class="table__header">
          <th class="table__header-name table__header--medium">Time</th>
          <th class="table__header-name table__header--short table__header--button">Action</th>
        </tr>
        <tr v-for="reservation in reservations" class="table__row">
          <td class="table__entry">{{ parseHour(reservation.hour) }}</td>
          <td class="table__entry">
            <button
              v-if="reservation.status == 'UNAVAILABLE'"
              class="button3 button3--red"
              style="cursor: not-allowed; width: 80px"
            >
              Reserved
            </button>
            <button
              v-if="reservation.status == 'RESERVED'"
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
      </table>
    </template>
    <template #panel>
      <div class="form">
        <div class="form-inputs">
          <div class="input-wrap">
            <label for="date" class="label">Date</label>
            <input
              @blur="validateDate"
              type="date"
              name="date"
              id="date"
              class="input"
              v-model="spaceDateInput.date.input"
            />
            <label v-if="spaceDateInput.date.error" class="error-label">{{
              spaceDateInput.date.error
            }}</label>
          </div>
          <div class="input-wrap">
            <label for="spaceId" class="label">Space name</label>
            <input
              @blur="validateSpaceId"
              type="hidden"
              name="spaceId"
              id="spaceId"
              class="input"
              v-model="spaceDateInput.spaceId.input"
            />
            <div
              class="select-wrap"
              :class="{
                'select-wrap--active': spaceListVisibility,
              }"
            >
              <div class="input select-input" @click="spaceListVisibility = !spaceListVisibility">
                <span>{{ selectedSpaceName }}</span>
                <div class="input--icon">
                  <Icon :svg="icons.chevronDown" />
                </div>
              </div>
              <ul class="select-option-list">
                <li
                  v-for="spaceItem in spaceList"
                  class="select-option-item button2"
                  @click="selectSpace(spaceItem)"
                >
                  {{ spaceItem.name }}
                </li>
              </ul>
            </div>
            <label v-if="spaceDateInput.spaceId.error" class="error-label">{{
              spaceDateInput.spaceId.error
            }}</label>
          </div>
        </div>
        <button
          @click="handleSpaceDateInput()"
          class="button3 button3--black button-animation"
          style="width: fit-content"
        >
          Find
        </button>
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

  &-key {
    color: var(--grey);
    width: 80px;
  }

  &-title {
    font: 400 14px/20px var(--font-family);
    color: var(--grey);
    padding-bottom: 8px;
  }
}
</style>
