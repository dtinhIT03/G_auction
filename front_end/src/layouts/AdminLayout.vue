<template>
  <TheAdminHeader />
  <div class="w-full h-full ">
    <div class="p-4">
      <router-view></router-view>
    </div>
  </div>
  <TheChevron />
</template>

<script setup>
import TheAdminHeader from '../components/AdminHeader/index.vue';
import TheChevron from '../components/Chevron/index.vue';
</script>
