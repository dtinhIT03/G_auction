<template>
  <router-view></router-view>
</template>

<script setup>
import { onMounted } from 'vue';
import stompApi from './api/stomp';

onMounted(() => {
    // TODO: change the way to check if user is logged in
    if (localStorage.getItem('token')) {
        stompApi.setup();
    }
})
</script>