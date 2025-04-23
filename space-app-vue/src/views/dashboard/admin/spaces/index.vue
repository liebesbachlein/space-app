<script setup lang="ts">
import { icons } from '@/assets/icons/icons'
import Icon from '@/components/Icon.vue'
import ModalWindow from '@/components/ModalWindow.vue'
import DashboardLayoutContent from '@/layouts/DashboardLayoutContent.vue'
import { ref } from 'vue'
import EditSpaceModalWindow from '../edit-space/EditSpaceModalWindow.vue'
import apiClient from '@/services/api'
import { useFetch } from '@/composables/useFetch'
import type { SpaceType } from '../create-space/index.vue'

export interface Space {
  id: number
  name: string
  typeId: number
  typeName: string
  price: number
}

const {
  fetch: fetchContent,
  response: content,
  error: contentError,
  loading: contentLoading,
} = useFetch<{
  spaces: Space[]
  spaceTypes: SpaceType[]
}>({
  client: apiClient,
  path: '/spaces',
  method: 'GET',
})

fetchContent({})

const {
  fetch: suspendSpace,
  error: suspendSpaceError,
  loading: suspendSpaceLoading,
} = useFetch({
  client: apiClient,
  path: '/admin/space',
  method: 'DELETE',
})

const spaceForSuspention = ref<Space | null>(null)
const spaceForEdit = ref<Space | null>(null)

function handleSuspendSpace() {
  if (spaceForSuspention.value) {
    suspendSpace({
      path: '/admin/space/' + spaceForSuspention.value.id,
    }).then(() => {
      fetchContent({})
      spaceForSuspention.value = null
    })
  }
}
</script>

<template>
  <DashboardLayoutContent>
    <template #main>
      <ModalWindow
        v-if="spaceForSuspention"
        header="Are you sure absolutely sure?"
        :subheader="`This will suspend the space '${spaceForSuspention.name}'`"
      >
        <div class="ModalWindow-buttons">
          <button
            @click="spaceForSuspention = null"
            class="button1 button1--outlined button-animation"
          >
            Abort
          </button>
          <button
            @click="handleSuspendSpace()"
            class="button1 button-animation"
            :class="{ 'button--loading': suspendSpaceLoading }"
          >
            Suspend space
          </button>
        </div>
      </ModalWindow>
      <EditSpaceModalWindow
        v-if="spaceForEdit && content"
        :space="spaceForEdit"
        :spaceTypes="content.spaceTypes"
        @abort="spaceForEdit = null"
        @complete="
          () => {
            spaceForEdit = null
            fetchContent({})
          }
        "
      />
      <h1 class="Dashboard-headline">Spaces</h1>
      <table class="table" v-if="content">
        <tbody>
          <tr class="table__header">
            <th class="table__header-name table__header--short">ID</th>
            <th class="table__header-name table__header--long">Space name</th>
            <th class="table__header-name table__header--medium">Type</th>
            <th class="table__header-name table__header--medium">Price</th>
            <th class="table__header-name table__header--short table__header--button"></th>
          </tr>
          <tr v-for="space in content.spaces" class="table__row">
            <td class="table__entry">{{ space.id }}</td>
            <td class="table__entry">{{ space.name }}</td>
            <td class="table__entry">{{ space.typeName }}</td>
            <td class="table__entry">{{ space.price }}</td>
            <td class="table__entry">
              <div class="table__button_popup">
                <div class="table__button button3" style="border: 1px solid var(--stroke)">
                  <Icon :svg="icons.threeDotes" />
                </div>
                <div class="table__popup">
                  <button @click="spaceForEdit = space" class="button2 button-animation">
                    Edit
                  </button>
                  <button
                    @click="spaceForSuspention = space"
                    class="button2 button-animation"
                    style="color: var(--red)"
                  >
                    Suspend
                  </button>
                </div>
              </div>
            </td>
          </tr>
        </tbody>
      </table>
    </template>
  </DashboardLayoutContent>
</template>
