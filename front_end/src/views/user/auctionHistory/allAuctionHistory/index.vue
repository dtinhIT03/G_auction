<template>
  <div class="flex flex-col md:flex-row mt-20 mx-5 space-y-5 md:space-y-0 md:space-x-5">
    <div class="w-full md:w-1/5 mr-4">
      <MenuAuctionHistory />
    </div>
    <div class="relative w-full md:w-4/5 container border-l bg-white mx-auto p-10 rounded-md shadow-lg mt-6">
      <div class="w-full max-w-md mx-auto">
        <h1 class="text-2xl font-bold text-center text-gray-800">
          Auctions you have participated in
        </h1>
        <div class="border-b-2 border-zinc-400 mt-2 mb-8"></div>
      </div>

      <div v-if="loading" class="flex items-center justify-center">
        <a-spin size="large" />
      </div>
      <div class="grid md:grid-cols-2 lg:grid-cols-3 xl:grid-cols-4 gap-4">
        <div v-for="session in paginatedSessions" :key="session.id"
          class="bg-white shadow-lg rounded-lg cursor-pointer transform hover:scale-105 transition duration-300 ease-in-out"
          @click="openModal(session)">

          <a-card hoverable>
            <template #cover>
              <img src="../../../../assets/images/auction.jpg" alt="Session" />
            </template>
            <a-card-meta :title="session.title" :description="session.status">
              <template #avatar>
                <a-avatar :src="session.avatar" />
              </template>
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
        <a-pagination v-model:current="currentPage" :total="totalSessions" :pageSize="pageSize * 2" />
      </div>
      <SessionModal :isVisible="isModalVisible" :session="selectedSession" @close="closeModal" />
    </div>
  </div>
</template>

<script setup>
import MenuAuctionHistory from '../../../../components/MenuAuctionHistory/index.vue';
import SessionModal from '../historyAuctionDetail/index.vue';
import { ref, computed, reactive, onBeforeMount, watch } from 'vue';
import { useStore } from 'vuex';

const store = useStore();

const loading = ref(true);
const isModalVisible = ref(false);
const selectedSession = ref(null);
const sessions = ref([]);
// sessions = store.getters.getSessions;
//sessions.push(...store.state.sessions);


const currentPage = ref(1);
const pageSize = 4;
let totalSessions = sessions.value.length;

const paginatedSessions = computed(() => {
  const start = (currentPage.value - 1) * pageSize * 2;
  const end = start + pageSize * 2;
  return sessions.value.slice(start, end);
});

const prevSlide = () => {
  if (currentPage.value > 1) {
    currentPage.value--;
  }
};

const nextSlide = () => {
  if (currentPage.value < Math.ceil(totalSessions / (pageSize * 2))) {
    currentPage.value++;
  }
};

const openModal = (session) => {
  selectedSession.value = session;
  isModalVisible.value = true;
};

const closeModal = () => {
  isModalVisible.value = false;
};

watch(sessions, () => {
  totalSessions = sessions.value.length;
});

onBeforeMount(async () => {
  loading.value = true;
  try {
    const res = await store.dispatch('getMyAuction');
    sessions.value = store.getters.getSessions;
  } catch (error) {
    message.error('Fetch failed');
  } finally {
    loading.value = false;
  }
});

</script>

<style lang="scss" src="./style.scss" scoped />
