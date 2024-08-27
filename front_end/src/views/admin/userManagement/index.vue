<template>
  <div class="flex mt-20 mx-5 space-x-5">
    <div class="w-1/5 ml-4 mr-4">
      <div class="z-10">
        <a-card hoverable class="h-40 bg-white shadow-lg rounded-md mt-6">
          <h1 class="text-lg font-bold">Search</h1>
          <div class="flex items-center justify-center mt-4">
            <input
              type="text"
              v-model="searchKeyword"
              class="w-3/4 p-2 border border-gray-300 rounded-md"
              placeholder="Enter user keyword..."
            />
            <button
              class="ml-2 p-2 bg-blue-50 hover:bg-teal-200 rounded-full outline-gray-400 shadow-lg"
            >
              <img
                src="../../../assets/icon/search.svg"
                alt="Search"
                class="w-5 h-5"
              />
            </button>
          </div>
        </a-card>
      </div>
      <div class="z-10">
        <a-card hoverable class="h-50 bg-white shadow-lg rounded-md mt-6">
          <h1 class="text-lg font-bold">Status</h1>
          <div class="flex items-center justify-center flex-wrap mt-4">
            <div v-for="tag in tags" :key="tag" class="mt-1 mx-1">
              <button
                @click="filterByTag(tag)"
                :class="[
                  'p-2 rounded-full shadow-lg',
                  {
                    'bg-teal-200': selectedTags.includes(tag),
                    'bg-blue-50': !selectedTags.includes(tag),
                  },
                ]"
              >
                {{ tag }}
              </button>
            </div>
          </div>
        </a-card>
      </div>
    </div>

    <div
      class="w-4/5 container border-l bg-white mx-auto p-10 rounded-md shadow-lg mt-6"
    >
      <div class="relative w-full">
        <h1 class="text-2xl font-bold text-center text-gray-800">User List</h1>
        <div class="border-b-2 border-zinc-400 mt-2 mb-8"></div>

        <a-table
          :columns="filteredColumns"
          :data-source="users"
          :row-key="(record) => record.id"
          :pagination="pagination"
          @change="handleTableChange"
        >
          <template #bodyCell="{ column, record }">
            <template v-if="column.key">
              <span class="hover:cursor-pointer" @click="selectUser(record)">
                {{ record[column.dataIndex] }}
              </span>
            </template>
          </template>
        </a-table>
      </div>
    </div>
  </div>

  <UserDetail
    :user="selectedUser"
    :isVisible="isModalVisible"
    @close="closeModal"
  />
</template>

<script setup>
import { ref, computed, reactive, onMounted, watch } from "vue";
import UserDetail from "../userDetail/index.vue";
import adminApi from "../../../api/admin.js";

const isModalVisible = ref(false);
const selectedUser = ref({});

const selectUser = (user) => {
  selectedUser.value = user || {};
  console.log(selectedUser.value);
  isModalVisible.value = true;
};

const columns = [
  { title: "Full Name", dataIndex: "fullName", key: "fullName" },
  { title: "Birthday", dataIndex: "dateOfBirth", key: "birthday" },
  { title: "Phone", dataIndex: "phone", key: "phone" },
  { title: "Email", dataIndex: "email", key: "email" },
  { title: "Address", dataIndex: "address", key: "address" },
  { title: "Gender", dataIndex: "gender", key: "gender" },
  { title: "Status", dataIndex: "statusAccount", key: "status" },
];

const data = [
  // { fullName: 'Jack', birthday: '1997/01/01', phone: '0210221100', email: 'j5m@gmail.com', address: 'Hanoi', gender: 'MALE', avatarUrl: 'j5m.jpg', status: 'Ban' },
  // Add more user data here
];

const users = reactive([]);
const pagination = reactive({ total: 0, pageSize: 8, current: 1});
const selectedTags = ref([]);
const searchKeyword = ref("");
const tags = ref(["ACTIVE", "BLOCK", "BAN"]);

const handleTableChange = async (pagination) => {
  if (selectedTags.value.length === 0) {
    getAllUsers(pagination.current);
  } else {
    getAllUserByStatus(selectedTags.value, pagination.current); 
  }
};

const getAllUsers = async (currentPage) => {
  try {
    pagination.current = currentPage ;
    const pageNo = pagination.current - 1;
    const response = await adminApi.getAllUsers(pageNo);
    pagination.total = response.totalElements;

    users.length = 0; 
    users.push(...response.content);

    console.log(pagination.total);
    console.log(users);
  } catch (error) {
    console.log(error);
  }
};

const getAllUserByStatus = async (status, currentPage) => {
  try {
    pagination.current = currentPage ;
    const pageNo = pagination.current - 1;
    const response = await adminApi.getAllUserByStatus(status,pageNo);

    pagination.total = response.totalElements;
    users.length = 0;
    users.push(...response.content);
  } catch (error) {
    console.log(error);
  }
};

// const filteredData = computed(() => {
//   return data.filter((user) => {
//     const matchesKeyword = user.fullName
//       .toLowerCase()
//       .includes(searchKeyword.value.toLowerCase());
//     const matchesTags =
//       selectedTags.value.length === 0 ||
//       selectedTags.value.includes(user.status);
//     return matchesKeyword && matchesTags;
//   });
// });

const filteredColumns = computed(() => {
  return columns.filter((col) => col.visible === undefined);
});

// const filterByTag = (tag) => {
//   if (selectedTags.value.includes(tag)) {
//     selectedTags.value = selectedTags.value.filter((t) => t !== tag);
//   } else {
//     selectedTags.value.push(tag);
//   }
// };

const filterByTag = (tag) => {
    if (selectedTags.value === tag) {
        selectedTags.value = '';
    } else {
        selectedTags.value = tag;
    }

};

watch(selectedTags, (newValue, oldValue) => {
    console.log('Selected tags:', newValue);
  
    if (newValue.length === 0) {
      getAllUsers();
    } else {
      const filteredUsers = getAllUserByStatus(newValue);
      console.log('Filtered auctions:', filteredUsers);  
    }
   
});


const closeModal = () => {
  isModalVisible.value = false;
};

onMounted(() => {
  getAllUsers();
});
</script>
