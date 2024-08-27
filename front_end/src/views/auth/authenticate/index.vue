<template>
  <div class="min-h-screen flex">
    <div class="hidden md:block md:w-1/2 bg-cover bg-center">
      <img src="../../../assets/images/image1.jpg" alt="Background" class="w-full h-full object-cover" />
    </div>
    <div class="w-full md:w-1/2 flex items-center justify-center">
      <router-view />
    </div>
  </div>
</template>

<style lang="scss" src="./style.scss" scoped />
