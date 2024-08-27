<template>
    <div class="container mx-auto max-w-sm p-0">
        <img src="../../../assets/images/logo.png" alt="Logo" class="h-56 ml-16 flex items-center justify-center">
        <router-link to="/login" class="flex items-center space-x-2 -ml-10 mb-4 text-gray-600 hover:text-gray-900">
            <img src="../../../assets/icon/auth-back.svg" class="w-6 h-6" />
            <span>Back</span>
        </router-link>
        <h1 class="flex items-center justify-center text-2xl font-bold mb-4">Verification</h1>
        <h2>
            Verification code is sent to your email. Please check and enter the verification code:
        </h2>

        <form @submit.prevent="onSubmit">
            <div class="flex justify-between mt-10 mb-10">
                <input v-for="index in 6" :key="index" type="text" :id="`code${index}`" v-model="codes[index - 1]"
                    @input="moveFocus(index - 1)" @keydown="checkBackspace($event, index - 1)" @keypress="isNumber"
                    class="form-input w-14 h-14 border border-gray-300 rounded-md px-2 py-2 focus:outline-none focus:ring-2 focus:ring-blue-600"
                    maxlength="1" inputmode="numeric" pattern="[0-9]*" />
            </div>

            <!-- resend btn -->
            

            <button type="submit"
                class="flex items-center justify-center w-full bg-green-600 hover:bg-green-700 text-white font-bold py-2 px-4 rounded">
                Next
            </button>
        </form>
        <div class="flex flex-row justify-around mt-6 mb-6">
                <button :disabled="isResendDisabled" @click="resendCode"
                    class="bg-green-600 hover:bg-green-700 text-white px-4 py-2 rounded disabled:bg-gray-400">
                    Resend Code 
                </button>
                <p v-if="isResendDisabled" class="mt-2 text-sm text text-gray-600">
                    Resend available in {{ countdown }} seconds </p>
            </div>
    </div>
</template>

<script setup>
import { reactive, ref, onMounted } from 'vue';
import { useRouter } from 'vue-router';
import { useStore } from 'vuex';
import { message } from 'ant-design-vue';

const codes = reactive(['', '', '', '', '', '']);
const countdown = ref(30);
const isResendDisabled = ref(true);

const router = useRouter();
const store = useStore();

const checkBackspace = (event, index) => {
    if (event.key === 'Backspace') {
        if (codes[index] === '' && index > 0) {
            const previousIndex = index - 1;
            const previousInput = document.getElementById(`code${previousIndex + 1}`);
            previousInput.focus();
        }
    }
};

const moveFocus = (index) => {
    if (codes[index] && index < codes.length - 1) {
        const nextInput = document.getElementById(`code${index + 2}`);
        if (nextInput) {
            nextInput.focus();
        }
    }
};

const isNumber = (event) => {
    const charCode = event.which ? event.which : event.keyCode;
    if (charCode < 48 || charCode > 57) {
        event.preventDefault();
    }
};

const startCountdown = () => {
    countdown.value = 30;
    // isResendDisabled.value = true;
    const timer = setInterval(() => {
        countdown.value -= 1;
        if (countdown.value <= 0) {
            clearInterval(timer);
            isResendDisabled.value = false;
        }
    }, 1000);
};

const resendCode = async () => {
    try {
        const email = store.getters.getUser.email;
        await store.dispatch('resendOtp',{email});
        isResendDisabled.value = !isResendDisabled.value;
        startCountdown();
    } catch (error) {
        console.error('Resend error:', error);
        message.error('Failed to resend verification code. Please try again.');
    }
};

async function onSubmit() {
    const otp = codes.join('');
    const email = store.getters.getUser.email;
    console.log(email);

    const data = {
        email : email,
        otp : otp,
    };
    
    try {
        await store.dispatch('verify', data);
        router.push('/login');
    } catch (error) {
        console.error('Verification error:', error);
        message.error('Verification failed. Please try again.');
    }
}

onMounted(() => {
    startCountdown();
});
</script>

<style lang="scss" src="./style.scss" scoped />
