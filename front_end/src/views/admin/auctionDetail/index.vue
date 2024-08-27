<template>
  <div v-show="isVisible" title="Auction Details" @click.self="closeModal"
    class="fixed inset-0 flex items-center justify-center bg-black bg-opacity-50">
    <div class="modal-content relative bg-white p-6 rounded-lg w-full max-w-screen-lg mx-4 mt-28">
      <button @click="closeModal" class="absolute top-4 right-4">
        <img src="../../../assets/icon/cancel.svg" alt="Close" class="w-6 h-6" />
      </button>
      <div v-if="loading" class="flex items-center justify-center w-full h-full">
        <a-spin size="large" />
      </div>
      <div class="flex">
        <div class="w-1/2 flex items-center justify-center space-x-8 h-64 overflow-hidden rounded-md">
          <img v-for="(image, index) in arrayImage" :key="index" :src="image" alt="Session"
            v-show="index === currentImageIndex" class="h-max w-96" />
          <button @click="prevImage"
            class="absolute -left-2 top-1/4 transform -translate-y-1/2 bg-slate-400 bg-opacity-50 p-2 rounded-full">
            <img src="../../../assets/icon/prev-arrow-slide.svg" alt="Previous" class="w-6 h-6" />
          </button>
          <button @click="nextImage"
            class="absolute right-1/2 top-1/4 transform -translate-y-1/2 bg-slate-400 bg-opacity-50 p-2 mr-4 rounded-full">
            <img src="../../../assets/icon/next-arrow-slide.svg" alt="Next" class="w-6 h-6" />
          </button>
        </div>
        <div class="w-1/2 space-y-1 ml-8">
          <p><strong>Product ID:</strong> {{ product?.id }}</p>
          <p><strong>Product Name:</strong> {{ product?.name }}</p>
          <p><strong>Category:</strong> {{ product?.category }}</p>
          <p><strong>Description:</strong> {{ product?.description }}</p>
          <p><strong>Start Bid:</strong> {{ auction.start_bid }}</p>
          <p><strong>Stepping Price:</strong> {{ auction.price_per_step }}</p>
          <span v-if="auction.status !== 'OPENING'">{{ auction.status }}</span>
          <select v-else v-model="selectedStatus" class="border border-gray-300 p-1 rounded-md">
            <option value="OPENING">OPENING</option>
            <option value="CLOSE">CLOSE</option>
            <option value="IN PROGRESS">IN PROGRESS</option>
            <option value="FINISH">FINISH</option>
            <option value="CANCELED">CANCELED</option>
          </select>
        </div>
      </div>
      <div class="flex mt-4">
        <div class="w-1/2 ml-8">
          <p><strong>ID Auction:</strong> {{ auction.id }}</p>
          <p><strong>Created At:</strong> {{ auction.created_at }}</p>
          <p><strong>Confirm Date:</strong> {{ auction.confirm_date }}</p>
          <p><strong>End Registration:</strong> {{ auction.end_registration }}</p>
        </div>
        <div class="w-1/2">
          <p><strong>Auction Name:</strong> {{ auction.title }}</p>
          <p><strong>Description:</strong> {{ auction.description }}</p>
          <p><strong>Start Time:</strong> {{ auction.start_time }}</p>
          <p><strong>End Time:</strong> {{ auction.end_time }}</p>
        </div>
      </div>
      <button v-if="auction.status === 'PENDING'" @click="acceptAuction"
        class="my-4 w-full flex items-center justify-center p-2 bg-blue-50 text-black font-bold rounded-md hover:bg-teal-200 outline-gray-400 shadow-lg">
        <img src="../../../assets/icon/accept.svg" class="w-6 h-6 mr-3" />
        Accept Auction
      </button>
      <button v-if="auction.status !== 'PENDING'" @click="changeStatus"
        class="my-4 w-full flex items-center justify-center p-2 bg-blue-50 text-black font-bold rounded-md hover:bg-teal-200 outline-gray-400 shadow-lg">
        <img src="../../../assets/icon/change-status.svg" class="w-6 h-6 mr-3" />
        Change Status
      </button>
      <button
        class="w-full flex items-center justify-center p-2 bg-blue-50 text-black font-bold rounded-md hover:bg-teal-200 outline-gray-400 shadow-lg">
        <img src="../../../assets/icon/delete.svg" class="w-6 h-6 mr-3" />
        Delete Auction
      </button>
    </div>
  </div>
</template>

<script setup>
import { defineProps, defineEmits, ref, reactive, onMounted, watch } from 'vue';
import productApi from '../../../api/products.js';
import adminApi from '../../../api/admin.js';

const loading = ref(false);

const props = defineProps({
  auction: Object,
  isVisible: Boolean
});

const selectedStatus = ref(props.auction.status);

const emit = defineEmits(['close']);

let product = reactive({});
const arrayImage = ref([]);

const getProduct = async () => {
  try {
    const response = await productApi.getProductById(props.auction.product_id);
    product = response;
    const imgs = product.image;
    arrayImage.value = imgs.split(', ').map(img => `https://res.cloudinary.com/dorl0yxpe/image/upload/` + img.trim());
    console.log(arrayImage.value);
  } catch (error) {
    console.error(error);

  }
};


watch(() => props.auction, (newVal, oldVal) => {
  if (newVal) {
    getProduct();
  }
});

const acceptAuction = async () => {
  loading.value = true;
  try {
    const response = await adminApi.confirmAuction(props.auction.id);
    closeModal();
  } catch (error) {
    console.error(error);
  } finally {
    loading.value = false;
  }
};

const closeModal = () => {
  emit('close');
};

const currentImageIndex = ref(0);
const prevImage = () => {
  if (currentImageIndex.value > 0) {
    currentImageIndex.value--;
  } else {
    currentImageIndex.value = arrayImage.value.length - 1;
  }
};

const nextImage = () => {
  if (currentImageIndex.value < arrayImage.value.length - 1) {
    currentImageIndex.value++;
  } else {
    currentImageIndex.value = 0;
  }
};

const changeStatus = async () => {

};

// onMounted(() => {
//     getProduct();
// });
</script>

<style lang="scss" src="./style.scss" scoped />
