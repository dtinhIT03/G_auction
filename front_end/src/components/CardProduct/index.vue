<template>
  <div class="flex mx-5 space-x-5">
    <div class="container border-l bg-white mx-auto p-10 rounded-md shadow-lg mt-6">
      <div class="mb-8">
        <div class="relative w-full max-w-md mx-auto">
          <h1 class="text-2xl font-bold text-center text-gray-800">
            {{ props.category }}
          </h1>
          <div class="border-b-2 border-zinc-400 mt-2 mb-8"></div>
        </div>

        <div v-if="loading" class="flex items-center justify-center">
          <a-spin size="large" />
        </div>
        <div class="grid sm:grid-cols-1 md:grid-cols-2 lg:grid-cols-3 xl:grid-cols-4 gap-4">
          <div v-for="(product, index) in products.content" :key="index"
            class="product-item bg-white shadow-lg rounded-lg h-96">
            <a-card hoverable @click="selectProduct(product)"
              class="h-full transform hover:scale-105 transition duration-300 ease-in-out">
              <span
                class="absolute top-4 left-4 flex justify-center items-center bg-white w-auto text-black font-bold p-2 rounded">
                <img :src="product.isFavorite
                  ? HeartFilled
                  : Heart
                  " alt="Interested" class="w-4 h-4 mr-1" />
                {{ product.quantity }}
              </span>
              <template #cover>
                <img :src="`https://res.cloudinary.com/dorl0yxpe/image/upload/` +
                  product.image.split(', ')[0]
                  " alt="Product" class="w-56 h-56 object-cover" />
              </template>
              <a-card-meta :title="product.name" :description="product.description" class="h-20">
                <template #avatar>
                  <a-avatar :src="product.name" />
                </template>
              </a-card-meta>
              <button @click.stop="toggleFavorite(product)" :disabled="isFavorited(product)"
                class="flex items-center justify-center p-2 rounded mt-4 w-full "
                :class="{ 'bg-pink-200 hover:bg-pink-400': !isFavorited(product), 'bg-gray-200 cursor-not-allowed': isFavorited(product) }">
                <img src="../../assets/icon/like.svg" class="w-6 h-6 mr-1" />
                Add to Favorites
              </button>
            </a-card>
          </div>
        </div>
        <div class="flex justify-center mt-4">
          <a-pagination 
            v-model="currentPage" 
            :page-size="pageSizeRef"
            :total="products.totalElements" 
            show-size-changer
            :page-size-options="['8', '12', '16', '20']"
            @change="handlePageChange" 
            />
      </div> 
        <CardDetailModal :visible="viewModalVisible" :product="selectedProduct" @close="closeProductDetailModal" />
      </div>
    </div>
  </div>
</template>

<script setup>
import { ref, computed, onMounted, watch, reactive, onBeforeMount } from "vue";
import CardDetailModal from "../CardProductDetail/index.vue";
import productApi from "../../api/products";
import Heart from '../../assets/icon/heart.svg';
import HeartFilled from '../../assets/icon/heart-filled.svg';
import { onUpdated } from "vue";
import { useRouter } from "vue-router";
import { message } from "ant-design-vue";

const router = useRouter();
const loading = ref(true);

const icons = ref([
  { src: Heart },
  { src: HeartFilled }
])
const pageSizeRef = ref(8);
const props = defineProps({
  category: String,
  require: true,
});

//art
let products = reactive({
  totalElements: 0,
  content: [],
});

const currentPage = ref(1);
const selectedProduct = ref(null);
const viewModalVisible = ref(false);

const selectProduct = (product) => {
  selectedProduct.value = product;
  viewModalVisible.value = true;
};

const closeProductDetailModal = () => {
  viewModalVisible.value = false;
};


const handlePageChange = async (page,pageSize) => {
  currentPage.value = page;
  pageSizeRef.value=pageSize;
  const pageCurrent = page - 1;
  await renderProduct(pageCurrent,pageSize);
};

const renderProduct = async (pageCurrent,pageSize) => {
  loading.value = true;
  if(!pageSize) pageSize=8;
  try {
    const response = await productApi.getAllProductByCategory(
      props.category,
      pageCurrent,pageSize
    );
    products.content = response.content;
    products.totalElements = response.totalElements;
    // console.log(products);
  } catch (error) {
    console.error(error);
  } finally {
    loading.value = false;
  }
};
onBeforeMount(() =>renderProduct(0))
onUpdated(() => {
  listProductFavorite();
});

const listProductFavorite = async() => {
  const res = await productApi.favoriteProduct();
  console.log(products.content)
  products.content.forEach(product => {
    if(res.includes(product.productId)){
      product.isFavorite=true;
      console.log(product.isFavorite)
    }
  }
  )
}

onUpdated(() => {
  const a =document.getElementsByClassName(`ant-pagination-item ant-pagination-item-${Math.ceil(products.totalElements/pageSizeRef.value)}`)
  if(a.length >0){
    a[0].style.display='none'
  }

})

const checkFavorite = async (product) => {
  try {
    const response = await productApi.checkFavorite(product.productId);
    product.isFavorite = response;
  } catch (error) {
    console.log(error.response.data.message);
  }
}

const isFavorited = (product) => {
  return product.isFavorite;
}

const toggleFavorite = async (product) => {
  //  product.isFavorite = !product.isFavorite;
  //  if (product.isFavorite) {
  //      product.quantity += 1;
  //      await productApi.interestProduct(product.productId);

  //    } else {
  //      product.quantity -= 1;
  //      // await productApi.UnInterestProduct(product.productId);
  //    }

  try {
    product.isFavorite = !product.isFavorite;
    product.quantity += 1;
    const response = await productApi.interestProduct(product.productId);
   
  } catch (error) {  
    product.isFavorite = !product.isFavorite;
    product.quantity -= 1;
    message.error('You must login!')
  }

};
</script>

<style lang="scss" src="./style.scss" scoped />
