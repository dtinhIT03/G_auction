<template>
  <div class="relative mt-20 px-8">
    <a-list item-layout="vertical" :data-source="data">
      <template #renderItem="{ item }">
        <div
          class="news-item bg-white shadow-lg rounded-lg overflow-hidden mb-6 transform hover:scale-105 transition duration-300 ease-in-out">
          <div class="flex">
            <div class="w-1/5">
              <a-avatar class="w-44 h-44 flex justify-center items-center"
                src="https://www.freestock.com/450/freestock_574637119.jpg" shape="square" />
            </div>
            <div class="w-4/5 p-4">
              <h2 class="text-lg font-bold text-gray-800 mb-2">
                <a :href="item.link" class="hover:text-blue-500">{{ item.title }}</a>
              </h2>
              <p class="text-gray-600">{{ item.description }}</p>
            </div>
          </div>
        </div>
      </template>
    </a-list>
  </div>
</template>

<script setup>
const data = [
  {
    title: 'Historic Art Auction Breaks Records',
    description: 'A Van Gogh painting sells for a record-breaking $150 million at Sotheby.',
    link: 'https://www.antdv.com/'
  },
  {
    title: 'Rare Diamond Fetches High Price',
    description: 'A 50-carat blue diamond sells for $45 million in a heated bidding war.',
    link: 'https://www.antdv.com/'
  },
  {
    title: 'World’s Largest Online Auction',
    description: 'Over 10,000 items, including rare antiques, are auctioned off in a 24-hour online event.',
    link: 'https://www.antdv.com/'
  },
  {
    title: 'Celebrity Memorabilia Auctioned for Charity',
    description: "Items from Hollywood stars raise $5 million for children’s hospitals.",
    link: 'https://www.antdv.com/'
  },
  {
    title: 'Ancient Artifact Unearthed and Sold',
    description: 'A rare Egyptian artifact from 2000 BC sells for $10 million at an exclusive auction.',
    link: 'https://www.antdv.com/'
  },
  {
    title: 'Tech Startup Auction Nets $1 Billion',
    description: 'Investors compete in a high-stakes auction for shares in a breakthrough tech company.',
    link: 'https://www.antdv.com/'
  },
  {
    title: 'Auction for World’s Most Expensive Wine',
    description: 'A bottle of 1945 Romanée-Conti wine sells for $500,000 at auction.',
    link: 'https://www.antdv.com/'
  },
  {
    title: 'Sports Memorabilia Auction Draws Record Bidders',
    description: 'Michael Jordan’s rookie jersey sells for $1.2 million in a competitive auction.',
    link: 'https://www.antdv.com/'
  },
  {
    title: 'Luxury Car Auction Features Classic Ferraris',
    description: 'A 1962 Ferrari 250 GTO sells for $48 million, setting a new auction record.',
    link: 'https://www.antdv.com/'
  },
  {
    title: 'Charity Auction Raises Millions for Conservation',
    description: 'Rare wildlife photography and experiences raise $8 million for conservation efforts.',
    link: 'https://www.antdv.com/'
  }
];
</script>
