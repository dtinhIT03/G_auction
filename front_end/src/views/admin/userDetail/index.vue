<template>
  <div v-show="isVisible" title="User Details" @click.self="closeModal"
    class="fixed inset-0 flex items-center justify-center bg-black bg-opacity-50">
    <div class="modal-content relative bg-white p-6 rounded-lg w-full max-w-screen-lg mx-4 mt-28">
      <button @click="closeModal" class="absolute top-4 right-4">
        <img src="../../../assets/icon/cancel.svg" alt="Close" class="w-6 h-6" />
      </button>
      <div class=" flex space-y-2">
        <div class="w-1/3 flex items-center justify-center">
          <p><strong></strong>
            <img :src="avatar" alt="avatar" class=" w-52 h-52">
          </p>
        </div>
        <div class="w-2/3 space-y-2">
          <p><strong>Full Name:</strong> {{ user.fullName }}</p>
          <p><strong>Birthday:</strong> {{ user.dateOfBirth }}</p>
          <p><strong>Phone:</strong> {{ user.phone }}</p>
          <p><strong>Email:</strong> {{ user.email }}</p>
          <p><strong>Address:</strong> {{ user.address }}</p>
          <p><strong>Gender:</strong> {{ user.gender }}</p>
          <select v-model="status" class="form-select w-full border border-gray-300 rounded-md px-2 py-2">
            <option v-for="sta in statusOptions" :key="sta" :value="sta">{{ sta }}</option>
          </select>
        </div>
      </div>
      <div class="flex flex-col items-center justify-center mt-4 space-y-4">
        <button @click="updateStatus"
          class="w-full flex items-center justify-center p-2 bg-blue-50 text-black font-bold rounded-md hover:bg-teal-200 outline-gray-400 shadow-lg">
          <img src="../../../assets/icon/accept.svg" class="w-6 h-6 mr-3" />
          Update
        </button>
        <!-- <button
          class="w-full flex items-center justify-center p-2 bg-blue-50 text-black font-bold rounded-md hover:bg-teal-200 outline-gray-400 shadow-lg">
          <img src="../../../assets/icon/delete-account.svg" class="w-6 h-6 mr-3" />
          Add to blacklist
        </button> -->
        <!-- <button
          class="w-full flex items-center justify-center p-2 bg-blue-50 text-black font-bold rounded-md hover:bg-teal-200 outline-gray-400 shadow-lg">
          <img src="../../../assets/icon/delete.svg" class="w-6 h-6 mr-3" />
          Delete User
        </button> -->
      </div>
    </div>
  </div>
</template>

<script setup>
import { defineProps, defineEmits, ref, reactive, watch } from 'vue';
import adminApi from "../../../api/admin.js";
import defaultAvt from '../../../assets/images/defaultAvt.png';

const props = defineProps({
  user: Object,
  isVisible: Boolean
});

const emit = defineEmits(['close']);

const statusOptions = ref(['ACTIVE', 'BLOCK', 'BAN']);
const status = ref();
const data = reactive({
  UserId: '',
  newStatus: ''
});

const avatar = ref();

watch(() => props.user, () => {
  status.value = props.user.statusAccount;
  data.UserId = props.user.id;
  avatar.value = props.user.avatar || defaultAvt;
});
const closeModal = () => {
  emit('close');
};

const updateStatus = async () => {
  try {

    if (status.value !== props.user.statusAccount) {
      data.newStatus = status.value;
      console.log(data);
      const response = await adminApi.updateStatusUser(data);
      window.location.reload();
    }
  } catch (error) {
    console.log(error);
  }
};
</script>

<style lang="scss" src="./style.scss" scoped />
