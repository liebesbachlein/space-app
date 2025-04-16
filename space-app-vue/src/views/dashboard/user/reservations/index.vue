<script setup lang="ts">
import { icons } from '@/assets/icons/icons'
import Icon from '@/components/Icon.vue'
import useUrl from '@/composables/useUrl'
import DashboardLayoutContent from '@/layouts/DashboardLayoutContent.vue'
import router from '@/router'
import { reactive, ref, watch } from 'vue'
import { useRoute } from 'vue-router'
import ModalWindow from '@/components/ModalWindow.vue'
import useDateTime from '@/composables/useDateTime'

const { parseDate, parseHour } = useDateTime()
const route = useRoute()
const selectedTab = reactive<{ selectedTab: 'active' | 'archive' | string }>({
  selectedTab: 'active',
})
const cancelReservationModalVisibility = ref(false)

const { getParamValue } = useUrl()

function changeTab(tab: string) {
  selectedTab.selectedTab = tab
  router.replace({ query: selectedTab })
}

watch(
  () => route.fullPath,
  () => {
    selectedTab.selectedTab = getParamValue('selectedTab') ?? 'active'
    /*fetchGet(
            `/api/products/${categorySlug.value}?${serializeParams(buildRequestParams())}`,
        );*/
  },
  { immediate: true },
)

interface Reservation {
  id: number
  spaceName: string
  hour: string
  date: string
}

const reservations: Reservation[] = [
  {
    id: 0,
    spaceName: 'Hello',
    hour: '11:00:00 GMT+0900',
    date: '2025/05/11',
  },
  {
    id: 0,
    spaceName: 'Hello',
    hour: '12:00:00 GMT+0900',
    date: '2025/05/11',
  },
  {
    id: 0,
    spaceName: 'Hello',
    hour: '15:00:00 GMT+0900',
    date: '2025/05/11',
  },
  {
    id: 0,
    spaceName: 'Hello',
    hour: '10:00:00 GMT+0900',
    date: '2025/05/11',
  },
]
</script>

<template>
  <DashboardLayoutContent>
    <template #main>
      <ModalWindow
        v-if="cancelReservationModalVisibility"
        header="Are you sure absolutely sure?"
        subheader="This action cannot be undone. This will cancel your reservation."
      >
        <div class="ModalWindow-buttons">
          <button
            @click="cancelReservationModalVisibility = false"
            class="button1 button1--outlined button-animation"
          >
            Abort
          </button>
          <button
            @click="cancelReservationModalVisibility = false"
            class="button1 button-animation"
          >
            Cancel reservation
          </button>
        </div>
      </ModalWindow>
      <h1 class="Dashboard-headline">My Reservations</h1>
      <div class="Dashboard-filters" style="display: flex; gap: 8px">
        <button
          class="button3 button-animation"
          :class="{
            'button3--green': selectedTab.selectedTab == 'active',
          }"
          @click="changeTab('active')"
        >
          Active
        </button>
        <button
          class="button3 button-animation"
          :class="{
            'button3--grey': selectedTab.selectedTab == 'archive',
          }"
          @click="changeTab('archive')"
        >
          Archive
        </button>
      </div>
      <table class="table">
        <tr class="table__header">
          <th class="table__header-name table__header--long">Space name</th>
          <th class="table__header-name table__header--medium">Date</th>
          <th class="table__header-name table__header--medium">Time</th>
          <th class="table__header-name table__header--short table__header--button"></th>
        </tr>
        <tr v-for="reservation in reservations" class="table__row">
          <td class="table__entry">{{ reservation.spaceName }}</td>
          <td class="table__entry">{{ parseDate(reservation.date) }}</td>
          <td class="table__entry">{{ parseHour(reservation.hour) }}</td>
          <td class="table__entry">
            <button
              class="button3 button3--red button-animation"
              @click="cancelReservationModalVisibility = true"
            >
              Cancel
            </button>
          </td>
        </tr>
      </table>
    </template>
  </DashboardLayoutContent>
</template>
