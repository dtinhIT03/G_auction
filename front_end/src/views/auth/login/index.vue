<template>
  <div class="relative container mx-auto max-w-sm p-8">
    <img src="../../../assets/images/logo.png" alt="Logo" class="h-130 h-130 -mt-10 flex items-center justify-center" />
    <router-link to="/home/default"
      class="flex items-center space-x-2 absolute top-0 left-0 text-gray-600 hover:text-gray-900">
      <img src="../../../assets/icon/auth-back.svg" alt="Back" class="w-6 h-6" />
      <span>Back</span>
    </router-link>
    <h1 class="flex items-center justify-center text-2xl font-bold mb-4">
      Login
    </h1>

    <form @submit.prevent="handleSignIn">
      <div class="mb-4">
        <label for="email" class="block text-gray-700">Email</label>
        <input type="text" id="email" v-model="email"
          class="form-input w-full border border-gray-300 rounded-md px-2 py-2 focus:outline-none focus:ring-2 focus:ring-blue-600" />
      </div>

      <div class="mb-4 relative">
        <label for="password" class="block text-gray-700">Password</label>
        <div class="relative">
          <input :type="passwordType" id="password" v-model="password"
            class="form-input w-full border border-gray-300 rounded-md px-2 py-2 pr-10 focus:outline-none focus:ring-2 focus:ring-blue-600" />
          <button type="button" @click="togglePasswordVisibility" class="absolute inset-y-0 right-2 flex items-center">
            <img src="../../../assets/icon/eye-hide.svg" alt="Eye hide"
              class="w-4 h-4 cursor-pointer" />
          </button>
        </div>
        <span v-if="validation.password" class="text-red-500">{{
          validation.password
          }}</span>
      </div>

      <div class="flex items-center mb-6">
        <input type="checkbox" id="rememberMe" v-model="rememberMe" />
        <label for="rememberMe" class="text-gray-700 ml-2 text-sm">Remember me?</label>
        <router-link to="/login/forgotPassword" class="text-sm text-blue-600 underline ml-14">Forgot
          password?</router-link>
      </div>

      <button type="submit" :disabled="loading"
        class="btn btn-primary w-full bg-green-600 hover:bg-green-700 text-white font-bold py-2 px-4 rounded">
        Login
      </button>
    </form>

    <p class="text-sm text-gray-600 mt-6">
      Do not have an account?
      <router-link to="/register" class="text-blue-600 underline ml-1">Register now</router-link>
    </p>
  </div>
  <div v-if="loading" class="flex items-center justify-center w-1/2 h-full absolute loading">
    <a-spin :indicator="indicator" />
  </div>
</template>

<script setup>
import { ref, reactive, computed } from "vue";
import { useRouter } from "vue-router";
import { useStore } from "vuex";
import { LoadingOutlined } from '@ant-design/icons-vue';
import { h } from 'vue';

const indicator = h(LoadingOutlined, {
  style: {
    fontSize: '100px',
  },
  spin: true,
});

const email = ref("");
const password = ref("");
const rememberMe = ref(false);
const validation = reactive({ email: null, password: null });

const isPasswordVisible = ref(false);
const passwordType = computed(() =>
  isPasswordVisible.value ? "text" : "password"
);

const router = useRouter();
const store = useStore();

const loading = computed(() => store.getters.getLoading);

function validateEmail(email) {
  const re = /^[^\s@]+@[^\s@]+\.[^\s@]+$/;
  return re.test(String(email).toLowerCase());
}
function togglePasswordVisibility() {
  isPasswordVisible.value = !isPasswordVisible.value;
}

const handleSignIn = async () => {
  let isValid = true;

  if (!email.value) {
    validation.email = "Email is required.";
    isValid = false;
  } else if (!validateEmail(email.value)) {
    validation.email = "Email is not valid.";
    isValid = false;
  } else {
    validation.email = null;
  }

  if (!password.value) {
    validation.password = "Password is required.";
    isValid = false;
  } else if (password.value.length < 4) {
    validation.password = "You need to enter 4 characters or more";
    isValid = false;
  } else {
    validation.password = null;
  }

  if (!isValid) return;
  try {
    // const response = await authApi.login(email.value, password.value);
    const response = await store.dispatch("login", {
      email: email.value,
      password: password.value,
    });

    const isAdmin = store.getters.getIsAdmin;
    console.log(isAdmin);
    if (isAdmin) {
      router.push("/admin/auctionManagement");
    } else {
      router.push("/user/default");
    }
  } catch (error) {
    console.error("Login failed:", error);
  }
};
</script>
