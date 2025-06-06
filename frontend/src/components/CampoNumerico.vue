<!--https://dm4t2.github.io/vue-currency-input/guide.html https://github.com/dm4t2/vue-currency-input?tab=readme-ov-file-->
<script setup>
import { useCurrencyInput } from 'vue-currency-input'
import { watch } from 'vue'

const props = defineProps({
  modelValue: {
    type: Number,
    default: 0
  }
})
const emit = defineEmits(['update:modelValue', 'change'])

const { inputRef, formattedValue, numberValue, setValue } = useCurrencyInput({
  locale: 'es-ES',
  currency: 'EUR',
  currencyDisplay: 'hidden',
  precision: (value) => Number.isInteger(value) ? 0 : 2,
  valueRange: { min: 0 }
})

watch(() => props.modelValue, (value) => setValue(value))
watch(numberValue, (val) => emit('update:modelValue', val))

</script>

<template>
  <input ref="inputRef" v-model="formattedValue" class="form-control text-end" />
</template>
