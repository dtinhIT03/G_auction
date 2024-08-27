<template>
  <div class="flex  mx-5 space-x-5">

    <div class=" container border-l bg-white mx-auto p-10 rounded-md shadow-lg mt-6">
      <div class="mb-8">
        <div class="relative w-full max-w-md mx-auto">
          <h1 class="text-2xl font-bold text-center text-gray-800">
            {{ props.statusAuction }}
          </h1>
          <div class="border-b-2 border-zinc-400 mt-2 mb-8"></div>
        </div>
        
          <div v-if="loading" class="flex items-center justify-center">
            <a-spin size="large" />
          </div>
          <div class="grid sm:grid-cols-1 md:grid-cols-2 lg:grid-cols-3 xl:grid-cols-4 gap-4">
          <div v-for="(auction, index1) in auctions.content" :key="index1"
            class="product-item bg-white shadow-lg rounded-lg">
            <a-card hoverable @click="selectAuction(auction)"
              class="h-96 transform hover:scale-105 transition duration-300 ease-in-out">

              <template #cover>
                <img :src="auction.productImage" alt="product image" class="w-60 h-60 object-cover" />
              </template>
              <a-card-meta :title="auction.title" :description="auction.description">
                <template #avatar>
                  <a-avatar :src="auction.name" />
                </template>
              </a-card-meta>
            </a-card>
          </div>
        </div>
        <div class="flex justify-center mt-4">
          <a-pagination 
            v-model="currentPage" 
            :page-size="pageSizeRef"
            :total="auctions.totalElements" 
            show-size-changer
            :page-size-options="['8', '12', '16', '20']"
            @change="handlePageChange" 
            />
        </div>
        <CardDetailModal :visible="viewModalVisible1" :auction="selectedAuction" @close="closeProductDetailModal1" />
      </div>


    </div>


  </div>
</template>

<script setup>
import { ref, computed, onMounted, watch, reactive, onBeforeMount } from 'vue';
import CardDetailModal from '../CardAuctionDetail/index.vue';
import auctionApi from '../../api/auctions';
import productApi from '../../api/products';

const loading = ref(true);

const props = defineProps({
  statusAuction: String,
  require: true,
});


//art
let auctions = reactive(
  {
    totalElements: 0,
    content: [],
  }
)

const currentPage = ref(1);
const selectedAuction = ref(null);
const viewModalVisible1 = ref(false);
const pageSizeRef = ref(8);

const selectAuction = (auction) => {
  selectedAuction.value = auction;
  viewModalVisible1.value = true;
  console.log(selectedAuction.value)
};

const closeProductDetailModal1 = () => {
  viewModalVisible1.value = false;
};

const handlePageChange = async (page,pageSize) => {
  currentPage.value = page;
  pageSizeRef.value=pageSize;
  const pageCurrent = page - 1;
  await renderAuction(pageCurrent,pageSize);
};


const renderAuction = async (pageCurrent,pageSize) => {
  loading.value = true;
  if(!pageSize) pageSize =8;
  try {
    const response = await auctionApi.getAllAuctionByStatus(props.statusAuction, pageCurrent,pageSize);
    pageSizeRef.value=pageSize;
    auctions.content = response.content;
    auctions.totalElements = response.totalElements;
    console.log(response)
    const promises = [];
    for (let auction of auctions.content) {
      promises.push(productApi.getProductById(auction.product_id).then((response) => {
        auction.product = response;
        auction.productImage = `https://res.cloudinary.com/dorl0yxpe/image/upload/` + response.image.split(', ')[0];
      }));
    }
    await Promise.all(promises);
  } catch (error) {
    console.error(error);
  } finally {
    loading.value = false;
  }

}
onBeforeMount(() => renderAuction(0))

</script>

<style lang="scss" src="./style.scss" scoped />
