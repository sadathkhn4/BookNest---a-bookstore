<script setup lang="ts">
import { provide } from "vue";
import { RouterView, useRoute } from "vue-router";
import {useCategoryStore} from "@/stores/category";
import type { CategoryItem } from "@/types";
import TheHeader from "@/components/TheHeader.vue";
import TheFooter from "@/components/TheFooter.vue";
// import sourceData from "@/db.json";
const route = useRoute();
const categoryStore= useCategoryStore();
categoryStore.fetchCategories();
// const categoryList = sourceData.categories as CategoryItem[];
//provide("categoryList", categoryList);
</script>

<template>
  <suspense>
    <div id="app">
      <the-header></the-header>
      <router-view :key="route.fullPath"></router-view>
      <the-footer></the-footer>
    </div>
  </suspense>
</template>
