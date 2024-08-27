<template>
  <div v-if="visible" class="fixed mt-20 z-20 inset-0 bg-gray-800 bg-opacity-50 flex items-center justify-center"
    @click="closeModal">
    <div class="bg-white rounded-lg shadow-lg p-6 w-5/6 relative h-[80vh] overflow-y-auto" @click.stop>
      <button @click="closeModal" class="absolute top-2 right-2 text-gray-500 hover:text-gray-700">
        <img src="../../assets/icon/cancel.svg" alt="Close" class="w-6 h-6" />
      </button>
      <h1 class="text-2xl font-bold text-center text-gray-800">{{ auction.title }}</h1>
      <div class="border-b-2 border-zinc-400 mt-2 mb-8"></div>
      <div class="lg:flex space-x-8">
        <div class="w-full lg:w-1/2 relative">
          <div class="w-full h-auto overflow-hidden rounded-md">
            <div class="flex-1">
              <img v-for="(image, index) in arrayImage" :key="index" :src="image" alt="Image"
                v-show="index === currentImageIndex" class="h-max w-max" />
              <button @click="prevImage"
                class="absolute left-0 top-1/2 transform -translate-y-1/2 bg-white bg-opacity-50 p-2 rounded-full">
                <img src="../../assets/icon/prev-arrow-slide.svg" alt="Previous" class="w-6 h-6" />
              </button>
              <button @click="nextImage"
                class="absolute right-0 top-1/2 transform -translate-y-1/2 bg-white bg-opacity-50 p-2 mr-4 rounded-full">
                <img src="../../assets/icon/next-arrow-slide.svg" alt="Next" class="w-6 h-6" />
              </button>
            </div>
          </div>
        </div>
        <div class="w-full lg:w-1/2 p-4 border-l border-gray-300">
          <p class="text-gray-700 mb-2"><strong>Description: </strong> {{ auction.description }}</p>
          <p class="text-gray-700 mb-2"><strong>Start Bid: </strong> {{ auction.start_bid }}</p>
          <p class="text-gray-700 mb-2"><strong>Stepping Price: </strong> {{ auction.price_per_step }}</p>
          <p class="text-gray-700 mb-2"><strong>End Registration Time: </strong> {{ auction.end_registration }}</p>
          <p class="text-gray-700 mb-2"><strong>Start Time: </strong> {{ auction.start_time }}</p>
          <p class="text-gray-700 mb-5"><strong>End Time: </strong> {{ auction.end_time }}</p>

          <h2 class="text-xl font-bold mb-2 ">Product Information</h2>
          <p class="text-gray-700 mb-1"><strong>Name: </strong>{{ auction.product.name }}</p>
          <p class="text-gray-700 mb-1"><strong>Description: </strong>{{ auction.product.description }}</p>
          <p class="text-gray-700 mb-1"><strong>Category: </strong>{{ auction.product.category }}</p>

          <div v-if="loading" class="flex items-center justify-center">
            <a-spin size="large" />
          </div>
          <button v-if="auction.status === 'OPENING'" @click="handleRegister(auction.id)"
            class="bg-green-500 text-white p-2 rounded mt-8 w-full">
            Register Auction
          </button>
        </div>
      </div>
    </div>
  </div>
</template>

<script setup>
import { defineProps, defineEmits, ref, computed, watch } from 'vue';
import auctionApi from '../../api/auctions';
import { message } from 'ant-design-vue';

const loading = ref(false);

const props = defineProps({
  visible: {
    type: Boolean,
    required: true
  },
  auction: {
    type: Object,
    required: true
  },

});

const emit = defineEmits(['close']);

const arrayImage = ref([]);

const updateArrayImage = () => {
  if (props.auction) {
    arrayImage.value = props.auction.product.image.split(', ').map(img => `https://res.cloudinary.com/dorl0yxpe/image/upload/` + img.trim());
    console.log(arrayImage.value)
  }
}

watch(() => props.auction, updateArrayImage, { immediate: true });

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

const handleRegister = async (id) => {
  loading.value = true;
  try {
    
    const response = await auctionApi.registerAuction(id);
    emit('close');
  } catch (error) {
    message.error("You can not register!")
  } finally {
    loading.value = false;
  }
}

const closeModal = () => {
  emit('close');
};
</script>
