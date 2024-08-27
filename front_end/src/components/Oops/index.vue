<template>
    <div class="flex items-center justify-center h-screen">
        <div class="text-center">
            <div class="flex items-center justify-center">
                <h1 class="text-6xl font-bold text-gray-800">404</h1>
                <!-- <img src="../../assets/icon/oops.svg" alt="Oops" class="w-14 h-14" /> -->
            </div>
            <p class="text-xl text-gray-600 mt-4">Oops! Page not found.</p>
            <p class="text-gray-500 mt-2">The page you are looking for does not exist or has been moved.</p>
            <router-link to="/" class="mt-6 inline-block bg-green-500 text-white py-2 px-4 rounded">
                Go to Home
            </router-link>
        </div>
    </div>
</template>