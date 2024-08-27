<template>
    <div class="relative mt-20">
        <TheCard :category="'ART'" />
        <TheCard :category="'LICENSE_PLATE'" />
        <TheCard :category="'VEHICLES'" />
        <TheCard :category="'ANTIQUES'" />
        <TheCard :category="'OTHER'" />
        
        
    </div>
</template>

<script setup>
import TheCard from '../../../components/CardProduct/index.vue';
</script>
