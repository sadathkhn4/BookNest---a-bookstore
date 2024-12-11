<script setup lang="ts">
import { reactive, ref } from "vue";
import useVuelidate from "@vuelidate/core";
import {
  email,
  helpers,
  maxLength,
  minLength,
  required,
} from "@vuelidate/validators";
import { useCartStore } from "@/stores/cart";
import { isCreditCard, isMobilePhone } from "@/validators";
import CheckoutFieldError from "@/components/CheckoutFieldError.vue";
import router from "@/router";
import { asDollarsAndCents } from "@/main";
import type { OrderDetails, ServerErrorResponse } from "@/types";
import { useCategoryStore } from "@/stores/category";
import {useOrderDetailsStore} from "@/stores/OrderDetailsStore";
import { ShoppingCart } from '@/models/ShoppingCart'

const cartStore = useCartStore();
const shoppingCart = new ShoppingCart();
const orderDetailsStore = useOrderDetailsStore();
const categoryStore = useCategoryStore();
const cart = cartStore.cart;
const defaultServerErrorMessage =
    "An unexpected error occurred, please try again.";
const serverErrorMessage = ref(defaultServerErrorMessage);
const months: string[] = [
  "January",
  "February",
  "March",
  "April",
  "May",
  "June",
  "July",
  "August",
  "September",
  "October",
  "November",
  "December",
];

function generateYears() {
  // Get current year
  const today = new Date();
  const currentYear = today.getFullYear();
  const years = [currentYear];
  // Get next 15 years
  for (let i = 1; i <= 15; i++) {
    years.push(currentYear + i);
  }
  return years;
}
const years: number[] = generateYears();

const form = reactive({
  name: "",
  address: "",
  phone: "",
  email: "",
  ccNumber: "",
  ccExpiryMonth: new Date().getMonth() + 1,
  ccExpiryYear: new Date().getFullYear(),
  checkoutStatus: "",
});

const rules = {
  name: {
    required: helpers.withMessage("Please provide a name.", required),
    minLength: helpers.withMessage(
        "Name must have at least 4 letters.",
        minLength(4)
    ),
    maxLength: helpers.withMessage(
        "Name can have at most 45 letters.",
        maxLength(45)
    ),
  },

  address: {
    required: helpers.withMessage("Please provide an address.", required),
    minLength: helpers.withMessage(
        "Address must have at least 4 letters.",
        minLength(4)
    ),
    maxLength: helpers.withMessage(
        "Address can have at most 45 letters.",
        maxLength(45)
    ),
  },

  phone: {
    required: helpers.withMessage("Please provide a phone number.", required),
    isMobilePhone: helpers.withMessage(
        "Please provide a valid phone number.",
        isMobilePhone
    ),

  },

  email: {
    required: helpers.withMessage("Please provide a email.", required),
    email: helpers.withMessage(
        "Please provide a valid email.",
        email
    ),

  },

//   ccNumber: {
//     required: helpers.withMessage("Please provide a credit card number.", required),
//     isCreditCard: helpers.withMessage(
//         "Please provide a valid credit card number.",
//         isCreditCard
//     ),
//
//   },
//
//   //   TODO: Add more validations for these and other fields that need more validation.
// };

  ccNumber: {
    required: helpers.withMessage("Please provide a credit card number.", required),
    ccNumber: helpers.withMessage(
        "Please provide a valid credit card number.",
        (value: string) => !helpers.req(value) || isCreditCard(value)
    ),
  },
  ccExpiryMonth: {},
  ccExpiryYear: {},
};
const v$ = useVuelidate(rules, form);

async function submitOrder() {
  console.log("Submit order");
  const isFormCorrect = await v$.value.$validate();
  if (!isFormCorrect) {
    form.checkoutStatus = "ERROR";
  } else {
    try {
      form.checkoutStatus = "PENDING";
      serverErrorMessage.value = defaultServerErrorMessage;

      const placeOrderResponse: OrderDetails | ServerErrorResponse =
          await cartStore
              .placeOrder({
                name: form.name,
                address: form.address,
                phone: form.phone,
                email: form.email,
                ccNumber: form.ccNumber,
                ccExpiryMonth: form.ccExpiryMonth,
                ccExpiryYear: form.ccExpiryYear,
              })

      if ("error" in placeOrderResponse) {
        form.checkoutStatus = "SERVER_ERROR";
        serverErrorMessage.value = placeOrderResponse.message
        console.log("Error placing order", placeOrderResponse);
      } else {
        form.checkoutStatus = "OK";
        orderDetailsStore.setOrderDetails(placeOrderResponse as OrderDetails);
        await router.push({name: "confirmation-view"});
      }

    } catch (e) {
      form.checkoutStatus = "SERVER_ERROR";
      serverErrorMessage.value = defaultServerErrorMessage;
      console.log("Error placing order", e);
    }
  }
}

/* NOTE: For example yearFrom(0) == <current_year> */
function yearFrom(index: number) {
  return new Date().getFullYear() + index;
}
</script>

<style scoped>
/* TODO: Adapt these styles to your page */
.checkout-page {
  background: var(--primary-color);
  color: black;
}


.checkout-page-body-empty {
  text-align: center;
  padding: 1em;
}

.checkout-page-body {
  display: flex;
  padding: 1em;
  width: 50%; /* Set a specific width for the block */
  margin: 0 auto;
}

form {
  text-align: left;
  display: block;
  flex-direction: column;
  width: 50%; /* Set a specific width for the block */
  margin: 0 auto;
}

form > div > div {
  display: flex;
  justify-content: flex-end;
  margin-bottom: 0.5em;
  margin-top: 1em;
}

form > div > div > input,
form > div > div > select {
  background-color: var(--card-background-color);
  margin-left: 0.5em;
  border: none;
}

form > .error {
  color: red;
  text-align: right;
}

.checkoutStatusBox {
  margin: 1em;
  padding: 1em;
  text-align: center;
}

.btn {
  background-color: var(--primary-background-color);
  color: var(--primary-color);
  text-decoration: none;
  border: none;
  cursor: pointer;
  padding: 6px 3px;
}

.btn:hover {
  background-color: var(--primary-color-dark);
  color: var(--primary-background-color);
}

.error-text {
  text-align: center;
  margin: 10px;
  color: var(--card-background-color)
}

.checkout-details {
  margin: 20px;
  text-align: right;
  color: var(--card-background-color);
  align-items: center;
}

.input-field{
  color: var(--card-background-color);
}

.input-text{
  color: var(--primary-color);
}

.button{
  border: solid 2px;
  margin-top: 20px;
  margin-left: 50px;
}

.details-box{
  width: 50%; /* Set a specific width for the block */
  margin: 0 auto;
}

</style>

<template>
  <div class="checkout-page">
    <!-- TODO: Add an HTML section to display when checking out with an empty cart -->
    <section class="checkout-page-body-empty" v-if="cart.empty">
      <div class="error-text">ERROR: Your Shopping Cart is Empty</div>
      <div>
        <router-link to="/category/Mythology" style="text-decoration: none">
          <button class="btn"> Continue Shopping</button>
        </router-link>
      </div>
    </section>


    <section class="checkout-page-body" v-if="!cart.empty">

      <form @submit.prevent="submitOrder">
        <div class="input-field">
          <div>
            <label for="name">Name</label>
            <input class="input-text"
                type="text"
                size="20"
                id="name"
                name="name"
                v-model.lazy="v$.name.$model"
            />
          </div>
          <CheckoutFieldError :field-name="v$.name"/>
        </div>

        <!-- TODO: Add address input and validation messages -->
        <div class="input-field">
          <div>
            <label for="address">Address</label>
            <input class="input-text"
                type="text"
                size="20"
                id="address"
                name="address"
                v-model.lazy="v$.address.$model"
            />
          </div>
          <CheckoutFieldError :field-name="v$.address"/>
        </div>

        <div class="input-field">
          <div>
            <label for="phone">Phone</label>
            <input
                class="input-text"
                type="text"
                size="20"
                id="phone"
                name="phone"
                v-model.lazy="v$.phone.$model"
            />
          </div>
          <CheckoutFieldError :field-name="v$.phone"/>
        </div>

        <div class="input-field">
          <div>
            <label for="email">Email</label>
            <input class="input-text"
                type="text"
                size="20"
                id="email"
                name="email"
                v-model.lazy="v$.email.$model"
            />
          </div>
          <CheckoutFieldError :field-name="v$.email"/>
        </div>

        <div class="input-field">
          <div>
            <label for="ccNumber">Credit card</label>
            <input class="input-text"
                type="text" size="20" id="ccNumber" name="ccNumber"
                   v-model.lazy="v$.ccNumber.$model"
            />
          </div>
          <CheckoutFieldError :field-name="v$.ccNumber"/>
        </div>
        <!-- TODO: Add credit card validation message(s) -->

        <div class="input-field" style="display: flex; justify-content: flex-end">

          <div>
            <label>Exp Date</label>
            <select v-model="v$.ccExpiryMonth.$model" class="input-text">
              <option class="input-text"
                  v-for="(month, index) in months"
                  :key="index"
                  :value="index + 1"
              >
                {{ month }} ({{ index + 1 }})
              </option>
            </select>
          </div>

          <div>
            <select v-model="v$.ccExpiryYear.$model" class="input-text">
              <option class="input-text"
                  v-for="(index) in 16"
                  :key="index"
              >
                {{ yearFrom(index) - 1 }}
              </option>
            </select>
          </div>
        </div>

        <!-- Replace the code for both month and year dropdowns with this -->
<!--        <div class="input-field" style="display: flex">-->
<!--          <label>Exp Date:</label>-->
<!--          <select v-model="v$.ccExpiryMonth.$model">-->
<!--            <option v-for="(month, index) in months" :key="index" :value="index + 1">-->
<!--              {{ month }} ({{ index + 1 }})-->
<!--            </option>-->
<!--          </select>-->
<!--          <select v-model="v$.ccExpiryYear.$model">-->
<!--            <option v-for="(year, index) in years" :key="index" :value="year">-->
<!--              {{ year }}-->
<!--            </option>-->
<!--          </select>-->
<!--        </div>-->

<!--        <div class="input-field" style="display: flex">-->
<!--          <div>-->
<!--            <label>Exp Date</label>-->
<!--            <select v-model="v$.ccExpiryMonth">-->
<!--              <option v-for="(month, index) in months" :key="index" :value="index + 1">-->
<!--                {{ month }} ({{ index + 1 }})-->
<!--              </option>-->
<!--            </select>-->
<!--          </div>-->

<!--          <div>-->
<!--            <label></label>-->
<!--            <select v-model="v$.ccExpiryYear">-->
<!--&lt;!&ndash;              <option v-for="index in 16" :key="index" :value="yearFrom(index)">&ndash;&gt;-->
<!--&lt;!&ndash;                {{ yearFrom(index) }}&ndash;&gt;-->
<!--&lt;!&ndash;              </option>&ndash;&gt;-->
<!--              <option> {{5}}</option>-->
<!--            </select>-->
<!--          </div>-->
<!--        </div>-->

        <!-- TODO (style): Use a single label for both month and date and put the on the same line. -->
        <!-- TODO (style): For example: Exp Date {Month} {Year}, with space between month/year selectors. -->

        <input
            type="submit"
            name="submit"
            class="button"
            :disabled="form.checkoutStatus === 'PENDING'"
            value="Complete Purchase"
        />
        <!-- TODO (style): The submit button should not take up the entire width of the form. -->
        <!-- TODO (style): The submit button should be styled consistent with your own site. -->
      </form>
      <!-- TODO (style): Fix error message placement so they nearer to the correct fields. -->
      <!-- TODO (style): HINT: Use another <div> and label, input, and error, and use flexbox to style. -->


      <!-- TODO: Display the cart total, subtotal and surcharge. -->
      <div class="details-box" style="display: flex; flex-direction: column; ">
        <div class="checkout-details" style="text-align: right">
          <p>Sub-Total: ${{ cartStore.cart.subtotal / 100 }}</p>
          <p>Surcharge: {{ shoppingCart.surcharge }}</p>
          <p>Total: ${{ Math.round(cartStore.cart.subtotal + 500) / 100 }}</p>
        </div>
      </div>

      <section v-show="form.checkoutStatus !== ''" class="checkoutStatusBox" style="color:var(--card-background-color); font-weight: bold; font-size: large">
        <div v-if="form.checkoutStatus === 'ERROR'">
          Error: Please fix the problems above and try again.
        </div>

        <div v-else-if="form.checkoutStatus === 'PENDING'">Processing...</div>

        <div v-else-if="form.checkoutStatus === 'OK'">Order placed...</div>

        <div v-else>{{ serverErrorMessage }}</div>
      </section>
    </section>
  </div>
</template>
