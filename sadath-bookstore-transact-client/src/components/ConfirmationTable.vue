<script setup lang="ts">
import { useCartStore } from "@/stores/cart";
import { asDollarsAndCents } from "@/main";
import { useOrderDetailsStore } from "@/stores/OrderDetailsStore";
import type { BookItem, OrderDetails } from "@/types";
import { ShoppingCart } from '@/models/ShoppingCart'

const shoppingCart = new ShoppingCart();
const cartStore = useCartStore();
const orderDetailsStore = useOrderDetailsStore();
const orderDetails: OrderDetails = orderDetailsStore.orderDetails;

// A helper function - optional to use
const bookAt = function (orderDetails: OrderDetails, index: number): BookItem {
  return orderDetails.books[index];
};
</script>

<style scoped>
table {
  /*border: 1px black solid;*/
  width: fit-content;
  /*margin-top: 1em;*/
  /*margin-bottom: 1em;*/
  background-color: var(--card-background-color);
  /*padding: 0 0.25em;*/
}

.table-heading {
  background-color: var(--neutral-color);
  color: white;
  font-weight: bold;
}

.table-heading > * {
  background-color: var(--neutral-color);
  padding: 0.5em;
}

td {
  display: table-cell;
  padding: 0.5em 0.5em;
  background-color: var(--card-background-color);
  vertical-align: middle;
}

tr:nth-child(even) > td {
  background-color: #f9f9f3;
}

td:nth-child(1) {
  text-align: left;
}

td:nth-child(2) {
  text-align: center;
}

td:nth-child(3) {
  text-align: right;
}

.special {
  font-weight: bolder;
}

.slant {
  font-style: italic;
}
</style>

<template>
  <table>
    <tr class="table-heading">
      <td>Book</td>
      <td>Quantity</td>
      <td>Amount</td>
    </tr>
    <tr v-for="(item, index) in orderDetails.lineItems" :key="item.bookId">
      <td>
        {{ orderDetails.books[index].title }}
      </td>
      <td>{{ item.quantity }}</td>
      <td>
        {{ asDollarsAndCents(item.quantity * orderDetails.books[index].price) }}
      </td>
    </tr>
    <tr>
      <td></td>
      <td class="slant">Sub-Total</td>
      <td class="slant">{{ asDollarsAndCents(orderDetails.order.amount - 500) }}</td>
    </tr>
    <tr>
      <td></td>
      <td class="slant">Surcharge</td>
      <td class="slant">{{ asDollarsAndCents(500) }}</td>
    </tr>
    <tr>
      <td></td>
      <td class="special">Total</td>
      <td class="special">
        {{ asDollarsAndCents(orderDetails.order.amount) }}
      </td>
    </tr>
  </table>
</template>