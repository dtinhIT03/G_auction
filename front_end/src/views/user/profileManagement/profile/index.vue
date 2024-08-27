<template>
  <div class="flex flex-col md:flex-row mt-8 mx-5 space-y-5 md:space-y-0 md:space-x-5">
    <div class="w-full md:w-1/5 mr-4">
      <Profile />
    </div>

    <div class="w-full md:w-4/5 container border-l mx-auto p-4 md:p-10 mt-4 md:mt-12">
      <a-card hoverable class="w-full h-auto bg-white shadow-lg rounded-lg p-4">
        <div class="flex items-center justify-center">
          <div class="relative inline-block group">
            <img :src="user.avatar" alt="Avatar" class="w-48 h-48 mr-4 ml-1 shadow-lg rounded-lg" />
            <button @click="changeAvatar"
              class="absolute -bottom-2 right-12 transform translate-x-1/2 -translate-y-1/2 bg-slate-400 bg-opacity-50 p-2 rounded-full opacity-0 group-hover:opacity-100 transition-opacity duration-300">
              <img src="../../../../assets/icon/change-avatar.svg" alt="Change Avatar" class="w-6 h-6" />
            </button>
          </div>
        </div>

        <div v-if="showUploadModal" class="fixed inset-0 flex items-center justify-center bg-black bg-opacity-50 z-50">
          <div class="bg-white p-6 rounded-lg z-50">
            <h2 class="text-lg font-bold mb-4">Upload New Avatar</h2>
            <input type="file" @change="handleFileChange" />
            <div v-if="imagePreview" class="mt-4">
              <img :src="imagePreview" alt="Image Preview" class="w-48 h-48 rounded-lg" />
            </div>
            <div class="mt-4 flex justify-end space-x-2">
              <button @click="showUploadModal = false" class="bg-gray-300 px-4 py-2 rounded">Cancel</button>
              <button @click="confirmUpload" class="bg-teal-500 text-white px-4 py-2 rounded">Confirm</button>
            </div>
          </div>
        </div>

        <a-tabs v-model:activeKey="activeKey" centered>
          <a-tab-pane key="1" tab="Personal Informations">
            <div class="text-xl ml-5 md:ml-20 space-y-5">
              <a-card-meta title="Full Name" :description="user.fullName"></a-card-meta>
              <a-card-meta title="Birthday" :description="user.dateOfBirth"></a-card-meta>
              <a-card-meta title="Gender" :description="user.gender"></a-card-meta>
            </div>
          </a-tab-pane>

          <a-tab-pane key="2" tab="Contact Informations" force-render>
            <div class="text-xl ml-5 md:ml-20 space-y-5">
              <a-card-meta title="Email" :description="user.email"></a-card-meta>
              <a-card-meta title="Address" :description="user.address"></a-card-meta>
              <a-card-meta title="Phone Number" :description="user.phone"></a-card-meta>
            </div>
          </a-tab-pane>
        </a-tabs>
      </a-card>

      <TheChevron />
    </div>
  </div>
</template>

<script setup>
import TheChevron from '../../../../components/Chevron/index.vue';
import Profile from '../../../../components/Profile/index.vue';
import { ref, computed, onMounted, onBeforeMount, watch } from 'vue';
import { message } from 'ant-design-vue';



import { useStore } from "vuex";
const activeKey = ref('1');

const showUploadModal = ref(false);
const avatar = ref('');
const imagePreview = ref('');

let selectedFile = ref('');

const store = useStore();


const user = computed(() => store.getters.getUser);

const changeAvatar = () => {
  showUploadModal.value = true;
};

const handleFileChange = (event) => {

  const files = event.target.files;
  selectedFile = files[0];

  const imageUrl = URL.createObjectURL(selectedFile);
  imagePreview.value = imageUrl;

};

const confirmUpload = async () => {
  try {
    avatar.value = imagePreview.value;

    const formData = new FormData();
    formData.append('files', selectedFile);
    formData.append('name', selectedFile.name);
    console.log(formData);
    const url = await store.dispatch('uploadImage', formData);

    const response = await store.dispatch('updateMyInfo', { avatar: url });
    console.log(response);

    showUploadModal.value = false;
  } catch (error) {
    message.error('OOps! Something went wrong');
  }

};
onBeforeMount(async () => {
  try {
    const res = await store.dispatch('getMyProfile');
  } catch (error) {
    message.error('Fetch failed');
  }
});
</script>
