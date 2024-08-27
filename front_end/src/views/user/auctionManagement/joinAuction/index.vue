<template>
  <div v-if="auction" class="absolute left-0 top-24 container p-1 h-5/6 max-w-full">
    <div class="flex flex-col md:flex-row h-full">
      <div class="w-full md:w-1/3 bg-black flex items-center justify-center mb-4 md:mb-0">
        <div class="relative w-full h-full flex items-center justify-center">
          <img v-for="(image, index) in images" :key="index" :src="image.src" alt="Session"
            v-show="index === currentImageIndex" class="max-w-full max-h-full object-contain" />
          <button @click="prevImage"
            class="absolute left-2 top-1/2 transform -translate-y-1/2 bg-white bg-opacity-50 p-2 rounded-full">
            <img src="../../../../assets/icon/prev-arrow-slide.svg" alt="Previous" class="w-6 h-6" />
          </button>
          <button @click="nextImage"
            class="absolute right-2 top-1/2 transform -translate-y-1/2 bg-white bg-opacity-50 p-2 rounded-full">
            <img src="../../../../assets/icon/next-arrow-slide.svg" alt="Next" class="w-6 h-6" />
          </button>
        </div>
      </div>

      <div class="relative w-full md:w-1/3 bg-white p-4 flex flex-col mb-4 md:mb-0">
        <button @click="goBack" class="absolute -top-16 right-5 text-gray-500 hover:text-gray-700 mt-20">
          <img src="../../../../assets/icon/cancel.svg" alt="Close" class="w-6 h-6" />
        </button>
        <h1 class="text-2xl font-bold text-gray-800 mb-4">{{ auction.title }}</h1>
        <div class="border-b-2 border-gray-300 mb-4"></div>
        <div class="mb-4">
          <h2 v-if="sessionState === 'PENDING'" class="text-md font-semibold text-blue-600 mb-2">Auction starts in:
            {{ timeUntilStart }}</h2>
          <h2 v-else-if="sessionState === 'IN_PROGRESS'" class="text-md font-semibold text-green-600 mb-2">Time
            remaining: {{ timeLeft }}</h2>
          <h2 v-else-if="sessionState === 'FINISHED'" class="text-md font-semibold text-red-600 mb-2">Auction has
            ended</h2>
        </div>
        <div class="border-b-2 border-gray-300 mb-4"></div>
        <div class="mb-4">
          <h2 class="text-xl font-semibold text-gray-700 mb-6">Session Details</h2>
          <p class="text-gray-700 mb-2"><strong>Starting Price:</strong> {{ formattedStartingBid }}</p>
          <p class="text-gray-700 mb-2"><strong>Stepping Price:</strong> {{ formattedSteppingPrice }}</p>
          <p class="text-gray-700 mb-2"><strong>Start Time:</strong> {{ auction.startTime ?? '?' }}</p>
          <p class="text-gray-700 mb-2"><strong>End Time:</strong> {{ auction.endTime ?? '?' }}</p>
        </div>
        <div class="mt-auto">
          <p :class="{ 'text-orange-500': isCurrentPriceYours, 'text-gray-700': !isCurrentPriceYours }"
            class="text-xl mb-2">
            <strong>Current Price:</strong> {{ formattedCurrentPrice }} VND
          </p>
          <div class="flex items-center mb-4 text-xl">
            <span class="text-gray-700 mr-2"><strong>Your Price:</strong></span>
            <input v-model="yourPriceInput" type="text" @input="adjustYourPrice" @keydown.enter="handlePlaceBid"
              @keydown.up="increasePrice" @keydown.down="decreasePrice"
              class="border p-2 rounded w-44 text-right font-mono" :step="steppingPrice" /> VND
          </div>
          <button @click="handlePlaceBid" :disabled="!biddable" :class="[biddable ? 'bg-green-500' : 'bg-gray-500']"
            class="text-white p-2 rounded w-full">
            Place Bid
          </button>
        </div>
      </div>


      <div class="h-full w-px bg-gray-300 ml-4"></div>

      <!-- list user auction -->
      <div class="w-full md:w-1/3 bg-white p-4 flex flex-col">
        <h2 class="text-xl font-bold text-gray-800 mb-4">Latest auction updates</h2>
        <div class="border-b-2 border-gray-300 mb-4"></div>
        <div class="overflow-y-scroll h-full custom-scrollbar p-2 flex justify-end flex-col">
          <div v-for="(bid, index) in bids" :key="index" hoverable
            class="h-10 text-sm w-full flex justify-start items-center shadow-md rounded-lg mb-2 p-2 ">
            <a-card-meta :title="bid.name + ' bid ' + bid.price + ' VND'"></a-card-meta>
          </div>
        </div>
      </div>

      <button @click="toggleComments"
        class="fixed top-20 right-2 flex justify-center items-center bg-white p-2 rounded-full border-collapse outline outline-green-400 shadow-lg z-50 h-14 w-14">
        <img v-if="!showComments" src="../../../../assets/icon/comment.svg" alt="Toggle" class="w-6 h-6" />
        <img v-else src="../../../../assets/icon/hide-comment.svg" alt="Toggle" class="w-6 h-6" />
      </button>
      <div v-if="showComments"
        class="z-40 fixed top-24 right-0 w-96 h-5/6 bg-white p-4 shadow-lg rounded-lg transition-transform transform border-collapse outline outline-slate-400 "
        :class="{ 'translate-x-0': showComments, 'translate-x-full': !showComments }">
        <div class="p-2">
          <h2 class="text-xl font-semibold text-gray-700 mb-6">Comments</h2>
          <a-card v-for="(noti, index) in notifications" :key="index" hoverable
            class="h-96 bg-white shadow-lg rounded-lg mb-2 custom-scrollbar">
            <a-card-meta :title="index + 1" :description="noti.content"></a-card-meta>
          </a-card>
        </div>
        <a-list item-layout="horizontal" :data-source="comments"
          class="p-5 overflow-y-scroll custom-scrollbar">
          <template #renderItem="{ item }">
            <a-list-item :key="item.id">
              <a-list-item-meta :description="item.content">
                <template #title>
                  <a class="font-bold">{{ item.name }}</a>
                </template>
                <template #avatar>
                  <a-avatar :src="item.avatar" />
                </template>
              </a-list-item-meta>
            </a-list-item>
          </template>
        </a-list>
        <div class="flex justify-center items-center absolute w-80 bottom-6 ml-3 mt-4 rounded space-x-2">
          <input v-model="myCommentInput" @keydown.enter="handleComment" type="text" placeholder="Enter your comment..."
            class="flex-1 w-full ml-3 border p-2 rounded-lg" />
          <button @click="handleComment" class="bg-green-300 text-white p-2 rounded-lg hover:bg-green-400">
            <img src="../../../../assets/icon/send.svg" alt="Next" class="w-6 h-6" />
          </button>
        </div>
      </div>
    </div>
  </div>
</template>


<script setup>
import { ref, computed, onMounted, onUnmounted, watchEffect, defineProps } from 'vue';
import { useRoute, useRouter } from 'vue-router';
import { differenceInMilliseconds, differenceInSeconds, format, formatDistance, parse, parseISO } from 'date-fns';
import { useStore } from 'vuex';
import { jwtDecode } from 'jwt-decode';
import sessionApi from '../../../../api/auctionSession';
import auctionApi from '../../../../api/auctions';
import authApi from '../../../../api/auths';
import { message } from 'ant-design-vue';

const IMAGE_PREFIX = import.meta.env.VITE_IMAGE_PREFIX;

const store = useStore();

const route = useRoute();
const router = useRouter();

const showComments = ref(false);

function toggleComments() {
  showComments.value = !showComments.value;
}

// const props = defineProps({
//     auctionId: {
//         type: Number,
//         required: true,
//     }
// });

// const { auctionId } = props;

const auctionId = route.params.id;
const userId = jwtDecode(localStorage.getItem('token')).id;
console.log(userId);

const auctionInfoRef = ref(null);
const auction = computed(() => auctionInfoRef.value || {});
const product = computed(() => auction.value.product || {});

const goBack = () => {
  router.back();
};

const images = computed(() => {
  const raw = product.value?.image?.split(", ");
  console.log(raw);
  const img = raw?.map((src) => ({
    src: IMAGE_PREFIX + src
  })) || [];
  console.log(img);
  return img;
});

const currentImageIndex = ref(0);

function prevImage() {
  if (currentImageIndex.value > 0) {
    currentImageIndex.value--;
  } else {
    currentImageIndex.value = images.value.length - 1;
  }
};

function nextImage() {
  if (currentImageIndex.value < images.value.length - 1) {
    currentImageIndex.value++;
  } else {
    currentImageIndex.value = 0;
  }
};


const baseDate = new Date();
let diffseconds = 0;
const now = ref(baseDate);
const timeUntilStart = computed(() => {
  if (!auction.value.startTime) {
    return null;
  }
  const startTime = parse(auction.value.startTime, 'yyyy-MM-dd HH:mm:ss', new Date());
  return formatTimeLeft(now.value, startTime);
})
const timeLeft = computed(() => {
  if (!auction.value.endTime) {
    return null;
  }
  const endTime = parse(auction.value.endTime, 'yyyy-MM-dd HH:mm:ss', new Date());
  return formatTimeLeft(now.value, endTime);
})

function updateCountdown() {
  const theNow = new Date();
  const diff = Math.floor(differenceInSeconds(theNow, baseDate));
  if (diff > diffseconds) {
    diffseconds = diff;
    now.value = theNow;
  }
};

let countdownInterval = null;

function formatTimeLeft(from, to) {
    let inSeconds = differenceInSeconds(to, from);
    const negative = inSeconds < 0;
    inSeconds = Math.abs(inSeconds);
    const hours = Math.floor(inSeconds / 3600);
    const minutes = Math.floor(inSeconds / 60) % 60;
    const seconds = inSeconds % 60;
    if (negative) return '00:00:00';
    return `${negative ? '-' : ''}${formatUnit(hours)}:${formatUnit(minutes)}:${formatUnit(seconds)}`;

  function formatUnit(unit) {
    return unit.toString().padStart(2, '0');
  }
}



const sessionState = ref(null);
const startingPrice = computed(() => auction.value.startBid);
const steppingPrice = computed(() => auction.value.pricePerStep);
const currentPrice = ref(null);
const isCurrentPriceYours = ref(false);
const yourPriceInput = ref('0');

const formattedSteppingPrice = computed(() => {
  return formatPrice(steppingPrice.value);
});

const formattedStartingBid = computed(() => {
  return formatPrice(startingPrice.value);
});

const formattedCurrentPrice = computed(() => {
  return formatPrice(currentPrice.value);
});
// const formattedYourPrice = computed(() => {
//     return formatPrice(yourPrice.value);
// });

const minimumPrice = computed(() => {
  return Math.max(startingPrice.value,
    currentPrice.value ? currentPrice.value + steppingPrice.value : 0);
});

const biddable = computed(() => {
  const yourPrice = parsePrice(yourPriceInput.value);
  return sessionState.value === "IN_PROGRESS" && yourPrice >= minimumPrice.value;
});

function adjustYourPrice(event) {
    const cleaned = event.target.value.replace(/\D/g, '');
    const newPrice = parsePrice(cleaned);
    const oldCursorPos = event.target.selectionStart;
    const digitsToTheRight = event.target.value.substring(oldCursorPos).match(/\d/g)?.length || 0;

  const formatted = formatPrice(newPrice);
  let newCursorPos = formatted.length;
  let dgcount = 0;
  while (dgcount < digitsToTheRight && newCursorPos > 0) {
    if (formatted[newCursorPos - 1].match(/\d/)) {
      dgcount++;
    }
    newCursorPos--;
  }
  if (dgcount < digitsToTheRight) {
    newCursorPos = formatted.length;
  }
  yourPriceInput.value = formatted;
  setTimeout(() => {
    event.target.setSelectionRange(newCursorPos, newCursorPos);
  }, 0);
};

function increasePrice() {
  if (!auctionInfoRef.value) {
    return;
  }
  const yourPrice = parsePrice(yourPriceInput.value);
  const addedPrice = yourPrice + steppingPrice.value;

  const editedPrice = Math.max(minimumPrice.value, addedPrice);
  yourPriceInput.value = formatPrice(editedPrice);
}

function decreasePrice() {
  if (!auctionInfoRef.value) {
    return;
  }
  const yourPrice = parsePrice(yourPriceInput.value);
  const decreasedPrice = yourPrice - steppingPrice.value;

  const editedPrice = Math.max(minimumPrice.value, decreasedPrice);
  yourPriceInput.value = formatPrice(editedPrice);
}

function handlePlaceBid() {
  if (!biddable.value) {
    return;
  }
  const newPrice = parsePrice(yourPriceInput.value);
  sessionApi.bid(auction.value.id, newPrice).catch((err) => {
    message.error(err.message);
  });
};

function parsePrice(priceStr) {
  return (parseInt(priceStr.replace(/\./g, '')) || 0);
};

function formatPrice(priceNum) {
  return priceNum == null ? "" :
    `${priceNum.toString().replace(/\B(?=(\d{3})+(?!\d))/g, '.')}`;
};



const bids = ref([]);

async function updateBid(bid) {
  currentPrice.value = bid.bid;
  if (!bid.userId) {
    return;
  }
  bids.value.push({name: "xxx", price: bid.bid});
  const index = bids.value.length - 1;
  
  if (isCurrentPriceYours.value = bid.userId === userId) {
    bids.value[index] = {
      name: 'You',
      price: bid.bid,
    }
  } else {
    authApi.getAnotherInfo(bid.userId).then((user) => {
      bids.value[index] = {
        name: user.fullName,
        price: bid.bid,
      }
    });
  }
}



const comments = ref([]);

const myCommentInput = ref('');

function handleComment() {
  if (!myCommentInput.value) {
    return;
  }
  sessionApi.comment(auctionId, myCommentInput.value).catch((err) => {
    message.error(err.message);
  });
  myCommentInput.value = '';
};

function addComment(data) {
    let { commentId, userId, content } = data;
    content = JSON.parse(content);
    comments.value.push({ content });
    const index = comments.value.length - 1;
    Promise.resolve(authApi.getAnotherInfo(userId)).then((user) => {
        comments.value[index] = { 
            id: commentId, 
            userId, 
            name: user.fullName, 
            content,
            avatar: user.avatar
        };
    })
}


const notifications = ref([]);

const addNotification = (data) => {
  notifications.value.push(data);
};


function handleUnload() {
  console.log('leaving room');
  sessionApi.leaveAuctionRoom(auctionId);
};

onMounted(() => {
  auctionApi.getAuctionById(auctionId)
    .then((res) => {
        console.log(res)
        res.startTime = parse(res.startTime, 'yyyy-MM-dd HH:mm:ss', new Date());
        res.startTime =  res.startTime.setHours(res.startTime.getHours() + 7);
        res.startTime = format(res.startTime, 'yyyy-MM-dd HH:mm:ss');
        res.endTime = parse(res.endTime, 'yyyy-MM-dd HH:mm:ss', new Date());
        res.endTime =  res.endTime.setHours(res.endTime.getHours() + 7);
        res.endTime = format(res.endTime, 'yyyy-MM-dd HH:mm:ss');
        auctionInfoRef.value = res;
        sessionState.value = 
            ["IN_PROGRESS", "FINISHED", "CANCELLED"].includes(res.status) ? res.status : "PENDING";
        if (sessionState.value === "IN_PROGRESS") {
            sessionApi.getCurrentPrice(auctionId).then((res) => {
                updateBid(res.data);
            });
            sessionApi.getPastComments(auctionId).then((comments) => {
                comments.forEach(addComment);
            });
        }
    })
    .catch((err) => {
      console.error(err);
    });


    const callbacks = {
        onStart: () => {
            sessionState.value = "IN_PROGRESS";
            console.log('auction started');
            sessionApi.getCurrentPrice(auctionId).then((res) => {
                updateBid(res.data);
            });
        },
        onEnd: (winnerId) => {
            console.log(winnerId);
            sessionState.value = "FINISHED";
            console.log('auction ended');
            message.success('Đấu giá đã kết thúc');
            if (winnerId === userId) {
                message.success(`Xin chúc mưng bạn đã thắng đấu giá`);
            } else if (winnerId != null) {
                authApi.getAnotherInfo(winnerId).then((user) => {
                    message.success(`${user.fullName} đã chiến thắng đấu giá`);
                });
            }
        },
        onBid: updateBid,
        onComment: addComment,
        onNotification: addNotification
    };

    const join = () => sessionApi.joinAuctionRoom(auctionId, callbacks)
    .catch((err) => {
      if (err.isAxiosError && err.response) {
        if (err.response.data?.message === 'da tham gia dau gia') {
          sessionApi.leaveAuctionRoom(auctionId).finally(() => {
            setTimeout(join, 1000);
          });
        } else {
          message.error('loi tham gia phong dau gia');
        }
      }
      console.error(err);
    });
  join();

    countdownInterval = setInterval(updateCountdown, 100);
});

onUnmounted(() => {
    console.log('leaving room');
    clearInterval(countdownInterval);
});

</script>

<style lang="scss" src="./style.scss" scoped />
