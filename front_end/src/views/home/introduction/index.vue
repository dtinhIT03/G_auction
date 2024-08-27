<template>
  <div class="relative mt-10 min-h-screen py-10">
    <div class="max-w-4xl mx-auto bg-white p-6 sm:p-8 md:p-10 lg:p-12 xl:p-16 rounded-lg shadow-lg">
      <h1 class="text-3xl sm:text-4xl md:text-5xl font-extrabold text-gray-800 mb-6">
        Welcome to Our Auction Platform!
      </h1>
      <p class="text-gray-700 mb-6 text-base sm:text-lg md:text-xl leading-relaxed">
        We're excited to have you on our online auction platform. Here, you'll discover a wide variety of exciting items
        and have the chance to participate in dynamic auction sessions. We are dedicated to providing a transparent and
        enjoyable auction experience for all users.
      </p>

      <div class="mb-8">
        <h2 class="text-2xl sm:text-3xl md:text-4xl font-semibold text-gray-800 mb-4">
          Why Choose Us?
        </h2>
        <ul class="list-disc list-inside text-gray-700 space-y-3">
          <li class="flex items-center">
            <img src="../../../assets/icon/success.svg" alt="Check Icon" class="w-5 h-5 text-blue-500 mr-2" />
            Diverse range of products from various categories.
          </li>
          <li class="flex items-center">
            <img src="../../../assets/icon/success.svg" alt="Check Icon" class="w-5 h-5 text-blue-500 mr-2" />
            User-friendly interface with intuitive auction tools.
          </li>
          <li class="flex items-center">
            <img src="../../../assets/icon/success.svg" alt="Check Icon" class="w-5 h-5 text-blue-500 mr-2" />
            Ensured transparency and fairness in every auction.
          </li>
          <li class="flex items-center">
            <img src="../../../assets/icon/success.svg" alt="Check Icon" class="w-5 h-5 text-blue-500 mr-2" />
            Friendly customer support ready to assist you.
          </li>
        </ul>
      </div>

      <div>
        <h2 class="text-2xl sm:text-3xl md:text-4xl font-semibold text-gray-800 mb-4">
          How It Works
        </h2>
        <p class="text-gray-700 mb-6 text-base sm:text-lg md:text-xl leading-relaxed">
          To start, create an account and browse through our auction sessions. Place bids on items you're interested in
          and monitor the auction to secure your desired products. At the end of the auction, the highest bidder wins
          the item. It's that simple!
        </p>
        <a href="/register"
          class="inline-block bg-green-600 text-white py-2 px-4 rounded-lg hover:bg-green-700 transition duration-300">
          Register Now
        </a>
      </div>
    </div>
  </div>
</template>

<script setup>
import { ref } from 'vue';
</script>

<style scoped>
/* Add custom styles here if needed */
</style>
