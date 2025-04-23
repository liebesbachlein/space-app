<script setup lang="ts">
import { icons } from '@/assets/icons/icons'
import Icon from '@/components/Icon.vue'
import ModalWindow from '@/components/ModalWindow.vue'
import { computed, onMounted, reactive, ref } from 'vue'
import type { Space } from '../spaces/index.vue'
import type { SpaceType } from '../create-space/index.vue'
import { useFetch } from '@/composables/useFetch'
import apiClient from '@/services/api'
import { mapReactiveFormToQuery, type ReactiveForm } from '@/util/reactiveForm'

interface Type {
  id: number
  name: string
}

const props = defineProps<{
  space: Space | null
  spaceTypes: SpaceType[]
}>()

const emits = defineEmits(['abort', 'complete'])

const form: ReactiveForm<'name' | 'typeId' | 'price'> = reactive({
  name: {
    value: props.space?.name ?? '',
    error: '',
  },
  typeId: {
    value: props.space?.typeId.toString() ?? '',
    error: '',
  },
  price: {
    value: props.space?.price.toString() ?? '',
    error: '',
  },
})

function validateName() {
  const RegEx = /^[a-zA-Z0-9 «».,!?''""-]{5,64}$/
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
  fetch: editSpace,
  error: editSpaceError,
  loading: editSpaceLoading,
} = useFetch({
  client: apiClient,
  method: 'PUT',
})

function handleEditSpace() {
  if (validateName() && validateTypeId() && validatePrice() && props.space) {
    editSpace({
      path: '/admin/space/' + props.space.id,
      payload: mapReactiveFormToQuery(form),
    }).then(() => {
      emits('complete')
      editSpaceError.value == null
    })
  }
}

function selectType(type: Type) {
  form.typeId.value = type.id.toString()
  visibilityOfSpaceTypeSelect.value = false
}

const nameOfSelectedSpaceType = computed(() => {
  const res = props.spaceTypes.find((e) => e.id.toString() == form.typeId.value)?.name
  return res ?? 'Select type'
})

const visibilityOfSpaceTypeSelect = ref(false)
const selectWrap = ref<Element | null>(null)

onMounted(() => {
  document.addEventListener('click', (event: Event) => {
    if (selectWrap.value && event) {
      if (selectWrap.value.contains(event.target as Node)) {
        visibilityOfSpaceTypeSelect.value = false
      }
    }
  })
})
</script>

<template>
  <ModalWindow header="Edit the space" subheader="Modify parameters of the space">
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
            <ul class="select-option-list">
              <li
                v-for="spaceType in props.spaceTypes"
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
        <button @click="emits('abort')" class="button1 button-animation">Abort</button>
        <button
          @click="handleEditSpace"
          class="button1 button-animation"
          :class="{ 'button--loading': editSpaceLoading }"
        >
          Save changes
        </button>
      </div>
    </div>
  </ModalWindow>
</template>
