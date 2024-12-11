<script setup lang="ts">
import { computed } from "vue";
import ConfirmationTable from "@/components/ConfirmationTable.vue";
import { useOrderDetailsStore } from "@/stores/OrderDetailsStore";
import { useCategoryStore } from "@/stores/category";
import { useCartStore } from "@/stores/cart";

const categoryStore = useCategoryStore();
const cartStore = useCartStore();

const orderDetailsStore = useOrderDetailsStore();
const orderDetails = orderDetailsStore.orderDetails;

const orderDate = computed(function () {
  let date = new Date(orderDetails.order.dateCreated);
  // return date.toLocaleTimeString();
  return date.toUTCString();
});

const ccExpDate = computed(function (): Date {
  return new Date(orderDetails.customer.ccExpDate);
});

const ccExpYear = computed(function (): number {
  return ccExpDate.value.getFullYear();
});

const ccExpMonth = computed(function (): string {
  let month = ccExpDate.value.getMonth() + 1;
  let result = "" + month;
  if (month < 10) {
    result = "0" + month;
  }
  return result;
});
</script>

<style scoped>
.sorry-msg {
  margin: 1em auto;
  text-align: center;
}

.sorry-button {
  display: flex;
  justify-content: center;
  align-items: center;
  margin: 1em auto;
}

a {
  text-decoration: none;
}

.confirmationView {
  width: 100vw;
  background:  var(--primary-color);
  color: var(--neutral-color);
  /*display: flex;*/
  /*flex-direction: column;*/
  /*align-items: flex-start;*/
  /*margin: 1em auto;*/
}

h2 {
  margin: 1em auto;
  text-align: center;
}

.confirmation-page-body {
  display: flex;
  /*align-items: center;*/
  justify-content: space-around;
  width: 100vw;
  /*height: 80%;*/
  padding: 1em;
  padding: 2em 6em;
}

.customer-details {
  /*width: fit-content;*/
  height: min-content;
  /*display: flex;*/
  /*flex-direction: column;*/
  align-items: center;
  color: var(--neutral-color);
  background-color: var(--card-background-color);
  padding: 1em 1em 2em 1em;
}

ul {
  text-align: left;
}

ul > li {
  margin: 0.25em;
}

.right {
  padding-left: 10px;
  text-align: right;
}

.special {
  font-weight: bolder;
}

.center {
  display: flex;
  justify-content: center;
  align-items: center;
  margin: 1em auto;
}

.cont-shop-btn {
  margin-top: 20px;
  margin-right: 20px;
  margin-left: 0 px;
  padding: 10px;
  border: none;
  font-weight: bold;
  cursor: pointer;
}

.cont-shop-btn:hover,
.cont-shop-btn:active{
  background-color: blue;
  color: white;
}
</style>

<template>
  <div class="confirmationView" v-if="!orderDetailsStore.hasOrderDetails()">
    <p class="sorry-msg" style="color: var(--card-background-color)">We are sorry, the order you requested could not be found.</p>
    <div class="sorry-button">
      <router-link :to="{ name: 'home' }">
        <button class="continue-btn">Go to Home Page</button>
      </router-link>
    </div>
  </div>
  <div class="confirmationView" v-else>
    <h2 class="center" style="color:  var(--card-background-color);">Congratulations! Order Successful. Your treasure will arrive soon...</h2>
    <section class="confirmation-page-body">
      <div class="customer-details">
        <h1>Transaction Summary</h1>
        <br />
        <ul>
          <li>
            <span class="special">Confirmation #: </span>
            <span class="right">{{ orderDetails.order.confirmationNumber }}</span>
          </li>
          <li>
            <i class="fa-regular fa-clock"></i>
            <span class="right">{{ orderDate }}</span>
          </li>
        </ul>
        <ul>
          <li>
            <i class="fa-regular fa-user"></i>
            <span class="right">{{ orderDetails.customer.customerName }}</span>
          </li>
          <li>
            <i class="fa-regular fa-envelope"></i>
            <span class="right">{{ orderDetails.customer.email }}</span>
          </li>
          <li>
            <i class="fa-regular fa-location-dot"></i>
            <span class="right">{{ orderDetails.customer.address }}</span>
          </li>
          <li>
            <i class="fa-regular fa-phone"></i>
            <span class="right">{{ orderDetails.customer.phone }}</span>
          </li>
          <li>
            <i class="fa-regular fa-credit-card"></i>
            <span class="right"
            >**** **** **** {{ orderDetails.customer.ccNumber.slice(12) }} [{{
                ccExpMonth
              }}-{{ ccExpYear }}]
            </span>
          </li>
        </ul>
      </div>
      <div class="order-summary-table">
        <confirmation-table> </confirmation-table>
        <router-link :to="'/category/' + cartStore.selectedCategoryName">
          <button class="cont-shop-btn">
             Continue Shopping
          </button>
        </router-link>
      </div>
    </section>
  </div>
</template>