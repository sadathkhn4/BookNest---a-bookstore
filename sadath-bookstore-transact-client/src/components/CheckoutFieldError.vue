<script setup lang="ts">
import {defineProps} from "vue";
import type {BaseValidation} from "@vuelidate/core";

const props = defineProps<{
  fieldName: BaseValidation;
}>();
</script>

<style scoped>
.error {
	color: red;
	text-align: right;
  font-weight: lighter;
  font-style: italic;
  font-size: 14px;
  display: block;
}

</style>

<template>
  <template  v-if="fieldName.$error">
    <span class="error">{{ fieldName.$errors[0].$message }}</span>
  </template>
</template>
