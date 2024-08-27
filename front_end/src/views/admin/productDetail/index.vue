<template>
  <div v-show="isVisible" title="Product Details" @click.self="closeModal"
    class="fixed inset-0 flex items-center justify-center bg-black bg-opacity-50">
    <div class="modal-content relative bg-white p-6 rounded-lg w-full max-w-screen-lg mx-4 mt-28">
      <button @click="closeModal" class="absolute top-4 right-4">
        <img src="../../../assets/icon/cancel.svg" alt="Close" class="w-6 h-6" />
      </button>
      <div class="space-y-2">
        <p><strong>ID:</strong> {{ product.id }}</p>
        <p><strong>Owner:</strong> {{ product.owner }}</p>
        <p><strong>Name:</strong> {{ product.name }}</p>
        <p><strong>Category:</strong> {{ product.category }}</p>
        <p><strong>Description:</strong> {{ product.description }}</p>
        <p><strong>Image:</strong> {{ product.image }}</p>
      </div>
      <div class="flex flex-col items-center justify-center mt-4 space-y-4">
        <button
          class="w-full flex items-center justify-center p-2 bg-blue-50 text-black font-bold rounded-md hover:bg-teal-200 outline-gray-400 shadow-lg">
          <img src="../../../assets/icon/accept.svg" class="w-6 h-6 mr-3" />
          Accept Product
        </button>
        <button
          class="w-full flex items-center justify-center p-2 bg-blue-50 text-black font-bold rounded-md hover:bg-teal-200 outline-gray-400 shadow-lg">
          <img src="../../../assets/icon/delete.svg" class="w-6 h-6 mr-3" />
          Delete Product
        </button>
      </div>
    </div>
  </div>
</template>

<script setup>
import { defineProps, defineEmits } from 'vue';

const props = defineProps({
  product: Object,
  isVisible: Boolean
});

const emit = defineEmits(['close']);

const closeModal = () => {
  emit('close');
};
</script>

<style lang="scss" src="./style.scss" scoped />
