<template>
  <div class="z-10">
    <a-card hoverable class="h-auto bg-white shadow-lg rounded-md mt-6">
      <h1 class="text-lg font-bold">Asset Management</h1>
      <div class="flex flex-col items-center justify-center mt-4 space-y-4">
        <router-link to="/user/allProduct" class="w-full">
          <button
            class="w-full flex items-center justify-center p-2 bg-blue-50 text-black font-bold rounded-md hover:bg-teal-200 outline-gray-400 shadow-lg">
            <img src="../../assets/icon/all.svg" class="w-6 h-6 mr-3" />
            All Product
          </button>
        </router-link>
        <router-link to="/user/addProduct" class="w-full">
          <button class="w-full flex items-center justify-center p-2 bg-blue-50 text-black font-bold rounded-md
                    hover:bg-teal-200 outline-gray-400 shadow-lg">
            <img src="../../assets/icon/add.svg" class="w-6 h-6 mr-3" />
            Add Product
          </button>
        </router-link>
      </div>
    </a-card>
  </div>
  <!-- <div class="z-10">
        <a-card hoverable class="h-40 bg-white shadow-lg rounded-md mt-6">
            <h1 class="text-lg font-bold">Search</h1>
            <div class="flex items-center justify-center mt-4">
                <input type="text" class="w-3/4 p-2 border border-gray-300 rounded-md"
                    placeholder="Enter product keyword...">
                <button class="ml-2 p-2 bg-blue-50 hover:bg-teal-200 rounded-full outline-gray-400 shadow-lg">
                    <img src="../../assets/icon/search.svg" alt="Search" class="w-5 h-5" />
                </button>
            </div>
        </a-card>
    </div> -->
  <div class="z-10">
    <a-card hoverable class="h-50 bg-white shadow-lg rounded-md mt-6">
      <h1 class="text-lg font-bold">Filter</h1>
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
</template>

<script setup>
import { ref, watch } from 'vue';
import { useStore } from 'vuex';

const store = useStore();

// const searchKeyword = ref('');
const tags = ref(['ART', 'LICENSE_PLATE', 'VEHICLES', 'ANTIQUES', 'OTHER']);
const selectedTags = ref('');

const filterByTag = (tag) => {
  // if (selectedTags.value.includes(tag)) {
  //     selectedTags.value = selectedTags.value.filter(t => t !== tag);
  // } else {
  //     selectedTags.value.push(tag);
  // }
  if (selectedTags.value === tag) {
    selectedTags.value = '';
  } else {
    selectedTags.value = tag;
  }
  //console.log('Filtering products by tags:', selectedTags.value);
};

watch(selectedTags, (newValue, oldValue) => {
  console.log('Selected tags:', newValue);

  const products = store.getters.getProducts.filter(product => product.category === newValue);
  store.commit('setFilterProducts', products);
  //store.state.filterProducts = products;
  //console.log('Filtered products:', products);
});
</script>
