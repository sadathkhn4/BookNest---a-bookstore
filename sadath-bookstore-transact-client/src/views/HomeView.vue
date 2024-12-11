<script setup lang="ts">
import TheHomeCategoryList from "@/components/TheHomeCategoryList.vue";
</script>

<style scoped>
.home-page {
  display: flex;
}

.cta-button {
  font-size: 25px;
  background-color: #4CAF50;
  color: white;
  padding: 10px 20px;
  border: none;
  cursor: pointer;
  transition: transform 0.3s ease, box-shadow 0.3s ease;
  vertical-align: middle;
}

.cta-button:hover {
  transform: scale(1.1);
  box-shadow: 0px 0px 10px 0px rgba(0, 0, 0, 0.5);
}

.cta-holder{
  display: flex;
  justify-content: center;
  align-items: center;
  margin-top: 50px;
}

.welcome-text {
  width: 20em;
  background-color: rgba(0,0, 0, 0.6);
}

.home-page{
  position: relative;
  background-image: url('/site-images/bg-2.jpg');
  background-size: cover;
  background-repeat: no-repeat;
  background-position: center center;
}

</style>
<template>
  <div class="home-page">
    <section class="welcome-text flow-content container dark-background">
      <h2 style="margin-top: 10px;">Welcome to READABLE Bookstore</h2>
      <p>
        Explore 10,000+ Books Now
      </p>
      <p>Readable - your gateway to literary delight.</p>
      <div class="cta-holder">
        <router-link to="/category/Mythology/">
        <button class="button cta-button">Shop Now</button>
        </router-link>
      </div>

    </section>
    <section class="category-images container">
      <the-home-category-list></the-home-category-list>
    </section>
  </div>
</template>
