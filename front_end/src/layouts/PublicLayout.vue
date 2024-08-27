<template>
  <TheHeader />
  <div class="w-full h-full bg-blue-50">
    <div class="p-4">
      <router-view></router-view>
    </div>
  </div>
  <TheChevron />
</template>

<script setup>
import TheHeader from '../components/Header/index.vue';
import TheChevron from '../components/Chevron/index.vue';
</script>
