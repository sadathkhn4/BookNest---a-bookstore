<script setup lang="ts">
</script>

<style scoped>
footer {
  background: var(--secondary-background-color);
  display: flex;
  gap: 1em;
}

.links,
.social-media-icons {
  display: flex;
  flex-direction: column;
}

.social-media-icons {
  text-align: center;
  gap: 0.25em;
}

</style>

<template>
  <footer class="container">
    <section style="width: 30%">
      <router-link to="/"> <i class="fa-regular fa-copyright"></i>2023 Readable Bookstore</router-link>
    </section>
    <section class="links" style="align-items: center; display: inline-block; width: 40%; text-align: center;">
      <router-link to="/">About</router-link> |
      <router-link to="/">Contact</router-link> |
      <router-link to="/">Directions</router-link>
    </section>
    <section class="social-media-icons" style="display: inline-block; text-align: right; width: 30%">
<!--      <router-link to="/" class="button">Facebook</router-link>-->
<!--      <router-link to="/" class="button">Twitter</router-link>-->

      <router-link to="/" style="margin-left: 10px; width: 42px"><i class="fa-brands fa-facebook-f"></i></router-link>
      <router-link to="/" style="margin-left: 10px; margin-right: 20px; width: 42px"><i class="fa-brands fa-x-twitter"></i></router-link>
    </section>
  </footer>
</template>
