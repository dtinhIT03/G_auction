<template>
  <div v-if="visible" class="fixed top-24 right-10 p-4 rounded-md shadow-lg z-50 flex items-center justify-between"
    :class="notificationClass">
    <p :class="textColor" class="flex-1">
      {{ message }}
    </p>
    <button @click="close" class="ml-4 text-white rounded">
      <img src="../../assets/icon/cancel.svg" alt="Close" class="w-6 h-6" />
    </button>
  </div>
</template>

<script setup>
import { ref, computed, defineExpose } from 'vue';

const props = defineProps({
  message: String,
  type: {
    type: String,
    default: 'info'
  }
});

const visible = ref(false);
const message = ref('');
const type = ref('');

function showNotification(msg, notificationType = 'info') {
  message.value = msg;
  type.value = notificationType;
  visible.value = true;
  setTimeout(() => {
    visible.value = false;
  }, 5000);
}

function close() {
  visible.value = false;
}

defineExpose({ showNotification });

const notificationClass = computed(() => {
  switch (type.value) {
    case 'success':
      return 'bg-white border-green-500 border';
    case 'error':
      return 'bg-white border-red-500 border';
    default:
      return 'bg-white border-black border';
  }
});

const textColor = computed(() => {
  switch (type.value) {
    case 'success':
      return 'text-green-500';
    case 'error':
      return 'text-red-500';
    default:
      return 'text-black';
  }
});
</script>
