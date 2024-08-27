<template>
    <div class="container mx-auto max-w-sm p-8">
        <div class="flex justify-between">
            <div class="flex justify-center">
                <router-link to="/home/default" class="flex items-center space-x-2 text-gray-600 hover:text-gray-900">
                <svg class="h-6 w-6" fill="none" stroke="currentColor" viewBox="0 0 24 24"
                    xmlns="http://www.w3.org/2000/svg">
                    <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M15 19l-7-7 7-7"></path>
                </svg>
                <span>Back</span>
            </router-link>
            </div>

            <div class="flex justify-center">
                <h1 class="flex items-center justify-center text-2xl font-bold ml-6 ">Register</h1>

            </div>
            
            <div>
                <img src="../../../assets/images/logo.png" alt="Logo" class="h-auto flex items-center justify-center">
            </div>
        </div>

        <form @submit.prevent="handleSignUp">
            <div class="mb-4">
                <label for="email" class="block text-gray-700">Email</label>
                <input type="email" id="email" v-model="data.email"
                    class="form-input w-full border border-gray-300 rounded-md px-2 py-2 focus:outline-none focus:ring-2 focus:ring-blue-600" />
            </div>

            <div class="mb-4">
                <label for="password" class="block text-gray-700">Password</label>
                <input type="password" id="password" v-model="data.password"
                    class="form-input w-full border border-gray-300 rounded-md px-2 py-2 focus:outline-none focus:ring-2 focus:ring-blue-600" />
            </div>

            <div class="mb-4">
                <label for="full_name" class="block text-gray-700">Full Name</label>
                <input type="text" id="full_name" v-model="data.full_name"
                    class="form-input w-full border border-gray-300 rounded-md px-2 py-2 focus:outline-none focus:ring-2 focus:ring-blue-600" />
            </div>

            <div class="mb-4">
                <label for="date_of_birth" class="block text-gray-700">Date of Birth</label>
                <input type="date" id="date_of_birth" v-model="data.date_of_birth"
                    class="form-input w-full border border-gray-300 rounded-md px-2 py-2 focus:outline-none focus:ring-2 focus:ring-blue-600" />
            </div>

            <!-- <div class="mb-4">
                <label for="gender" class="block text-gray-700">Gender</label>
                <select id="gender" v-model="data.gender"
                    class="form-select w-full border border-gray-300 rounded-md px-2 py-2 focus:outline-none focus:ring-2 focus:ring-blue-600">
                    <option value="true">Male</option>
                    <option value="false">Female</option>
                </select>
            </div> -->

            <div class="mb-4">
                <label for="phone" class="block text-gray-700">Phone</label>
                <input type="text" id="phone" v-model="data.phone"
                    class="form-input w-full border border-gray-300 rounded-md px-2 py-2 focus:outline-none focus:ring-2 focus:ring-blue-600" />
            </div>

            <!-- <div class="mb-4">
                <label for="address" class="block text-gray-700">Address</label>
                <input type="text" id="address" v-model="data.address"
                    class="form-input w-full border border-gray-300 rounded-md px-2 py-2 focus:outline-none focus:ring-2 focus:ring-blue-600" />
            </div> -->

            <button type="submit"
                class="flex items-center justify-center w-full bg-green-600 hover:bg-green-700 text-white font-bold py-2 px-4 rounded">
                Register
            </button>
        </form>

    </div>
</template>

<script setup>

import { reactive } from 'vue';
import { useRouter } from 'vue-router';
import { useStore } from 'vuex'

const data = reactive({
    email: '',
    password: '',
    full_name: '',
    date_of_birth: '',
    phone: '',
});

const router = useRouter();
const store = useStore();

const validateForm = () => {
    let isValid = true;
    if (!data.email) {
        message.error('Email is required');
        isValid = false;
    }
    if (!data.password) {
        message.error('Password is required');
        isValid = false;
    }
    if (!data.full_name) {
        message.error('Full name is required');
        isValid = false;
    }

    return isValid;
};

const handleSignUp = async () => {
    const isValid = validateForm();
    if (!isValid) return;
    try {
        const response = await store.dispatch("registry", data );
        router.push('/login/verify');
        console.log(response);
    } catch (error) {
        console.log(error);
    }
};

</script>