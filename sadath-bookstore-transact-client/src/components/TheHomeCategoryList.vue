<script setup lang="ts">
import type {CategoryItem} from "@/types";
import { apiUrl } from "@/api"

//let response = await fetch("http://localhost:8080/SadathBookstoreOrder/api/categories/");
let response = await fetch(`${apiUrl}/categories/`);
let data = await response.json();
let categoryList = data as CategoryItem[];
const categoryImageFileName = function (category: CategoryItem): string {
  let name = category.name;
  name = name.replace(/ /g, "-");
  name = name.replace(/'/g, "");
  return `${name}.png`;
};
</script>

<style scoped>
ul {
  display: flex;
  flex-direction: row;
  flex-wrap: wrap;

  gap: 1em;
}

li {
  text-align: center;
  cursor: pointer;
}

li div {
  margin-bottom: -2em;
  padding: 0.5em 0;
  background: rgba(0, 0, 0, 0.5); /* last # is percent opacity */
  color: white;
  transform: translateY(-2.25em);
}

img {
  object-fit: cover;
  width: 250px;
  height: 470px;
  filter: grayscale(60%);
}
</style>

<template>
  <suspense>
    <ul>
      <li v-for="category in categoryList" :key="category.categoryId">
        <router-link :to="'../category/' + category.name">
          <img
              :src="'category-images/' + categoryImageFileName(category)"
              :alt="category.name + ' category'"
          />
          <div>{{ category.name }}</div>
        </router-link>
      </li>
    </ul>
  </suspense>
</template>
