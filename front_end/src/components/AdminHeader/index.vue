<template>
  <div class="w-full h-full">
    <div class="header fixed left-0 top-0 w-full z-50 bg-green-200 h-20 flex items-center" @click="onCloseModal">
      <div class="w-1/10 flex sm:hidden items-center justify-center ml-4">
        <span @click="showDrawer">
          <img src="../../assets/icon/dashboard.svg" alt="Dashboard" class="w-6 h-6" />
        </span>
      </div>
      <div class="w-8/10 flex">
        <img src="../../assets/images/logo.png" alt="Logo" class="mt-2 mr-40 ml-6 h-20 w-30 flex justify-between">
        <div class="hidden sm:flex items-center justify-center space-x-2 ml-6">
          <a class="ant-dropdown-link font-bold cursor-pointer hover:text-green-600">
            <router-link to="/admin/auctionManagement" class="block w-full h-full hover:text-green-600 rounded"
              active-class="text-green-600">
              <div class="w-full h-full p-5 flex items-center justify-center">
                <img src="../../assets/icon/auction-management.svg" alt="V" class="h-5 w-5 mr-2" />
                Registered Auctions
              </div>
            </router-link>
          </a>
          <a class="ant-dropdown-link font-bold cursor-pointer hover:text-green-600">
            <router-link to="/admin/userManagement" class="block w-full h-full hover:text-green-600 rounded"
              active-class="text-green-600">
              <div class="w-full h-full p-5 flex items-center justify-center">
                <img src="../../assets/icon/profile.svg" alt="V" class="h-5 w-5 mr-2" />
                User Management
              </div>
            </router-link>
          </a>
        </div>
      </div>

      <!-- avatar and dropdown-->
      <div class=" relative w-1/10 flex items-center m-auto">
        <div ref="dropdownTrigger" @click="toggleDropdown" class="relative flex items-center hover:cursor-pointer">
          <a-avatar class="bg-green-400">
            <img src="../../assets/icon/user.svg" alt="User" class="w-6 h-6" />
          </a-avatar>
          <img src="../../assets/icon/chevron-down.svg" alt="V" class="h-5 w-5" />
        </div>
        <div v-if="showDropdown"
          class="absolute top-10 left-5 w-32 bg-white border border-gray-200 rounded shadow-lg z-20">
          <a-menu>
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
    <div>
      <a-drawer v-model:open="open" title="Dashboard" placement="left">
        <p>Productss ...</p>
        <p>Auctions ...</p>
        <p>News ...</p>
        <p>Introduction ...</p>
        <p>Contact ...</p>
      </a-drawer>
    </div>
  </div>
</template>

<script setup>
import { ref, onMounted, onBeforeUnmount } from 'vue';
import { useRouter } from 'vue-router';
//import { useAuthStore } from '../../stores/auth/auth-store';
import { message } from 'ant-design-vue';
import { useStore } from 'vuex';
// const authStore = useAuthStore();
const router = useRouter();
const store = useStore();
const open = ref(false);
const showDropdown = ref(false);
const dropdownTrigger = ref(null);

const toggleDropdown = () => {
  showDropdown.value = !showDropdown.value;
};

const hideDropdown = () => {
  showDropdown.value = false;
};

const profileManagement = () => {
  router.push('/admin/profileManagement');
  hideDropdown();
};


const handleLogout = async () => {
  try {
    const response = await store.dispatch('logout');
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
</script>
