<template>
  <div v-if="visible" class="fixed mt-20 z-20 inset-0 bg-gray-800 bg-opacity-50 flex items-center justify-center"
    @click="closeModal">
    <div class="bg-white rounded-lg shadow-lg p-6 w-5/6 relative h-[80vh] overflow-y-auto" @click.stop>
      <button @click="closeModal" class="absolute top-2 right-2 text-gray-500 hover:text-gray-700">
        <img src="../../assets/icon/cancel.svg" alt="Close" class="w-6 h-6" />
      </button>
      <h1 class="text-2xl font-bold text-center text-gray-800">
        {{ product.name }}
      </h1>
      <div class="border-b-2 border-zinc-400 mt-2 mb-8"></div>
      <div class="lg:flex">
        <div class="w-full lg:w-1/2 relative">
          <div class="w-full h-full overflow-hidden rounded-md">
            <div class="relative mr-10">
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
        <div class="w-full lg:w-1/2">
          <div class="mb-5">
            <label for="category" class="block text-gray-700 mb-3 text-lg font-bold">Category</label>
            <p id="category" class="text-gray-800">{{ product.category }}</p>
          </div>
          <div class="mb-5">
            <label for="description" class="block text-gray-700 mb-3 text-lg font-bold">Description</label>
            <p id="description" class="text-gray-800">
              {{ product.description }}
            </p>
          </div>
          <button @click.stop="toggleFavorite(product)" :disabled="isFavorited(product)"
            class="flex items-center justify-center p-2 rounded mt-4 w-full "
            :class="{ 'bg-pink-200 hover:bg-pink-400': !isFavorited(product), 'bg-gray-200 cursor-not-allowed': isFavorited(product) }">
            <img src="../../assets/icon/like.svg" class="w-6 h-6 mr-1" />
            Add to Favorites
          </button>
        </div>
      </div>
    </div>
  </div>
</template>

<script setup>
import { defineProps, defineEmits, ref, computed, watch } from "vue";
import productApi from "../../api/products";

const props = defineProps({
  visible: {
    type: Boolean,
    required: true,
  },
  product: {
    type: Object,
    required: true,
  },
});

const emit = defineEmits(["close"]);

const arrayImage = ref([]);

const updateArrayImage = () => {
  if (props.product) {
    arrayImage.value = props.product.image
      .split(", ")
      .map(
        (img) =>
          `https://res.cloudinary.com/dorl0yxpe/image/upload/` + img.trim()
      );
    console.log(arrayImage.value);
  }
};

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
  emit("close");
};


const isFavorited = (product) => {
  return product.isFavorite;
}

const toggleFavorite = async (product) => {
  try {
    const response = await productApi.interestProduct(product.productId);
    product.isFavorite = !product.isFavorite;
    product.quantity += 1;
  } catch (error) {
    product.isFavorite = !product.isFavorite;
    console.log(error.response.data.message);
  }
};
</script>
