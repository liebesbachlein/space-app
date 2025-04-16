<script setup lang="ts">
import { icons } from '@/assets/icons/icons'
import Icon from '@/components/Icon.vue'
import ModalForm from '@/components/ModalForm.vue'
import DashboardLayoutContent from '@/layouts/DashboardLayoutContent.vue'
import { computed, reactive, ref } from 'vue'

const createSpaceForm = reactive<{
  name: {
    input: string
    error: string
  }
  typeId: {
    input: string
    error: string
  }
  price: {
    input: string
    error: string
  }
}>({
  name: {
    input: '',
    error: '',
  },
  typeId: {
    input: '',
    error: '',
  },
  price: {
    input: '',
    error: '',
  },
})

function validateName() {
  const RegEx = /^[a-zA-Z0-9 ]{5,64}$/
  createSpaceForm.name.error = RegEx.test(createSpaceForm.name.input)
    ? ''
    : 'Required field, enter valid name (latin letters/digits only, 5-64 length)'
  return createSpaceForm.name.error.length == 0
}

function validateTypeId() {
  const RegEx = /^[0-9]+$/
  createSpaceForm.typeId.error = RegEx.test(createSpaceForm.typeId.input) ? '' : 'Required field'
  return createSpaceForm.typeId.error.length == 0
}

function validatePrice() {
  const RegEx = /^[1-9][0-9]*$/
  createSpaceForm.price.error = RegEx.test(createSpaceForm.price.input)
    ? ''
    : 'Required field, enter valid price'
  return createSpaceForm.price.error.length == 0
}

function handleCreateSpace() {
  if (validateName() && validateTypeId() && validatePrice()) {
  }
}

interface Type {
  id: number
  name: string
}

const typeList: Type[] = [
  {
    id: 1,
    name: 'Hello',
  },
  {
    id: 2,
    name: 'He56o',
  },
  {
    id: 3,
    name: '46Hello',
  },
  {
    id: 4,
    name: 'He98llo',
  },
]

function selectType(space: Type) {
  createSpaceForm.typeId.input = space.id.toString()
  typeListVisibility.value = false
}

const selectedTypeName = computed(() => {
  const res = typeList
    .filter((e) => e.id.toString() == createSpaceForm.typeId.input)
    .map((e) => e.name)
  return res.pop() ?? 'Select type'
})

const typeListVisibility = ref(false)
</script>

<template>
  <DashboardLayoutContent>
    <template #main>
      <h1 class="Dashboard-headline">Create a Space</h1>
      <ModalForm header="Create new space" subheader="Make a brand-new space">
        <div class="form">
          <div class="form-inputs">
            <div class="input-wrap">
              <label for="name" class="label">Name</label>
              <input
                @blur="validateName"
                type="text"
                name="name"
                id="name"
                class="input"
                v-model="createSpaceForm.name.input"
              />
              <label v-if="createSpaceForm.name.error" class="error-label">{{
                createSpaceForm.name.error
              }}</label>
            </div>
            <div class="input-wrap">
              <label for="typeId" class="label">Type</label>
              <input
                @blur="validateTypeId"
                type="hidden"
                name="typeId"
                id="typeId"
                class="input"
                v-model="createSpaceForm.typeId.input"
              />
              <div
                class="select-wrap"
                :class="{
                  'select-wrap--active': typeListVisibility,
                }"
              >
                <div class="input select-input" @click="typeListVisibility = !typeListVisibility">
                  <span>{{ selectedTypeName }}</span>
                  <div class="input--icon">
                    <Icon :svg="icons.chevronDown" />
                  </div>
                </div>
                <ul class="select-option-list">
                  <li
                    v-for="typeItem in typeList"
                    class="select-option-item button2"
                    @click="selectType(typeItem)"
                  >
                    {{ typeItem.name }}
                  </li>
                </ul>
              </div>
              <label v-if="createSpaceForm.typeId.error" class="error-label">{{
                createSpaceForm.typeId.error
              }}</label>
            </div>
            <div class="input-wrap">
              <label for="price" class="label">Price</label>
              <input
                @blur="validatePrice"
                type="text"
                name="price"
                id="price"
                class="input"
                v-model="createSpaceForm.price.input"
              />
              <label v-if="createSpaceForm.price.error" class="error-label">{{
                createSpaceForm.price.error
              }}</label>
            </div>
          </div>

          <div class="form-buttons">
            <button @click="handleCreateSpace" class="button1 button-animation">
              Save changes
            </button>
          </div>
        </div>
      </ModalForm>
    </template>
  </DashboardLayoutContent>
</template>
