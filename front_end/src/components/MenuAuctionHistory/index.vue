<template>
  <div class="z-10">
    <a-card hoverable class="h-auto bg-white shadow-lg rounded-md mt-6">
      <h1 class="text-lg font-bold">Auction History</h1>
      <div class="flex flex-col items-center justify-center mt-4 space-y-4">
        <router-link to="/user/allAuctionHistory" class="w-full">
          <button
            class="w-full flex items-center justify-center p-2 bg-blue-50 text-black font-bold rounded-md hover:bg-teal-200 outline-gray-400 shadow-lg">
            <img src="../../assets/icon/all.svg" class="w-6 h-6 mr-3" />
            Auctions Participated
          </button>
        </router-link>
        <router-link to="/user/auctionWon" class="w-full">
          <button class="w-full flex items-center justify-center p-2 bg-blue-50 text-black font-bold rounded-md
                    hover:bg-teal-200 outline-gray-400 shadow-lg">
            <img src="../../assets/icon/success.svg" class="w-6 h-6 mr-3" />
            Auction Won
          </button>
        </router-link>
      </div>
    </a-card>
  </div>
  <!-- <div class="z-10">
    <a-card hoverable class="h-40 bg-white shadow-lg rounded-md mt-6">
      <h1 class="text-lg font-bold">Search</h1>
      <div class="flex items-center justify-center mt-4">
        <input type="text" v-model="searchKeyword" class="w-3/4 p-2 border border-gray-300 rounded-md"
          placeholder="Enter keyword..." />
        <button class="ml-2 p-2 bg-blue-50 hover:bg-teal-200 rounded-full outline-gray-400 shadow-lg">
          <img src="../../assets/icon/search.svg" alt="Search" class="w-5 h-5" />
        </button>
      </div>
    </a-card>
  </div>
  <div class="z-10">
    <a-card hoverable class="bg-white shadow-lg rounded-md mt-6">
      <h1 class="text-lg font-bold">Filter by Date</h1>
      <div class="flex flex-col items-center mt-2 space-y-4">
        <input type="date" v-model="startDate" class="w-full p-2 border border-gray-300 rounded-md"
          placeholder="Start Date">
        <span>TO</span>
        <input type="date" v-model="endDate" class="w-full p-2 border border-gray-300 rounded-md"
          placeholder="End Date">
        <button @click="filterByDate"
          class=" flex items-center justify-center p-2 w-full bg-blue-50 font-bold hover:bg-teal-200 rounded-md outline-gray-400 shadow-lg">
          <img src="../../assets/icon/search.svg" alt="Search" class="w-5 h-5 mr-3" />
          Search
        </button>
      </div>
    </a-card>
  </div> -->
</template>

<script setup>

// const filterByDate = () => {
//   if (!startDate.value || !endDate.value) {
//     alert('Please select both start and end dates');
//     return;
//   }
// };

</script>
