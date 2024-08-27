<template>
    <div class="container mx-auto max-w-sm p-10">
        <img src="../../../assets/images/logo.png" alt="Logo" class="h-130 h-130 -mt-10 flex items-center justify-center">
        <router-link to="/login" class=" flex items-center space-x-2 -ml-16 mb-4 text-gray-600 hover:text-gray-900">
            <img src="../../../assets/icon/auth-back.svg" class="w-6 h-6" />
            <span>Back</span>
        </router-link>
        <h1 class="flex items-center justify-center text-2xl font-bold mb-4">Forgot your password?</h1>
        <h2>
            Please provide your email, we will send you an OTP verification code:
        </h2>

        <form @submit.prevent="onSubmit">
            <div class="mt-6 mb-6">
                <label for="username" class="block text-gray-700">Email</label>
                <input type="text" id="username" v-model="username"
                    class="form-input w-full border border-gray-300 rounded-md px-2 py-2 focus:outline-none focus:ring-2 focus:ring-blue-600" />
            </div>

            <router-link to="/login/verify" type="submit"
                class="flex items-center justify-center w-full bg-green-600 hover:bg-green-700 text-white font-bold py-2 px-4 rounded">
                Next
            </router-link>
        </form>
    </div>
</template>


<script>

// export default {
//     data() {
//         return {
//             email: '',
//         };
//     },
//     methods: {
//         onSubmit() {
//             baseService.post('/users/forget-password', { email: this.email })
//                 .then(() => {
//                     this.$router.push('/login/verify-otp');
//                 });
//         },
//     },
// };
</script>