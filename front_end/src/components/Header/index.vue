<template>
  <div class="w-full h-full">
    <div class="header fixed left-0 top-0 w-full z-50 bg-green-200 h-20 flex items-center">
      <div class="w-1/10 flex sm:hidden items-center justify-center ml-4">
        <span @click="showDrawer">
          <img src="../../assets/icon/dashboard.svg" alt="Dashboard" class="w-6 h-6" />
        </span>
      </div>
      <div class="w-7/10 flex">
        <img @click="goToHome" src="../../assets/images/logo.png" alt="Logo" class="mt-2 mr-28 ml-6 h-20 w-30">
        <div class="hidden sm:flex items-center justify-center space-x-2 ml-6">
          <ul class="hidden md:block navbar-item font-bold cursor-pointer">
            <router-link to="/home/default" active-class="text-green-600">
              <div class="w-full h-full p-5 flex items-center justify-center">
                <img src="../../assets/icon/home.svg" class="h-5 w-5 inline-block mr-2" />
                Home
              </div>
            </router-link>
          </ul>

          <a class="hidden md:block ant-dropdown-link font-bold cursor-pointer hover:text-green-600">
            <router-link to="/home/product" class="block w-full h-full hover:text-green-600 rounded"
              active-class="text-green-600">
              <div class="w-full h-full p-5 flex items-center justify-center">
                Auction Product
              </div>
            </router-link>
          </a>
          <a class="hidden md:block ant-dropdown-link font-bold cursor-pointer hover:text-green-600">
            <router-link to="/home/session" class="block w-full h-full hover:text-green-600 rounded"
              active-class="text-green-600">
              <div class="w-full h-full p-5 flex items-center justify-center">
                Auction Session
              </div>
            </router-link>
          </a>
          <a class="hidden xl:block ant-dropdown-link font-bold cursor-pointer hover:text-green-600">
            <router-link to="/home/news" class="block w-full h-full hover:text-green-600 rounded"
              active-class="text-green-600">
              <div class="w-full h-full p-5 flex items-center justify-center">
                News
              </div>
            </router-link>
          </a>
          <ul class="hidden xl:block navbar-item font-bold cursor-pointer">
            <router-link to="/home/introduction" class="block w-full h-full p-5 hover:text-green-600 rounded"
              active-class="text-green-600">
              <div class="w-full h-full flex items-center justify-center">Introduction</div>
            </router-link>
          </ul>
          <ul class="hidden xl:block navbar-item font-bold cursor-pointer">
            <router-link to="/home/contact" class="block w-full h-full hover:text-green-600 rounded"
              active-class="text-green-600">
              <div class="w-full h-full p-5 flex items-center justify-center">Contact</div>
            </router-link>
          </ul>
        </div>
      </div>

      <div class="w-1/5 flex items-center justify-center space-x-4">
        <button @click="handleLogin"
          class="bg-white hover:bg-green-600 text-black hover:text-white font-bold py-1 px-4 rounded shadow-lg outline outline-1 outline-green-600 focus:outline-none">
          Login
        </button>
        <button @click="handleRegister"
          class="hidden sm:block bg-white hover:bg-green-600 text-black hover:text-white font-bold py-1 px-4 rounded shadow-lg outline outline-1 outline-green-600 focus:outline-none">
          Register
        </button>
      </div>
    </div>

    <div>
      <a-drawer v-model:open="open" title="Dashboard" placement="left" class="bg-gray-100">
        <div class="space-y-2">
          <button @click="navigateTo('/home/default')"
            class="flex items-center p-4 w-full hover:bg-gray-200 rounded-md transition-colors">
            <img src="../../assets/icon/home.svg" class="h-5 w-5 mr-3" />
            <span class="text-gray-800 font-bold">Home</span>
          </button>
          <button @click="navigateTo('/home/product')"
            class="flex items-center p-4 w-full hover:bg-gray-200 rounded-md transition-colors">
            <span class="text-gray-800 font-bold">Auction Product</span>
          </button>
          <button @click="navigateTo('/home/session')"
            class="flex items-center p-4 w-full hover:bg-gray-200 rounded-md transition-colors">
            <span class="text-gray-800 font-bold">Auction Session</span>
          </button>
          <button @click="navigateTo('/home/news')"
            class="flex items-center p-4 w-full hover:bg-gray-200 rounded-md transition-colors">
            <span class="text-gray-800 font-bold">News</span>
          </button>
          <button @click="navigateTo('/home/introduction')"
            class="flex items-center p-4 w-full hover:bg-gray-200 rounded-md transition-colors">
            <span class="text-gray-800 font-bold">Introduction</span>
          </button>
          <button @click="navigateTo('/home/contact')"
            class="flex items-center p-4 w-full hover:bg-gray-200 rounded-md transition-colors">
            <span class="text-gray-800 font-bold">Contact</span>
          </button>
        </div>
      </a-drawer>
    </div>
  </div>
</template>

<script setup>
import { ref } from 'vue';
import { useRouter } from 'vue-router';

const router = useRouter();
const open = ref(false);

const showDrawer = () => {
  open.value = true;
};

const closeDrawer = () => {
  open.value = false;
};

const goToHome = () => {
  router.push("/home/default");
};

const handleLogin = () => {
  router.push('/login');
};

const handleRegister = () => {
  router.push('/register');
};

const navigateTo = (path) => {
  router.push(path);
  closeDrawer();
};
</script>
