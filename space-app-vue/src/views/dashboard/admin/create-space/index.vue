<script setup lang="ts">
import { icons } from '@/assets/icons/icons'
import Icon from '@/components/Icon.vue'
import ModalForm from '@/components/ModalForm.vue'
import { useFetch } from '@/composables/useFetch'
import DashboardLayoutContent from '@/layouts/DashboardLayoutContent.vue'
import apiClient from '@/services/api'
import { mapReactiveFormToQuery, type ReactiveForm } from '@/util/reactiveForm'
import { computed, onMounted, reactive, ref } from 'vue'

export interface SpaceType {
  id: number
  name: string
}

const {
  fetch: fetchContent,
  response: content,
  error: contentError,
  loading: contentLoading,
} = useFetch<{ spaceTypes: SpaceType[] }>({
  client: apiClient,
  path: '/admin/space/form',
  method: 'GET',
})
fetchContent({})

const form: ReactiveForm<'name' | 'typeId' | 'price'> = reactive({
  name: {
    value: '',
    error: '',
  },
  typeId: {
    value: '',
    error: '',
  },
  price: {
    value: '',
    error: '',
  },
})

function selectType(space: SpaceType) {
  form.typeId.value = space.id.toString()
  visibilityOfSpaceTypeSelect.value = false
}

const nameOfSelectedSpaceType = computed(() => {
  if (content.value) {
    const res = content.value.spaceTypes.find((e) => e.id.toString() == form.typeId.value)?.name
    return res ?? 'Select type'
  } else return 'Select type'
})

const visibilityOfSpaceTypeSelect = ref(false)

function validateName() {
  const RegEx = /^[a-zA-Z0-9 ]{5,64}$/
  form.name.error = RegEx.test(form.name.value)
    ? ''
    : 'Required field, enter valid name (latin letters/digits only, 5-64 length)'
  return form.name.error.length == 0
}

function validateTypeId() {
  const RegEx = /^[0-9]+$/
  form.typeId.error = RegEx.test(form.typeId.value) ? '' : 'Required field'
  return form.typeId.error.length == 0
}

function validatePrice() {
  const RegEx = /^[1-9][0-9]*$/
  form.price.error = RegEx.test(form.price.value) ? '' : 'Required field, enter valid price'
  return form.price.error.length == 0
}

const {
  fetch: createSpace,
  error: createSpaceError,
  loading: createSpaceLoading,
} = useFetch<{}>({
  client: apiClient,
  path: '/admin/spaces',
  method: 'POST',
})
const createSpaceSuccess = ref(false)

function handleCreateSpace() {
  //if (validateName() && validateTypeId() && validatePrice()) {
  createSpace({
    payload: mapReactiveFormToQuery(form),
  }).then(() => {
    createSpaceSuccess.value = true
  })
  //}
}

const selectWrap = ref<Element | null>(null)

onMounted(() => {
  document.addEventListener('click', (event: Event) => {
    if (selectWrap.value && event && !selectWrap.value.contains(event.target as Node)) {
      visibilityOfSpaceTypeSelect.value = false
    }
  })
})
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
                v-model="form.name.value"
              />
              <label v-if="form.name.error" class="error-label">{{ form.name.error }}</label>
            </div>
            <div class="input-wrap">
              <label for="typeId" class="label">Type</label>
              <input
                @blur="validateTypeId"
                type="hidden"
                name="typeId"
                id="typeId"
                class="input"
                v-model="form.typeId.value"
              />
              <div
                ref="selectWrap"
                class="select-wrap"
                :class="{
                  'select-wrap--active': visibilityOfSpaceTypeSelect,
                }"
              >
                <div
                  class="input select-input"
                  @click="visibilityOfSpaceTypeSelect = !visibilityOfSpaceTypeSelect"
                >
                  <span>{{ nameOfSelectedSpaceType }}</span>
                  <div class="input--icon">
                    <Icon :svg="icons.chevronDown" />
                  </div>
                </div>
                <ul class="select-option-list" v-if="content">
                  <li
                    v-for="spaceType in content.spaceTypes"
                    class="select-option-item button2"
                    @click="selectType(spaceType)"
                  >
                    {{ spaceType.name }}
                  </li>
                </ul>
              </div>
              <label v-if="form.typeId.error" class="error-label">{{ form.typeId.error }}</label>
            </div>
            <div class="input-wrap">
              <label for="price" class="label">Price</label>
              <input
                @blur="validatePrice"
                type="text"
                name="price"
                id="price"
                class="input"
                v-model="form.price.value"
              />
              <label v-if="form.price.error" class="error-label">{{ form.price.error }}</label>
            </div>
          </div>

          <div class="form-buttons">
            <button
              @click="handleCreateSpace"
              class="button1 button-animation"
              :class="{ 'button--loading': createSpaceLoading }"
            >
              Save changes
            </button>
          </div>
          <label v-if="createSpaceError" class="error-label">{{ createSpaceError.message }}</label>
          <p v-if="createSpaceError && createSpaceError.details" class="error-label">
            {{ createSpaceError.details }}
          </p>
          <label v-else-if="createSpaceSuccess" class="createSpaceSuccess-label"
            >Space successfully added</label
          >
        </div>
      </ModalForm>
    </template>
  </DashboardLayoutContent>
</template>
