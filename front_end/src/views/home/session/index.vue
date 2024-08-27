<template>
    <div class="relative mt-20">
        <TheCard :statusAuction="'IN_PROGRESS'" />
        <TheCard :statusAuction="'OPENING'" />
    </div>
</template>

<script setup>
import TheCard from '../../../components/CardAuction/index.vue';
</script>
