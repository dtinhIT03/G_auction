<template>
  <div>
    <button v-if="showScrollButton" @click="scrollToTop"
      class="fixed bottom-4 right-4 bg-green-500 hover:bg-green-600 text-white p-3 rounded-full shadow-lg  focus:outline-none">
      <svg class="w-6 h-6 pt-1" fill="none" stroke="currentColor" viewBox="0 0 24 24"
        xmlns="http://www.w3.org/2000/svg">
        <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M19 9l-7-7-7 7"></path>
      </svg>
    </button>
  </div>
</template>

<script setup>
import { ref, onMounted, onUnmounted } from 'vue';

const showScrollButton = ref(false);

const checkScrollPosition = () => {
  showScrollButton.value = window.scrollY > 300;
};

const scrollToTop = () => {
  window.scrollTo({ top: 0, behavior: 'smooth' });
};

onMounted(() => {
  window.addEventListener('scroll', checkScrollPosition);
});

onUnmounted(() => {
  window.removeEventListener('scroll', checkScrollPosition);
});
</script>

<style lang="scss" src="./style.scss" scoped />
