<script setup lang="ts">

import {useCategoryStore} from "@/stores/category";
const categoryStore = useCategoryStore();

</script>

<style scoped>
.header-dropdown {
  position: relative;
}

.categories-button {
  margin-top: 0.25em;
  background-color: #031f82;
  border-style: none;
}

.header-dropdown ul {
  position: absolute;
  right: 0;
  list-style: none;
  background-color: white;
  color: black;
  max-height: 0;
  overflow: hidden;
  min-width: 6.8em;
  transition: max-height 0.3s ease-in-out;
}

.header-dropdown li {
  padding: 0 0.5em;
}

.header-dropdown a {
  color: black;
  text-decoration: none;
}

.header-dropdown li:hover {
  background: lightgray;
}

.header-dropdown:hover ul {
  max-height: 200px;
}

</style>

<template>
  <div class="header-dropdown">
    <button class="button categories-button">Categories</button>
    <ul class="dropdown-content">
      <li v-for="category in categoryStore.categoryList" :key="category.categoryId">
        <router-link :to="'/category/' + category.name">
          <div class="list-item-content">
            {{ category.name }}
          </div>
        </router-link>
      </li>
    </ul>

  </div>
</template>
