<script setup lang="ts">
import { icons } from '@/assets/icons/icons'
import Icon from '@/components/Icon.vue'
import ModalWindow from '@/components/ModalWindow.vue'
import DashboardLayoutContent from '@/layouts/DashboardLayoutContent.vue'
import { computed, reactive, ref } from 'vue'
import type { Space } from '../spaces/index.vue'

const props = defineProps<{
  space: Space
}>()

const emits = defineEmits(['close'])

const editSpaceForm = reactive<{
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
    input: props.space.name,
    error: '',
  },
  typeId: {
    input: props.space.type.id.toString(),
    error: '',
  },
  price: {
    input: props.space.price.toString(),
    error: '',
  },
})

function validateName() {
  const RegEx = /^[a-zA-Z0-9 ]{5,64}$/
  editSpaceForm.name.error = RegEx.test(editSpaceForm.name.input)
    ? ''
    : 'Required field, enter valid name (latin letters/digits only, 5-64 length)'
  return editSpaceForm.name.error.length == 0
}

function validateTypeId() {
  const RegEx = /^[0-9]+$/
  editSpaceForm.typeId.error = RegEx.test(editSpaceForm.typeId.input) ? '' : 'Required field'
  return editSpaceForm.typeId.error.length == 0
}

function validatePrice() {
  const RegEx = /^[1-9][0-9]*$/
  editSpaceForm.price.error = RegEx.test(editSpaceForm.price.input)
    ? ''
    : 'Required field, enter valid price'
  return editSpaceForm.price.error.length == 0
}

function handleEditSpace() {
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

function selectType(type: Type) {
  editSpaceForm.typeId.input = type.id.toString()
  typeListVisibility.value = false
}

const selectedTypeName = computed(() => {
  const res = typeList
    .filter((e) => e.id.toString() == editSpaceForm.typeId.input)
    .map((e) => e.name)
  return res.pop() ?? 'Select type'
})

const typeListVisibility = ref(false)
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
            v-model="editSpaceForm.name.input"
          />
          <label v-if="editSpaceForm.name.error" class="error-label">{{
            editSpaceForm.name.error
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
            v-model="editSpaceForm.typeId.input"
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
          <label v-if="editSpaceForm.typeId.error" class="error-label">{{
            editSpaceForm.typeId.error
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
            v-model="editSpaceForm.price.input"
          />
          <label v-if="editSpaceForm.price.error" class="error-label">{{
            editSpaceForm.price.error
          }}</label>
        </div>
      </div>

      <div class="form-buttons">
        <button @click="emits('close')" class="button1 button-animation">Abort</button>
        <button @click="handleEditSpace" class="button1 button-animation">Save changes</button>
      </div>
    </div>
  </ModalWindow>
</template>
