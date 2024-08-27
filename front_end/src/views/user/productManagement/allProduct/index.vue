<template>
  <div class="flex flex-col md:flex-row mt-20 mx-5 space-y-5 md:space-y-0 md:space-x-5">
    <div class="w-full md:w-1/5 mr-4">
      <MenuProductManagement />
    </div>
    <div class="relative w-full md:w-4/5 container border-l bg-white mx-auto p-10 rounded-md shadow-lg mt-6">
      <div class="relative w-full max-w-md mx-auto">
        <h1 class="text-2xl font-bold text-center text-gray-800">
          All Products
        </h1>
        <div class="border-b-2 border-zinc-400 mt-2 mb-8"></div>
      </div>

      <div v-if="loading" class="flex items-center justify-center">
        <a-spin size="large" />
      </div>
      <div class="grid md:grid-cols-2 lg:grid-cols-3 xl:grid-cols-4 gap-4">
        <div v-for="(product, index) in paginatedProducts" :key="index" :product="product" :index="index"
          class=" product-item bg-white shadow-lg rounded-lg transform hover:scale-105 transition duration-300 ease-in-out">
          <a-card hoverable @click="selectProduct(product, index)" class="h-full ">
            <!-- <div class=" flex absolute right-0 top-0 m-4 space-x-2">
              <button @click.stop="editProduct(product)"
                class="flex justify-center items-center w-8 bg-teal-300 text-black hover:bg-teal-400 outline-gray-600 shadow-lg font-bold py-2 rounded">
                <img src="../../../../assets/icon/pencil2.svg" alt="Edit" class="w-4 h-4" />
              </button>
              <button @click.stop="deleteProduct(product)"
                class="flex justify-center items-center w-8 bg-red-300 text-black hover:bg-red-400 outline-gray-600 shadow-lg font-bold py-2 rounded">
                <img src="../../../../assets/icon/delete2.svg" alt="Delete" class="w-4 h-4" />
              </button>
            </div> -->
            <template #cover>
              <img class="h-64"
                :src="`https://res.cloudinary.com/dorl0yxpe/image/upload/` + product.image.split(', ')[0]"
                alt="No Image" />
            </template>
            <a-card-meta :title="product.name" :description="product.category">
              <!-- <template #avatar>
                                <a-avatar :src="product.image" />
                            </template> -->
            </a-card-meta>
          </a-card>
        </div>
      </div>
      <button @click="prevSlide"
        class="absolute left-0 top-1/2 transform -translate-y-1/2 bg-slate-300 bg-opacity-50 p-2 rounded-full">
        <img src="../../../../assets/icon/prev-arrow-slide.svg" alt="Previous" class="w-6 h-6" />
      </button>
      <button @click="nextSlide"
        class="absolute right-0 top-1/2 transform -translate-y-1/2 bg-slate-300 bg-opacity-50 p-2 rounded-full">
        <img src="../../../../assets/icon/next-arrow-slide.svg" alt="Next" class="w-6 h-6" />
      </button>
      <div class="flex justify-center mt-4">
        <a-pagination v-model:current="currentPage" :total="totalProducts" :pageSize="pageSize * 2" />
      </div>
    </div>

    <ProductDetailModal :pos="a" :visible="viewModalVisible" :product="selectedProduct"
      @close="closeProductDetailModal" />

  </div>
</template>

<script setup>
import MenuProductManagement from '../../../../components/MenuProductManagement/index.vue';
import { ref, reactive, computed, watch, defineProps, onBeforeMount } from 'vue';
import ProductDetailModal from '../productDetail/index.vue';
import { useStore } from 'vuex'

const loading = ref(true);

const store = useStore();
const products = ref([]);
// products.value = store.getters.getProducts;
let totalProducts = products.value.length;

const currentPage = ref(1);
const pageSize = 4;
const selectedProduct = ref(null);
const viewModalVisible = ref(false);
const editModalVisible = ref(false);
const a = ref(1000);

const selectProduct = async (product, index) => {
  selectedProduct.value = product;
  a.value = index;
  store.commit('setProductDetail',
    {
      id: product.productId,
      name: product.name,
      category: product.category,
      description: product.description,
      images: product.image,
      owner: product.owner,
    }
  );
  viewModalVisible.value = true;
};

const closeProductDetailModal = () => {
  viewModalVisible.value = false;
  store.commit('setProductDetail',
    {
      id: '',
      name: '',
      category: '',
      description: '',
      images: '',
      owner: '',
    });
};


const paginatedProducts = computed(() => {
  const start = (currentPage.value - 1) * pageSize * 2;
  const end = start + pageSize * 2;
  return products.value.slice(start, end);
});

const prevSlide = () => {
  if (currentPage.value > 1) {
    currentPage.value--;
  }
};
const nextSlide = () => {
  if (currentPage.value < Math.ceil(totalProducts / (pageSize * 2))) {
    currentPage.value++;
  }
};

store.watch((state, getters) => getters.getFilterProducts, (newValue, oldValue) => {
  if (newValue.length === 0) {
    products.value = store.getters.getProducts;
  } else {
    products.value = newValue;
  }
  totalProducts = products.value.length;
  currentPage.value = 1;
  console.log('AAAAA', products.value);
});

watch(products, () => {
  totalProducts = products.value.length;
});

onBeforeMount(async () => {
  loading.value = true;
  try {
    const res = await store.dispatch('getProducts');
    products.value = store.getters.getProducts;
  } catch (error) {
    message.error('Fetch failed');
  } finally {
    loading.value = false;
  }
});
</script>

<style lang="scss" src="./style.scss" scoped />
