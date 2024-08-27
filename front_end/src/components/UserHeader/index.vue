<template>
  <div class="w-full h-full">
    <div class="header fixed left-0 top-0 w-full z-50 bg-green-200 h-20 flex items-center">
      <div class="w-1/10 flex sm:hidden items-center justify-center ml-4">
        <span @click="showDrawer">
          <img src="../../assets/icon/dashboard.svg" alt="Dashboard" class="w-6 h-6" />
        </span>
      </div>
      <div class="w-8/10 flex">
        <img @click="goToHome" src="../../assets/images/logo.png" alt="Logo" class="mt-2 ml-6 h-20 w-30">
        <div class="flex items-center justify-center space-x-6 ml-32">
          <ul class="hidden md:block navbar-item font-bold cursor-pointer">
            <router-link to="/user/default" active-class="text-green-600">
              <div class="w-full h-full p-5 flex items-center justify-center">
                <img src="../../assets/icon/home.svg" class="h-5 w-5 inline-block mr-2" />
                Home
              </div>
            </router-link>
          </ul>

          <a class="hidden md:block ant-dropdown-link font-bold cursor-pointer hover:text-green-600">
            <router-link to="/user/product" class="block w-full h-full hover:text-green-600 rounded"
              active-class="text-green-600">
              <div class="w-full h-full p-5 flex items-center justify-center">
                Auction Product
                <!-- <img src="../../assets/icon/chevron-down.svg" alt="Chevron Down" class="h-5 w-5" /> -->
              </div>
            </router-link>
          </a>
          <a class="hidden md:block ant-dropdown-link font-bold cursor-pointer hover:text-green-600">
            <router-link to="/user/session" class="block w-full h-full hover:text-green-600 rounded"
              active-class="text-green-600">
              <div class="w-full h-full p-5 flex items-center justify-center">
                Auction Session
                <!-- <img src="../../assets/icon/chevron-down.svg" alt="Chevron Down" class="h-5 w-5" /> -->
              </div>
            </router-link>
          </a>
          <a class="hidden xl:block ant-dropdown-link font-bold cursor-pointer hover:text-green-600">
            <router-link to="/user/news" class="block w-full h-full hover:text-green-600 rounded"
              active-class="text-green-600">
              <div class="w-full h-full p-5 flex items-center justify-center">
                News
                <!-- <img src="../../assets/icon/chevron-down.svg" alt="Chevron Down" class="h-5 w-5" /> -->
              </div>
            </router-link>
          </a>
          <ul class="hidden xl:block navbar-item font-bold cursor-pointer">
            <router-link to="/user/introduction" class="block w-full h-full p-5 hover:text-green-600 rounded"
              active-class="text-green-600">
              <div class="w-full h-full flex items-center justify-center">Introduction</div>
            </router-link>
          </ul>
          <ul class="hidden xl:block navbar-item font-bold cursor-pointer">
            <router-link to="/user/contact" class="block w-full h-full hover:text-green-600 rounded"
              active-class="text-green-600">
              <div class="w-full h-full p-5 flex items-center justify-center">Contact</div>
            </router-link>
          </ul>
        </div>
      </div>

      <!-- avatar and dropdown-->
      <div class="relative w-1/10 flex items-center m-auto">
        <div ref="dropdownTrigger" @click="toggleDropdown" class="flex items-center hover:cursor-pointer">
          <a-avatar class="bg-green-400">
            <img src="../../assets/icon/user.svg" alt="User" class="w-6 h-6" />
          </a-avatar>
          <img src="../../assets/icon/chevron-down.svg" alt="V" class="h-5 w-5" />
        </div>
        <div v-if="showDropdown"
          class="absolute right-5 top-10 w-56 bg-white border border-gray-200 rounded shadow-lg z-20">
          <a-menu>
            <a-menu-item @click="profileManagement">
              <a class="font-bold flex items-center" href="javascript:;">
                <img src="../../assets/icon/profile.svg" class="h-5 w-5 inline-block mr-2" />
                Profile
              </a>
            </a-menu-item>
            <a-menu-item @click="auctionHistory">
              <a class="font-bold flex items-center">
                <img src="../../assets/icon/history.svg" class="h-5 w-5 inline-block mr-2" />
                Auction History
              </a>
            </a-menu-item>
            <a-menu-item @click="auctionManagement">
              <a class="font-bold flex items-center">
                <img src="../../assets/icon/auction-management.svg"
                  class="h-5 w-5 inline-block mr-2" />
                Registered Auctions
              </a>
            </a-menu-item>
            <a-menu-item @click="productManagement">
              <a class="font-bold flex items-center">
                <img src="../../assets/icon/asset-management.svg"
                  class="h-5 w-5 inline-block mr-2" />
                Auction Asset
              </a>
            </a-menu-item>
            <a-menu-item @click="auctionSessionManagement">
              <a class="font-bold flex items-center">
                <img src="../../assets/icon/auction-session-management.svg"
                  class="h-5 w-5 inline-block mr-2" />
                Auction Management
              </a>
            </a-menu-item>
            <a-menu-item>
              <button @click="handleLogout" class="font-bold flex items-center">
                <img src="../../assets/icon/logout.svg" class="h-5 w-5 inline-block mr-2" />
                Logout
              </button>
            </a-menu-item>
          </a-menu>
        </div>
      </div>
    </div>

    <a-drawer v-model:open="open" title="Dashboard" placement="left" class="bg-gray-100">
      <div class="space-y-2">
        <button @click="navigateTo('/user/default')"
          class="flex items-center p-4 w-full hover:bg-gray-200 rounded-md transition-colors">
          <img src="../../assets/icon/home.svg" class="h-5 w-5 mr-3" />
          <span class="text-gray-800 font-bold">Home</span>
        </button>
        <button @click="navigateTo('/user/product')"
          class="flex items-center p-4 w-full hover:bg-gray-200 rounded-md transition-colors">
          <span class="text-gray-800 font-bold">Auction Product</span>
        </button>
        <button @click="navigateTo('/user/session')"
          class="flex items-center p-4 w-full hover:bg-gray-200 rounded-md transition-colors">
          <span class="text-gray-800 font-bold">Auction Session</span>
        </button>
        <button @click="navigateTo('/user/news')"
          class="flex items-center p-4 w-full hover:bg-gray-200 rounded-md transition-colors">
          <span class="text-gray-800 font-bold">News</span>
        </button>
        <button @click="navigateTo('/user/introduction')"
          class="flex items-center p-4 w-full hover:bg-gray-200 rounded-md transition-colors">
          <span class="text-gray-800 font-bold">Introduction</span>
        </button>
        <button @click="navigateTo('/user/contact')"
          class="flex items-center p-4 w-full hover:bg-gray-200 rounded-md transition-colors">
          <span class="text-gray-800 font-bold">Contact</span>
        </button>
      </div>
    </a-drawer>
  </div>
</template>

<script setup>
import { ref, onMounted, onBeforeUnmount } from 'vue';
import { useRouter } from 'vue-router';
import { useStore } from 'vuex';
import { message } from 'ant-design-vue';

const store = useStore();
const router = useRouter();
const open = ref(false);
const showDropdown = ref(false);
const showSidebar = ref(false);
const dropdownTrigger = ref(null);

const goToHome = () => {
  router.push("/user/default");
};

const toggleDropdown = () => {
  showDropdown.value = !showDropdown.value;
};

const hideDropdown = () => {
  showDropdown.value = false;
};

const hideSidebar = () => {
  showSidebar.value = false;
};

const auctionManagement = () => {
  router.push('/user/allAuction');
  hideDropdown();
};

const productManagement = () => {
  router.push('/user/allProduct');
  hideDropdown();
};

const auctionSessionManagement = () => {
  router.push('/user/allSession');
  hideDropdown();
};

const profileManagement = () => {
  router.push('/user/profile');
  hideDropdown();
};

const auctionHistory = () => {
  router.push('/user/allAuctionHistory');
  hideDropdown();
};

const handleLogout = async () => {
  try {
    await store.dispatch('logout');
    // message.success('You have successfully logout');
    router.push('/');
  } catch (error) {
    message.error('Logout. Please try again.');
  }
};

const handleClickOutside = (event) => {
  if (dropdownTrigger.value && !dropdownTrigger.value.contains(event.target)) {
    hideDropdown();
  }
};

onMounted(() => {
  document.addEventListener('click', handleClickOutside);
});

onBeforeUnmount(() => {
  document.removeEventListener('click', handleClickOutside);
});

const showDrawer = () => {
  open.value = true;
};

const navigateTo = (path) => {
  router.push(path);
  open.value = false;
};
</script>
