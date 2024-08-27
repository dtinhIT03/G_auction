<template>
  <div v-if="isVisible" class="fixed inset-0 flex items-center justify-center bg-black bg-opacity-50"
    @click.self="closeModal">
    <div
      class="modal-content relative bg-white p-6 rounded-lg w-5/6 max-w-screen-lg mx-4 mt-28 h-[80vh] overflow-y-auto">
      <button @click="closeModal" class="absolute top-4 right-4">
        <img src="../../../../assets/icon/cancel.svg" alt="Close" class="w-6 h-6" />
      </button>
      <div class="lg:flex">
        <div class="lg:w-1/2 p-4">
          <img src="../../../../assets/images/product.jpg" alt="Session Image" class="modal-image mb-4" />

        </div>
        <div class="lg:w-1/2 p-4 border-l border-gray-300">
          <h2 class="text-2xl font-bold mb-4">{{ session.title }}</h2>
          <p class="text-gray-700 mb-2"><strong>Product ID:</strong> {{ product.id }}</p>
          <p class="text-gray-700 mb-2"><strong>Product Name:</strong> {{ product.name }}</p>
          <p class="text-gray-700 mb-2"><strong>Category:</strong> {{ product.category }}</p>
          <p class="text-gray-700 mb-2"><strong>Description:</strong> {{ session.description }}</p>
          <p class="text-gray-700 mb-2"><strong>Start Bid:</strong> {{ session.start_bid }}</p>
          <p class="text-gray-700 mb-2"><strong>Price per Step:</strong> {{ session.price_per_step }}</p>
          <p class="text-gray-700 mb-2"><strong>Start Time:</strong> {{ session.start_time }}</p>
          <p class="text-gray-700 mb-2"><strong>End Time:</strong> {{ session.end_time }}</p>
        </div>
      </div>
    </div>
  </div>
</template>

<script setup>
import { useStore } from 'vuex';
const store = useStore();

const product = store.getters.getProductDetail;


const props = defineProps({
  isVisible: Boolean,
  session: Object,
});

const emit = defineEmits(['close']);

const closeModal = () => {
  emit('close');
};
</script>

<style lang="scss" src="./style.scss" scoped />
