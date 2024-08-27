<template>
  <div class="z-10 mt-24">
    <a-card hoverable class="h-auto bg-white shadow-lg rounded-md mt-6">
      <h1 class="text-lg font-bold">Dashboard</h1>
      <div class="flex flex-col items-center justify-center mt-4 space-y-4">
        <router-link to="/user/profile" class="w-full">
          <button
            class="w-full flex items-center justify-center p-2 bg-blue-50 text-black font-bold rounded-md hover:bg-teal-200 outline-gray-400 shadow-lg">
            <img src="../../assets/icon/overview.svg" class="w-8 h-8 " />
            Overview Profile
          </button>
        </router-link>
        <router-link to="/user/editProfile" class="w-full">
          <button
            class="w-full flex items-center justify-center p-2 bg-blue-50 text-black font-bold rounded-md hover:bg-teal-200 outline-gray-400 shadow-lg">
            <img src="../../assets/icon/pencil.svg" class="w-5 h-5 mr-3 -ml-6" />
            Edit Profile
          </button>
        </router-link>
        <router-link to="/user/changePassword" class="w-full">
          <button class="w-full flex items-center justify-center p-2 bg-blue-50 text-black font-bold rounded-md
                   hover:bg-teal-200 outline-gray-400 shadow-lg">
            <img src="../../assets/icon/change-password.svg" class="w-6 h-6 mr-3" />
            Change Password
          </button>
        </router-link>
      </div>
    </a-card>
  </div>


</template>
