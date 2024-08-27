<template>
  <div v-if="visible" class="fixed inset-0 bg-gray-800 bg-opacity-50 flex items-center justify-center"
    @click="closeModal">
    <div class="bg-white rounded-lg shadow-lg p-6 mt-20 w-5/6 h-[80vh] overflow-y-auto h- relative" @click.stop>
      <button @click="closeModal" class="absolute top-2 right-2 text-gray-500 hover:text-gray-700">
        <img src="../../../../assets/icon/cancel.svg" alt="Close" class="w-6 h-6" />
      </button>
      <h1 class="text-2xl font-bold text-center text-gray-800"> {{ product.name }} </h1>
      <div class="border-b-2 border-zinc-400 mt-2 mb-8"></div>
      <div class="lg:flex justify-center">
        <div class="lg:w-1/2 relative">
          <div class="w-full h-full overflow-hidden rounded-md">
            <div class="mr-10">
              <img v-for="(image, index) in arrayImage" :key="index" :src='image' alt="Image"
                v-show="index === currentImageIndex" class="h-max w-max" />
              <button @click="prevImage"
                class="absolute left-0 top-1/2 transform -translate-y-1/2 bg-white bg-opacity-50 p-2 rounded-full">
                <img src="../../../../assets/icon/prev-arrow-slide.svg" alt="Previous" class="w-6 h-6" />
              </button>
              <button @click="nextImage"
                class="absolute right-2 top-1/2 transform -translate-y-1/2 bg-white bg-opacity-50 p-2 mr-4 rounded-full">
                <img src="../../../../assets/icon/next-arrow-slide.svg" alt="Next" class="w-6 h-6" />
              </button>
            </div>
          </div>
        </div>
        <div class="lg:w-1/2">
          <div class="mb-5">
            <label for="category" class="block text-gray-700 mb-3 text-lg font-bold">Category</label>
            <p id="category" class="text-gray-800">{{ product.category }}</p>
          </div>
          <div class="mb-5">
            <label for="description" class="block text-gray-700 mb-3 text-lg font-bold">Description</label>
            <p id="description" class="text-gray-800">{{ product.description }}</p>
          </div>
          <button @click="createSession" class="w-auto bg-green-500 text-white px-4 py-2 rounded">Create Auction
            Session</button>
        </div>
      </div>
    </div>
  </div>
</template>

<script setup>
import { defineProps, defineEmits, ref, watch } from 'vue';
import { useStore } from 'vuex';
import { useRouter } from 'vue-router';

const router = useRouter();

const createSession = () => {
  router.push('/user/addSession')
}

const props = defineProps({
  visible: {
    type: Boolean,
    required: true
  },
  product: {
    type: Object,
    required: true
  },
  pos: {
    type: Number,
    required: true
  }
});


const emit = defineEmits(['close']);

// const store = useStore();

// let productDetail = store.getters.getProductDetail;
// const images = productDetail.images;
// console.log(images);
// const bum = images.split(', ');
// console.log(bum);
// const productImages = ref([]);

// bum.forEach(image => {
//     productImages.value.push('https://res.cloudinary.com/dorl0yxpe/image/upload/'.concat(image));
// });

// watch(() => productDetail, images, bum, productImages, {immediate : true});

const arrayImage = ref([]);

const updateArrayImage = () => {
  if (props.product) {
    arrayImage.value = props.product.image.split(', ').map(img => `https://res.cloudinary.com/dorl0yxpe/image/upload/` + img.trim());
    console.log(arrayImage.value)
  }
}

watch(() => props.product, updateArrayImage, { immediate: true });

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

const closeModal = () => {
  emit('close');
};
</script>
