<template>
  <div class="flex flex-col md:flex-row mt-20 mx-5 space-y-5 md:space-y-0 md:space-x-5">
    <div class="w-full md:w-1/5 mr-4">
      <!-- <div class="z-10">
        <a-card hoverable class="h-40 bg-white shadow-lg rounded-md mt-6">
          <h1 class="text-lg font-bold">Search</h1>
          <div class="flex items-center justify-center mt-4">
            <input type="text" v-model="searchKeyword" class="w-3/4 p-2 border border-gray-300 rounded-md"
              placeholder="Enter product keyword..." />
            <button class="ml-2 p-2 bg-blue-50 hover:bg-teal-200 rounded-full outline-gray-400 shadow-lg">
              <img src="../../../../assets/icon/search.svg" alt="Search" class="w-5 h-5" />
            </button>
          </div>
        </a-card>
      </div> -->
      <div class="z-10">
        <a-card hoverable class="h-50 bg-white shadow-lg rounded-md mt-6">
          <h1 class="text-lg font-bold">Status</h1>
          <div class="flex items-center justify-center flex-wrap mt-4">
            <div v-for="tag in tags" :key="tag" class="mt-1 mx-1">
              <button @click="filterByTag(tag)"
                :class="['p-2 rounded-full shadow-lg', { 'bg-teal-200': selectedTags.includes(tag), 'bg-blue-50': !selectedTags.includes(tag) }]">
                {{ tag }}
              </button>
            </div>
          </div>
        </a-card>
      </div>
    </div>
    <div class="relative w-full md:w-4/5 container border-l bg-white mx-auto p-10 rounded-md shadow-lg mt-6">
      <div class="w-full max-w-md mx-auto">
        <h1 class="text-2xl font-bold text-center text-gray-800">
          All Auctions
        </h1>
        <div class="border-b-2 border-zinc-400 mt-2 mb-8"></div>
      </div>

      <div v-if="loading" class="flex items-center justify-center">
        <a-spin size="large" />
      </div>
      <div class="grid md:grid-cols-2 lg:grid-cols-3 xl:grid-cols-4 gap-4 p-4">
        <button @click="prevSlide"
          class="absolute top-1/2 left-0 transform -translate-y-1/2 bg-slate-300 bg-opacity-50 p-2 rounded-full">
          <img src="../../../../assets/icon/prev-arrow-slide.svg" alt="Previous" class="w-6 h-6" />
        </button>
        <button @click="nextSlide"
          class="absolute top-1/2 right-0 transform -translate-y-1/2 bg-slate-300 bg-opacity-50 p-2 rounded-full">
          <img src="../../../../assets/icon/next-arrow-slide.svg" alt="Next" class="w-6 h-6" />
        </button>
        <div v-for="auction in paginatedAuctions" :key="auction.id"
          class="bg-white shadow-lg rounded-lg relative hover:cursor-pointer transform hover:scale-105 transition duration-300 ease-in-out"
          @click="goToAuction(auction.id)">
          <a-card hoverable>
            <template #cover>
              <img class="h-56" :src="getUrlImage(auction.product.image)" alt="Auction_Image" />
            </template>
            <a-card-meta :title="auction.title" :description="auction.status">
              <template #avatar>
                <a-avatar :src="auction.product.avatarUrl" alt="Auction" />
              </template>
            </a-card-meta>
          </a-card>
        </div>
      </div>
      <div class="flex justify-center mt-4">
        <a-pagination v-model:current="currentPage" :total="totalAuctions" :pageSize="pageSize * 2" />
      </div>
    </div>
  </div>
</template>

<script setup>
import MenuSessionManagement from '../../../../components/MenuSessionManagement/index.vue';
import { ref, computed, watch, onBeforeMount } from 'vue';
import { useRouter } from 'vue-router';
import { useStore } from 'vuex';

const loading = ref(true);

const router = useRouter();
const store = useStore();

const auctions = ref([]);
let totalAuctions = auctions.value.length;

const currentPage = ref(1);
const pageSize = 4;



const paginatedAuctions = computed(() => {
  const start = (currentPage.value - 1) * pageSize * 2;
  const end = start + pageSize * 2;
  return auctions.value.slice(start, end);
});

const tags = ref(['OPENING', 'CLOSED', 'IN_PROGRESS', 'FINISHED']);
const selectedTags = ref([]);

const filterByTag = (tag) => {
  if (selectedTags.value === tag) {
    selectedTags.value = '';
  } else {
    selectedTags.value = tag;
  }

};

watch(selectedTags, (newValue, oldValue) => {
  console.log('Selected tags:', newValue);

  const auctionFilter = store.getters.getAuction.filter(auction => auction.status === newValue);
  store.commit('setFilterAuctions', auctionFilter);
  //store.state.filterProducts = products;
  //console.log('Filtered products:', products);
});




store.watch((state, getters) => getters.getFilterAuctions, (newValue, oldValue) => {
  if (newValue.length === 0) {
    auctions.value = store.getters.getAuction;
  } else {
    auctions.value = newValue;
  }
  totalAuctions = auctions.value.length;
  currentPage.value = 1;
  console.log('AAAAA', auctions.value);
});


watch(auctions, () => {
  totalAuctions = auctions.value.length;
});


const prevSlide = () => {
  if (currentPage.value > 1) {
    currentPage.value--;
  }
};

const nextSlide = () => {
  if (currentPage.value < Math.ceil(totalAuctions.value / (pageSize * 2))) {
    currentPage.value++;
  }
};


const getUrlImage = (image) => {
  return `https://res.cloudinary.com/dorl0yxpe/image/upload/` + image.split(', ')[0];
}

const getUrlAvatar = async (ownerId) => {
  const response = await store.dispatch('getAnotherInfo', ownerId)
  console.log(response.avatar)
  return response.avatar;
}

const goToAuction = (auctionId) => {
  router.push({ name: 'joinAuction', params: { id: auctionId } });
};

const renderAuction = async () => {
  loading.value = true;
  try {
    const response = await store.dispatch('getMyJoined');
    auctions.value = store.getters.getAuction;
    // console.log(auctions.value)
    // for (let auction of auctions) {
    //   const avatarUrl = await getUrlAvatar(auction.product.ownerId);
    //   auction.product.avatarUrl = avatarUrl;
    // }
  } catch (error) {
    console.error(error);
  } finally {
    loading.value = false;
  }
}
onBeforeMount(() => renderAuction());

</script>

<style lang="scss" src="./style.scss" scoped />
