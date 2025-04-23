<script setup lang="ts">
import DashboardLayoutContent from '@/layouts/DashboardLayoutContent.vue'
import router from '@/router'
import { reactive, ref, watch } from 'vue'
import { useRoute } from 'vue-router'
import ModalWindow from '@/components/ModalWindow.vue'
import { useFetch } from '@/composables/useFetch'
import apiClient from '@/services/api'
import { prettyDateMonthYearFromStr, prettyHourStr } from '@/util/dateTime'

interface Reservation {
  id: number
  spaceName: string
  hour: string
  date: string
}

const route = useRoute()

const params: { selectedTab: 'active' | 'archive' | string } = reactive({
  selectedTab: 'active',
})

function changeTab(tab: string) {
  params.selectedTab = tab
  router.replace({ query: params })
}

const {
  fetch: fetchContent,
  response: content,
  error: contentError,
  loading: contentLoading,
} = useFetch<{ reservations: Reservation[] }>({
  client: apiClient,
  path: '/reservations/user',
  method: 'GET',
})

const reservationForCancellation = ref<Reservation | null>(null)
const {
  fetch: cancelReservation,
  error: cancelReservationError,
  loading: cancelReservationLoading,
} = useFetch({
  client: apiClient,
  method: 'DELETE',
})

function handleCancelReservation() {
  if (reservationForCancellation.value) {
    cancelReservation({
      path: `/reservation/${reservationForCancellation.value.id}`,
    }).then(() => {
      reservationForCancellation.value = null
      fetchContent({ params: params })
    })
  }
}

watch(
  () => route.fullPath,
  () => {
    params.selectedTab = (route.query.selectedTab as string) ?? 'active'
    fetchContent({ params: params })
  },
  { immediate: true },
)
</script>

<template>
  <DashboardLayoutContent>
    <template #main>
      <ModalWindow
        v-if="reservationForCancellation"
        header="Are you sure absolutely sure?"
        subheader="This action cannot be undone. This will remove your reservation."
      >
        <div class="ModalWindow-buttons">
          <button
            @click="reservationForCancellation = null"
            class="button1 button1--outlined button-animation"
          >
            Abort
          </button>
          <button @click="handleCancelReservation()" class="button1 button-animation">
            Proceed
          </button>
        </div>
        <h5 v-if="cancelReservationError" class="error-label">
          Error occurred while cancelling the reservation
        </h5>
      </ModalWindow>
      <h1 class="Dashboard-headline">My Reservations</h1>
      <div class="Dashboard-filters" style="display: flex; gap: 8px">
        <button
          class="button3 button-animation"
          :class="{
            'button3--green': params.selectedTab == 'active',
          }"
          @click="changeTab('active')"
        >
          Active
        </button>
        <button
          class="button3 button-animation"
          :class="{
            'button3--grey': params.selectedTab == 'archive',
          }"
          @click="changeTab('archive')"
        >
          Archive
        </button>
      </div>
      <h4 v-if="content && content.reservations.length == 0">You have no reservations yet</h4>
      <table v-else-if="content && content.reservations.length > 0" class="table">
        <tbody>
          <tr class="table__header">
            <th class="table__header-name table__header--long">Space name</th>
            <th class="table__header-name table__header--medium">Date</th>
            <th class="table__header-name table__header--medium">Time</th>
            <th class="table__header-name table__header--short table__header--button"></th>
          </tr>
          <tr v-for="reservation in content.reservations" class="table__row">
            <td class="table__entry">{{ reservation.spaceName }}</td>
            <td class="table__entry">{{ prettyDateMonthYearFromStr(reservation.date) }}</td>
            <td class="table__entry">{{ prettyHourStr(reservation.hour) }}</td>
            <td class="table__entry">
              <button
                class="button3 button3--red button-animation"
                @click="reservationForCancellation = reservation"
                :class="{
                  'button3--grey': params.selectedTab == 'archive',
                }"
              >
                {{ params.selectedTab == 'archive' ? 'Delete' : 'Cancel' }}
              </button>
            </td>
          </tr>
        </tbody>
      </table>
    </template>
  </DashboardLayoutContent>
</template>
