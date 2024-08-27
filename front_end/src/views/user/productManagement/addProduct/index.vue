<template>
  <div class="flex flex-col md:flex-row mt-20 mx-5 space-y-5 md:space-y-0 md:space-x-5">
    <div class="w-full md:w-1/5 mr-4">
      <MenuProductManagement />
    </div>
    <div class="md:w-4/5 container border-l bg-white mx-auto p-10 rounded-md shadow-lg mt-6">
      <div class="relative w-full max-w-md mx-auto">
        <h1 class="text-2xl font-bold text-center text-gray-800">
          Add Product
        </h1>
        <div class="border-b-2 border-zinc-400 mt-2 mb-8"></div>
      </div>
      <div class="flex space-x-8 justify-center lg:flex">
        <div class="w-full lg:w-1/2">
          <div class="mb-4">
            <label class="block text-gray-700 mb-3">Upload Image</label>
            <input type="file" @change="onFileChange"
              class="form-input w-full border border-gray-300 rounded-md px-2 py-2" />
          </div>
          <div v-if="loadingImage" class="flex items-center justify-center">
            <a-spin size="large" />
          </div>
          <button @click="onUpload"
            class="border border-gray-300 rounded-md px-2 py-2 bg-sky-300 hover:bg-sky-400">Upload</button>
          <!-- <a-carousel autoplay>
                        <a-carousel-slide v-for="(image,index) in imagePreview" :key="index">
                            <img :src="image" alt="Product Image"
                            class="w-full h-auto border border-gray-300 rounded-md" />
                        </a-carousel-slide>
                    </a-carousel> -->
          <div v-for="(image, index) in imagePreview" :key="index" class="relative mb-4">
            <img :src="image" alt="Product Image"
              class="w-full h-auto md:w-28 md:h-28 border border-gray-300 rounded-md" />
            <button @click="removeImage(index)"
              class="absolute flex justify-center items-center top-2 right-2 rounded-full bg-red-300 w-8 h-8 p-2">✖</button>
          </div>
        </div>

        <form @submit.prevent="submitProduct" class="w-full lg:w-1/2">
          <div class="mb-5">
            <label for="name" class="block text-gray-700 mb-3">Product Name</label>
            <input type="text" id="name" v-model="product.name"
              class="form-input w-full border border-gray-300 rounded-md px-2 py-2" />
          </div>

          <div class="mb-5">
            <label for="category" class="block text-gray-700 mb-3">Category</label>
            <select id="category" v-model="product.category"
              class="form-select w-full border border-gray-300 rounded-md px-2 py-2">
              <option value="" disabled>Select product type</option>
              <option v-for="cat in categories" :key="cat" :value="cat">{{ cat }}</option>
            </select>
          </div>

          <div class="mb-5">
            <label for="description" class="block text-gray-700 mb-3">Description</label>
            <textarea id="description" v-model="product.description"
              class="form-textarea w-full border border-gray-300 rounded-md px-2 py-2"></textarea>
          </div>

          <div v-if="loadingProduct" class="flex items-center justify-center">
            <a-spin size="large" />
          </div>
          <button type="submit"
            class="w-full bg-teal-400 hover:bg-teal-500 outline-gray-400 shadow-lg text-white font-bold py-2 px-4 rounded">
            Confirm
          </button>
        </form>
      </div>
    </div>
  </div>

  <TheChevron />
</template>

<script setup>
import TheChevron from '../../../../components/Chevron/index.vue';
import MenuProductManagement from '../../../../components/MenuProductManagement/index.vue';
import { ref, reactive } from 'vue';
import { useStore } from 'vuex';
import { message } from 'ant-design-vue';

const loadingImage = ref(false);
const loadingProduct = ref(false);

const store = useStore();

const product = reactive({
  name: '',
  category: '',
  description: '',
  image: '',
});

const images = ref([]);
// const imagePreview = ref(null);

const imagePreview = ref([]);
let selectedFile = ref([]);

const categories = ['ART', 'LICENSE_PLATE', 'VEHICLES', 'ANTIQUES', 'OTHER'];

const onFileChange = (event) => {
  // const files = event.target.files;
  // selectedFile = files[0];

  // for (let i = 0; i < files.length; i++) {
  //     const file = files[i];
  //     if (file) {
  //         const imageUrl = URL.createObjectURL(file);
  //         imagePreview.value.push(imageUrl);
  //         console.log(imagePreview.value);
  //     }
  // }
  selectedFile = event.target.files[0];
};

const removeImage = (index) => {
  imagePreview.value.splice(index, 1);
  console.log(imagePreview.value);
};

const onUpload = async () => {
  loadingImage.value = true;
  try {
    const formData = new FormData();
    formData.append('files', selectedFile);
    formData.append('name', selectedFile.name);

    const response = await store.dispatch('uploadImage', formData);

    imagePreview.value.push(response);

    console.log(imagePreview.value);
  } catch (error) {
    console.log(error);
    message.error('Upload image failed');
  } finally {
    loadingImage.value = false;
  }
};

const submitProduct = async () => {
  loadingProduct.value = true;
  try {
    for (let i = 0; i < imagePreview.value.length; i++) {
      console.log(imagePreview.value[i]);
      const rs = imagePreview.value[i].split("upload/")[1];
      images.value.push(rs);
    }

    product.image = images.value.join(', ');

    const response = await store.dispatch('addProduct', product);

    product.name = '';
    product.category = '';
    product.description = '';
    product.image = '';
    imagePreview.value = [];
    message.success('Bạn đã thêm sản phẩm thành công');
  } catch (error) {
    console.log(error);
    message.error('Vui lòng thử lại');
  }
  finally {
    loadingProduct.value = false;
  }
};
</script>
