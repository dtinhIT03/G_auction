<template>
  <div>
    <div class="relative mt-12">
      <a-carousel autoplay class="mb-8" ref="carouselRef">
        <div class="relative h-72">
          <img src="../../../assets/images/image1.jpg" alt="Auction 1" class="w-full h-full object-cover" />
          <div class="absolute bottom-0 left-0 w-full bg-black bg-opacity-50 text-white p-4">
            <h3 class="text-lg font-bold">Online Auction</h3>
            <p class="text-sm">
              The community is strong, the opportunities are endless, total
              victory for everyone.
            </p>
          </div>
        </div>
        <div class="relative h-72">
          <img src="../../../assets/images/image2.jpg" alt="Auction 2" class="w-full h-full object-cover" />
          <div class="absolute bottom-0 left-0 w-full bg-black bg-opacity-50 text-white p-4">
            <h3 class="text-lg font-bold">Safe Auction</h3>
            <p class="text-sm">
              Ensuring fair competition and the most transparent results.
            </p>
          </div>
        </div>
        <div class="relative h-72">
          <img src="../../../assets/images/image3.jpg" alt="Auction 3" class="w-full h-full object-cover" />
          <div class="absolute bottom-0 left-0 w-full bg-black bg-opacity-50 text-white p-4">
            <h3 class="text-lg font-bold">Convenient Auction</h3>
            <p class="text-sm">
              Simple auction, quick ownership of assets upon successful auction.
            </p>
          </div>
        </div>
        <div class="relative h-72">
          <img src="../../../assets/images/image4.jpg" alt="Auction 4" class="w-full h-full object-cover" />
          <div class="absolute bottom-0 left-0 w-full bg-black bg-opacity-50 text-white p-4">
            <h3 class="text-lg font-bold">Auction Anytime Anywhere</h3>
            <p class="text-sm">
              Full updates on asset types, auctions are held 24/7
            </p>
          </div>
        </div>
      </a-carousel>

      <button @click="prevSlide"
        class="absolute left-0 top-1/2 transform -translate-y-1/2 bg-white bg-opacity-50 p-2 rounded-full">
        <img src="../../../assets/icon/prev-arrow-slide.svg" alt="Previous" class="w-4 h-4 sm:w-6 sm:h-6" />
      </button>
      <button @click="nextSlide"
        class="absolute right-0 top-1/2 transform -translate-y-1/2 bg-white bg-opacity-50 p-2 mr-4 rounded-full">
        <img src="../../../assets/icon/next-arrow-slide.svg" alt="Next" class="w-4 h-4 sm:w-6 sm:h-6" />
      </button>
    </div>

    <div class="flex flex-col sm:flex-row">
      <div class="w-80 lg:w-1/5 md:w-1/5 ml-4 mr-4 mb-10">
        <div class="z-10">
          <a-card hoverable class="h-40 bg-white shadow-lg rounded-md mt-6">
            <h1 class="text-lg font-bold">Search</h1>
            <div class="flex items-center justify-center mt-4">
              <input type="text" class="w-3/4 p-2 border border-gray-300 rounded-md"
                placeholder="Enter product keyword..."
                v-model="search" />
              <button @click="searchProduct" class="ml-2 p-2 bg-blue-50 hover:bg-teal-200 rounded-full outline-gray-400 shadow-lg">
                <img src="../../../assets/icon/search.svg" alt="Search" class="w-5 h-5" />
              </button>
            </div>
          </a-card>
        </div>
        <div class="z-10">
          <a-card hoverable class="h-auto shadow-lg rounded-md mt-6">
            <h1 class="text-lg font-bold mb-4"> News </h1>
            <div class="space-y-4">
              <a-card-meta
                class="shadow-lg p-4 rounded-md transform hover:scale-105 transition duration-300 ease-in-out"
                title="Historic Art Auction Breaks Records"
                description="A Van Gogh painting sells for a record-breaking $150 million at Sotheby">
                <template #avatar>
                  <a-avatar src="https://joeschmoe.io/api/v1/random" />
                </template>
              </a-card-meta>
              <a-card-meta
                class="shadow-lg p-4 rounded-md transform hover:scale-105 transition duration-300 ease-in-out"
                title="Rare Diamond Fetches High Price"
                description="A 50-carat blue diamond sells for $45 million in a heated bidding war">
                <template #avatar>
                  <a-avatar src="https://joeschmoe.io/api/v1/random" />
                </template>
              </a-card-meta>
              <a-card-meta
                class="shadow-lg p-4 rounded-md transform hover:scale-105 transition duration-300 ease-in-out"
                title="World’s Largest Online Auction"
                description="Over 10,000 items, including rare antiques, are auctioned off in a 24-hour online event.">
                <template #avatar>
                  <a-avatar src="https://joeschmoe.io/api/v1/random" />
                </template>
              </a-card-meta>
              <a-card-meta
                class="shadow-lg p-4 rounded-md transform hover:scale-105 transition duration-300 ease-in-out"
                title="Celebrity Memorabilia Auctioned for Charity"
                description=" Items from Hollywood stars raise $5 million for children’s hospitals.">
                <template #avatar>
                  <a-avatar src="https://joeschmoe.io/api/v1/random" />
                </template>
              </a-card-meta>
              <a-card-meta
                class="shadow-lg p-4 rounded-md transform hover:scale-105 transition duration-300 ease-in-out"
                title="Ancient Artifact Unearthed and Sold"
                description="A rare Egyptian artifact from 2000 BC sells for $10 million at an exclusive auction.">
                <template #avatar>
                  <a-avatar src="https://joeschmoe.io/api/v1/random" />
                </template>
              </a-card-meta>
              <a-card-meta
                class="shadow-lg p-4 rounded-md transform hover:scale-105 transition duration-300 ease-in-out"
                title="Tech Startup Auction Nets $1 Billion"
                description="Investors compete in a high-stakes auction for shares in a breakthrough tech company.">
                <template #avatar>
                  <a-avatar src="https://joeschmoe.io/api/v1/random" />
                </template>
              </a-card-meta>
            </div>
          </a-card>
        </div>
      </div>

      <div class="w-80 md:w-4/5 ml-3 rounded-md">
        <div class="max-w-md mx-auto">
          <h1 class="text-2xl font-bold text-center text-gray-800">
            Most Popular Products
          </h1>
          <div class="border-b-2 border-blue-300 mt-2 mb-6"></div>
        </div>

        <div v-if="loadingTop" class="flex items-center justify-center">
          <a-spin size="large" />
        </div>
        <div class="grid sm:grid-cols-1 md:grid-cols-2 lg:grid-cols-3 xl:grid-cols-4 gap-4">
          <div v-for="(product, index) in topProducts" :key="index" class="bg-white shadow-lg rounded-lg p-5">
            <a-card hoverable @click="selectProduct(product, index)"
              class="h-full transform hover:scale-105 transition duration-300 ease-in-out">
              <span
                class="absolute top-4 left-4 flex justify-center items-center bg-white w-auto text-black font-bold py-1 px-1 rounded">
                <img :src="product.isFavorite
                  ? HeartFilled
                  : Heart
                  " alt="Interested" class="w-4 h-4 mr-1" />
                {{ product.quantity }}
              </span>
              <template #cover>
                <img class="h-52 w-52" :src="`https://res.cloudinary.com/dorl0yxpe/image/upload/` +
                  product.image.split(', ')[0]
                  " />
              </template>
              <div class="h-20">
                <a-card-meta :title="product.name" :description="product.description">
                  <template #avatar>
                    <a-avatar :src="product.avatar" />
                  </template>
                </a-card-meta>
              </div>

              <button @click.stop="toggleFavorite(product)" :disabled="isFavorited(product)"
                class="flex items-center justify-center p-2 rounded mt-4 w-full "
                :class="{ 'bg-pink-200 hover:bg-pink-400': !isFavorited(product), 'bg-gray-200 cursor-not-allowed': isFavorited(product) }">
                <img src="../../../assets/icon/like.svg" class="w-6 h-6 mr-1" />
                Add to Favorites
              </button>
            </a-card>
          </div>
        </div>
        <div class="max-w-md mx-auto mt-14">
          <h1 class="text-2xl font-bold text-center text-gray-800">
            New Products
          </h1>
          <div class="border-b-2 border-blue-300 mt-2 mb-6"></div>
        </div>

        <div v-if="loadingNew" class="flex items-center justify-center">
          <a-spin size="large" />
        </div>
        <div class="grid sm:grid-cols-1 md:grid-cols-2 lg:grid-cols-3 xl:grid-cols-4 gap-4">
          <div v-for="(product, index) in newProducts" :key="index" class="bg-white shadow-lg rounded-lg p-5">
            <a-card hoverable @click="selectProduct(product, index)"
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
                <img class="h-52 w-52" alt="sample" :src="`https://res.cloudinary.com/dorl0yxpe/image/upload/` +
                  product.image.split(', ')[0]
                  " />
              </template>
              <div class="h-20">
                <a-card-meta :title="product.name" :description="product.description">
                  <template #avatar>
                    <a-avatar :src="product.avatar" />
                  </template>
                </a-card-meta>
              </div>
              <button @click.stop="toggleFavorite(product)" :disabled="isFavorited(product)"
                class="flex items-center justify-center p-2 rounded mt-4 w-full "
                :class="{ 'bg-pink-200 hover:bg-pink-400': !isFavorited(product), 'bg-gray-200 cursor-not-allowed': isFavorited(product) }">
                <img src="../../../assets/icon/like.svg" class="w-6 h-6 mr-1" />
                Add to Favorites
              </button>
            </a-card>
          </div>
        </div>
      </div>
      <CardDetailModal :visible="viewModalVisible" :product="selectedProduct" @close="closeProductDetailModal" />
    </div>
  </div>
</template>

<script setup>
import TheChevron from "../../../components/Chevron/index.vue";
import CardDetailModal from "../../../components/CardProductDetail/index.vue";
import { reactive, ref, onMounted, watch, onUpdated } from "vue";
import productApi from "../../../api/products.js";
import { useStore } from "vuex";
import Heart from '../../../assets/icon/heart.svg';
import HeartFilled from '../../../assets/icon/heart-filled.svg';
import { useRouter } from 'vue-router';
import { message } from "ant-design-vue";

const router = useRouter();

const store = useStore();
const carouselRef = ref(null);
let topProducts = reactive([]);
let newProducts = reactive([]);
const selectedProduct = ref(null);
const viewModalVisible = ref(false);
const a = ref(1000);
const loadingTop = ref(true);
const loadingNew = ref(true);

const getTopProducts = async () => {
  loadingTop.value = true;
  try {
    const response = await productApi.getTopProducts();
    console.log(response);
    // topProducts = response;
    topProducts.length = 0;
    topProducts.push(...response);
    //topProducts.splice(0, topProducts.length, ...response);
  } catch (error) {
    console.log(error);
  } finally {
    loadingTop.value = false;
  }
};

const getNewProducts = async () => {
  loadingNew.value = true;
  try {
    const response = await productApi.getNewProducts();
    console.log(response.content);
    newProducts.length = 0;
    newProducts.push(...response.content);
    console.log(newProducts);
  } catch (error) {
    console.log(error);
  } finally {
    loadingNew.value = false;
  }
};

const prevSlide = () => {
  if (carouselRef.value) {
    carouselRef.value.prev();
  }
};

const nextSlide = () => {
  if (carouselRef.value) {
    carouselRef.value.next();
  }
};
const selectProduct = async (product, index) => {
  selectedProduct.value = product;
  a.value = index;
  // store.commit('setProductDetail',
  //     {
  //         id: product.productId,
  //         name: product.name,
  //         category: product.category,
  //         description: product.description,
  //         images: product.image,
  //         owner: product.owner,
  //     }
  // );
  viewModalVisible.value = true;
};

const closeProductDetailModal = () => {
  viewModalVisible.value = false;
  // store.commit('setProductDetail',
  //     {
  //         id: '',
  //         name: '',
  //         category: '',
  //         description: '',
  //         images: '',
  //         owner: '',
  //     });
};

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
    const response = await productApi.interestProduct(product.productId);
    product.isFavorite = !product.isFavorite;
    product.quantity += 1;
   } catch (error) {
    message.error("You must login!");
   }
 
 };

 const search = ref('');
 const searchProduct = () => {
  const isLoggedIn = store.getters.getLoginState;

  if (isLoggedIn) {
  router.push({name : 'user-search', query: { keyword : search.value }})
  }else {
    router.push({ name: 'home-search', query: { keyword: search.value } });
  }
}

onMounted(() => {
  getTopProducts();
  getNewProducts();
});

onUpdated(() => {
  listProductFavorite();
});

const listProductFavorite = async() => {
  const res = await productApi.favoriteProduct();
  topProducts.forEach(product => {
    if(res.includes(product.productId)){
      product.isFavorite=true;
      console.log(product.isFavorite)
    }
  })
  newProducts.forEach(product => {
    if(res.includes(product.productId)){
      product.isFavorite=true;
      console.log(product.isFavorite)
    }
  })
  
}
</script>


<style lang="scss" src="./style.scss" scoped />
