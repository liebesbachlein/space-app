<script setup lang="ts">
import { icons } from '@/assets/icons/icons'
import Icon from '@/components/Icon.vue'
import ModalWindow from '@/components/ModalWindow.vue'
import DashboardLayoutContent from '@/layouts/DashboardLayoutContent.vue'
import { ref } from 'vue'
import EditSpaceModalWindow from '../edit-space/EditSpaceModalWindow.vue'

export interface Space {
  id: number
  name: string
  type: {
    id: number
    name: string
  }
  price: number
}

const spaces: Space[] = [
  {
    id: 0,
    name: 'Hello',
    type: {
      id: 1,
      name: 'Kitty',
    },
    price: 2036,
  },
  {
    id: 0,
    name: 'Hello',
    type: {
      id: 1,
      name: 'Kitty',
    },
    price: 2036,
  },
  {
    id: 0,
    name: 'Hello',
    type: {
      id: 1,
      name: 'Kitty',
    },
    price: 2036,
  },
  {
    id: 0,
    name: 'Hello',
    type: {
      id: 3,
      name: 'Kitty',
    },
    price: 2036,
  },
]

const spaceForSuspention = ref<Space | null>(null)
const spaceForEdit = ref<Space | null>(null)
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
          <button @click="spaceForSuspention = null" class="button1 button-animation">
            Suspend space
          </button>
        </div>
      </ModalWindow>
      <EditSpaceModalWindow
        v-if="spaceForEdit"
        :space="spaceForEdit"
        @close="spaceForEdit = null"
      />
      <h1 class="Dashboard-headline">Spaces</h1>
      <table class="table">
        <tr class="table__header">
          <th class="table__header-name table__header--short">ID</th>
          <th class="table__header-name table__header--long">Space name</th>
          <th class="table__header-name table__header--medium">Type</th>
          <th class="table__header-name table__header--medium">Price</th>
          <th class="table__header-name table__header--short table__header--button"></th>
        </tr>
        <tr v-for="space in spaces" class="table__row">
          <td class="table__entry">{{ space.id }}</td>
          <td class="table__entry">{{ space.name }}</td>
          <td class="table__entry">{{ space.type.name }}</td>
          <td class="table__entry">{{ space.price }}</td>
          <td class="table__entry">
            <div class="table__button_popup">
              <div class="table__button button3" style="border: 1px solid var(--stroke)">
                <Icon :svg="icons.threeDotes" />
              </div>
              <div class="table__popup">
                <button @click="spaceForEdit = space" class="button2 button-animation">Edit</button>
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
      </table>
    </template>
  </DashboardLayoutContent>
</template>
