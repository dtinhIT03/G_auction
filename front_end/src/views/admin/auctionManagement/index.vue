<template>
  <div class="flex mt-20 mx-5 space-x-5">
    <div class="w-1/5 ml-4 mr-4">
      <div class="z-10">
        <a-card hoverable class="h-40 bg-white shadow-lg rounded-md mt-6">
          <h1 class="text-lg font-bold">Search</h1>
          <div class="flex items-center justify-center mt-4">
            <input type="text" v-model="searchKeyword" class="w-3/4 p-2 border border-gray-300 rounded-md"
              placeholder="Enter session keyword..." />
            <button class="ml-2 p-2 bg-blue-50 hover:bg-teal-200 rounded-full outline-gray-400 shadow-lg">
              <img src="../../../assets/icon/search.svg" alt="Search" class="w-5 h-5" />
            </button>
          </div>
        </a-card>
      </div>
      <div class="z-10">
        <a-card hoverable class="h-50 bg-white shadow-lg rounded-md mt-6">
          <h1 class="text-lg font-bold">Status</h1>
          <div class="flex items-center justify-center flex-wrap mt-4">
            <div v-for="tag in tags" :key="tag" class="mt-1 mx-1">
              <button @click="filterByTag(tag)" :class="[
                'p-2 rounded-full shadow-lg',
                {
                  'bg-teal-200': selectedTags.includes(tag),
                  'bg-blue-50': !selectedTags.includes(tag),
                },
              ]">
                {{ tag }}
              </button>
            </div>
          </div>
        </a-card>
      </div>
    </div>

    <div class="w-4/5 container border-l bg-white mx-auto p-10 rounded-md shadow-lg mt-6">
      <div class="relative w-full">
        <h1 class="text-2xl font-bold text-center text-gray-800">
          Auction List
        </h1>
        <div class="border-b-2 border-zinc-400 mt-2 mb-8"></div>

        <a-table :columns="filteredColumns" :data-source="auctions" :row-key="(record) => record.id"
          :pagination="pagination" @change="handleTableChange">
          <template #bodyCell="{ column, record }">
            <template v-if="column.key">
              <span class="hover:cursor-pointer" @click="selectAuction(record)">
                {{ record[column.dataIndex] }}
              </span>
            </template>
          </template>
        </a-table>
      </div>
    </div>
  </div>

  <AuctionDetail :auction="selectedAuction" :isVisible="isModalVisible" @close="closeModal" />
</template>

<script setup>
import { ref, reactive, computed, onMounted, watch } from "vue";
import AuctionDetail from "../auctionDetail/index.vue";
import adminApi from "../../../api/admin.js";

const isModalVisible = ref(false);
const selectedAuction = ref({});
const isDetailsVisible = ref(false);

const selectAuction = (auction) => {
  selectedAuction.value = auction || {};
  console.log(selectedAuction.value);
  isModalVisible.value = true;
};

const columns = [
  { title: "ID", dataIndex: "auction_id", key: "id" },
  { title: "Title", dataIndex: "title", key: "title" },
  { title: "Description", dataIndex: "description", key: "description" },
  { title: "Created Time", dataIndex: "created_at", key: "createdAt" },
  { title: "Confirm Time", dataIndex: "confirm_date", key: "confirmDate" },
  {
    title: "End Registration Time",
    dataIndex: "end_registration",
    key: "endRegistration",
    visible: isDetailsVisible,

  },
  { title: "Start Time", dataIndex: "start_time", key: "startTime", visible: isDetailsVisible },
  { title: "End Time", dataIndex: "end_time", key: "endTime", visible: isDetailsVisible },
  { title: "Start Bid", dataIndex: "start_bid", key: "startBid", visible: isDetailsVisible },
  { title: "Stepping Price", dataIndex: "price_per_step", key: "pricePerStep", visible: isDetailsVisible, },
  { title: "End Bid", dataIndex: "end_bid", key: "endBid", visible: isDetailsVisible, },
  { title: "Status", dataIndex: "status", key: "status" },
  {
    title: "Product",
    dataIndex: "product_id",
    key: "product",
    visible: isDetailsVisible,
  },
  //   {
  //     title: "Created At",
  //     dataIndex: "createdAt",
  //     key: "createdAt",
  //     visible: isDetailsVisible,
  //   },
  //   {
  //     title: "Confirm Date",
  //     dataIndex: "confirmDate",
  //     key: "confirmDate",
  //     visible: isDetailsVisible,
  //   },
  //   {
  //     title: "End Registration",
  //     dataIndex: "endRegistration",
  //     key: "endRegistration",
  //     visible: isDetailsVisible,
  //   },
];

const auctions = reactive([]);
const pagination = reactive({ total: 0, pageSize: 8, current: 1 });
const searchKeyword = ref("");
const tags = [
  "PENDING",
  "OPENING",
  "CLOSED",
  "IN_PROGRESS",
  "FINISHED",
  "CANCELED",
];
const selectedTags = ref([]);

const handleTableChange = async (pagination) => {
  if (selectedTags.value.length === 0) {
    getAllAuctions(pagination.current);
  } else {
    getAllAuctionsByStatus(selectedTags.value, pagination.current);
  }
};

const getAllAuctions = async (currentPage) => {
  try {
    pagination.current = currentPage;
    const pageNo = pagination.current - 1;
    const response = await adminApi.getAllAuctions(pageNo);
    pagination.total = response.totalElements;

    auctions.length = 0;
    auctions.push(...response.content);

    console.log(pagination.total);
    console.log(auctions);
  } catch (error) {
    console.log(error);
  }
};
const getAllAuctionsByStatus = async (status, currentPage) => {
  try {
    pagination.current = currentPage;
    const pageNo = pagination.current - 1;
    const response = await adminApi.getAllAuctionsByStatus(status, pageNo);

    pagination.total = response.totalElements;
    auctions.length = 0;
    auctions.push(...response.content);
  } catch (error) {
    console.log(error);
  }
};


const filteredColumns = computed(() => {
  return columns.filter(
    (col) => col.visible === undefined || col.visible === isDetailsVisible.value
  );
});


const filterByTag = (tag) => {
  if (selectedTags.value === tag) {
    selectedTags.value = '';
  } else {
    selectedTags.value = tag;
  }

};

watch(selectedTags, (newValue, oldValue) => {
  console.log('Selected tags:', newValue);

  if (newValue.length === 0) {
    getAllAuctions();
  } else {
    const filteredAuctions = getAllAuctionsByStatus(newValue);
    console.log('Filtered auctions:', filteredAuctions);
  }

});

const closeModal = () => {
  isModalVisible.value = false;
};

onMounted(() => {
  getAllAuctions();
});
</script>
