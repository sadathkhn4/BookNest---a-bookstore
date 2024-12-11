<script setup lang="ts">
import type {BookItem} from "@/types";
import {useCartStore} from "@/stores/cart";
const cartStore=useCartStore();

const props = defineProps<{
  book: BookItem;
}>();

const bookImagePrefix = `${import.meta.env.BASE_URL}/book-images`;
const bookImageFileName = function (book: BookItem): string {
  let name = book.title.toLowerCase();
  name = name.replace(/ /g, "-");
  name = name.replace(/'/g, "");
  return `${name}.gif`;
};
</script>
<style scoped>
.book-box {
  display: flex;
  flex-direction: column;
  padding: 1em;
  background-color: var(--card-background-color);

  gap: 0.25em;
}

.book-title {
  font-weight: bold;
}

.book-author {
  font-style: italic;
}

img {
  height: 300px;
  width: 200px;
  object-fit: fill;
}
.book-image{
  position: relative;
}

.book-image .fa-book-open-reader{
  position: absolute;
  top:5px;
  right:5px;
}

.fa-book-open-reader{
  color: var(--card-background-color);
  border-radius: 12px;
  background-color: black;
  border: 2px solid var(--card-background-color);
  padding: 10px;
}

button{
  border: none;
}


</style>

<template>
  <li class="book-box">
    <div class="book-image">
      <img
          :src="`${bookImagePrefix}/${bookImageFileName(book)}`"
          :alt="book.title"
      />
      <i class=" button fa-solid fa-book-open-reader" v-if="book.isPublic"></i>
    </div>
    <div class="book-title">{{ book.title }}</div>
    <div class="book-author">{{ book.author }}</div>
    <div class="book-price">${{ (book.price / 100).toFixed(2) }}</div>
    <button class="button" style="margin-top: 15px;"
            @click="cartStore.addToCart(props.book);"
    >
      <i class="fa-solid fa-cart-shopping"></i>
      Add to Cart
    </button>
  </li>
</template>
