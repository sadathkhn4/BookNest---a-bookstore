<script setup lang="ts">
import TheHeaderDropdown from "@/components/TheHeaderDropdown.vue";
import {useCartStore} from "@/stores/cart";

const cartStore = useCartStore();
</script>

<style scoped>
header {
  display: flex;
  flex-direction: row;
  padding: 1em;
  background: var(--secondary-background-color);

  gap: 1em;
}

.text-logo {
  font-family: "Times New Roman", monospace;
  font-size: xxx-large;
  text-decoration: none;
}

.search-bar {
  padding: 0.5em;
}

section {
  margin-top: 40px;
}

button {
  margin-left: 10px;
  border-style: none;
}

.badge {
  padding-left: 10px;
  padding-right: 10px;
  -webkit-border-radius: 10px;
  -moz-border-radius: 10px;
  border-radius: 10px;
}

.label-warning[href],
.badge-warning[href] {
  background-color: #c67605;
}

#lblCartCount {
  font-size: 15px;
  background: #ff0000;
  color: #000000;
  padding: 0 3px;
  vertical-align: top;
  margin-left: 5px;

}


@media (max-width: 600px) {
  header {
    flex-direction: column;
  }
}
</style>

<template>
  <header class="container">
    <section class="bookstore-logo" style="margin-top: 0">
      <router-link
          to="/">
        <!--      exact-active-class="router-link-exact-active"-->
        <!--        .router-link-exact-active {-->
        <!--        /* Your exact-active styles here */-->
        <!--        background-color: var(&#45;&#45;primary-color-dark);-->
        <!--        }-->

        <img
            src="/site-images/Logo.png"
            alt="Readable Bookstore Logo"
            width=120
            height=auto
        />
      </router-link>
    </section>
    <section style=" width: 20%" class="title-and-search-bar">
      <h1>
        <router-link to="/" class="text-logo"> READABLE</router-link>
      </h1>
    </section>
    <section class="header-dropdown-and-cart" style="width: 70%; text-align: right ">
      <form action="category/Kids" style="display: inline-block">
        <input type="text" placeholder="Search" class="search-bar" style="margin-right: 0"/>
        <button type="submit" class="button search-button" style="margin-left:0"><i
            class="fa-solid fa-magnifying-glass"></i></button>
      </form>
      <router-link to="/cart">
        <button class="button"><i class="fa-solid fa-cart-shopping"></i><span class='badge badge-warning' id='lblCartCount'>
          {{ cartStore.count }}
        </span></button>
      </router-link>
      <button class="button">Sadath</button>
      <the-header-dropdown style="margin-left: 10px; display: inline-block"></the-header-dropdown>
    </section>

  </header>
</template>
