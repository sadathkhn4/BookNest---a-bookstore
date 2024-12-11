<script setup lang="ts">
import CartTable from "@/components/CartTable.vue";
import {useCartStore} from "@/stores/cart";

const cartStore = useCartStore();
const clearCart = function () {
  cartStore.clearCart();
};
</script>
<style scoped>
h1 {
  margin: 1em auto;
  text-align: center;
}

.button-container {
  text-align: center;
  margin-top: 20px;
  margin-bottom: 20px;
}

.cart-page-btn {
  margin-right: 20px;
  margin-left: 20px;
  padding: 10px;
  border: none;
  font-weight: bold;
  cursor: pointer;
}

.cart-page-btn:hover {
  background-color: blue;
  color: white;
}

.cart-btn-sec {
  margin-right: 20px;
  margin-left: 20px;
  border: solid 2px white;
  background-color: var(--primary-color);
  color: var(--primary-background-color);
  padding: 9px;
  cursor: pointer;
}

.cart-btn-sec:hover {
  background-color: blue;
  color: white;
}

.cart-btn-ter {
  color: white;
  cursor: pointer;
  padding: 9px;
  margin-right: 20px;
  margin-left: 20px;
}

.cart-btn-ter:hover {
  color: blue;
}

.cart-text {
  margin: 10px;
  color: var(--primary-background-color);
}

</style>

<template>
  <h1 style="color: white">Cart Page</h1>

  <div v-if="cartStore.count === 0" class="button-container">
    <p class="cart-text">Your Shopping Cart is EMPTY</p>
    <router-link to="/category/Mythology/">
      <button class="cart-btn-sec">Continue Shopping</button>
    </router-link>
  </div>

  <div v-else class="button-container">
    <p class="cart-text">Your Shopping Cart contains {{ cartStore.count }} items</p>
    <a class="cart-btn-ter">Clear Cart</a>
    <button class="cart-btn-sec">Continue Shopping</button>
    <router-link to="/checkout">
      <button class="cart-page-btn">
        Proceed to Checkout
      </button>
    </router-link>
    <cart-table></cart-table>
  </div>
  <!--  <cart-table></cart-table>-->

</template>