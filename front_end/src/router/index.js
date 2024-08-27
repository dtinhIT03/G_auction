import { createRouter, createWebHistory } from "vue-router";
import { useStore } from "vuex";
import authRoutes from "./auth";
import userRoutes from "./user";
import adminRoutes from "./admin";
import PublicLayout from "../layouts/PublicLayout.vue";
import Default from "../views/home/default/index.vue";
import Product from "../views/home/product/index.vue";
import Session from "../views/home/session/index.vue";
import News from "../views/home/news/index.vue";
import Introduction from "../views/home/introduction/index.vue";
import Contact from "../views/home/contact/index.vue";

import SearchProduct from "../views/searchProduct/index.vue";

const routes = [
  {
    path: "/",
    redirect: "/home/default",
  },
  {
    path: "/home",
    component: PublicLayout,
    children: [
      {
        path: "default",
        name: "home-default",
        component: Default,
      },
      {
        path: "product",
        name: "home-product",
        component: Product,
      },
      {
        path: "session",
        name: "home-session",
        component: Session,
      },
      {
        path: "news",
        name: "home-news",
        component: News,
      },
      {
        path: "introduction",
        name: "home-introduction",
        component: Introduction,
      },
      {
        path: "contact",
        name: "home-contact",
        component: Contact,
      },
      {
        path: 'search',
        name: "home-search",
        component: SearchProduct,
      }
    ],
  },
  ...authRoutes,
  ...userRoutes,
  ...adminRoutes,
];

const router = createRouter({
  history: createWebHistory(),
  routes,
});

router.beforeEach((to, from, next) => {

  const store = useStore();
  const token = localStorage.getItem('token');
  const isAdmin = store.getters.getIsAdmin;

  const requiresAuth = to.matched.some(record => record.meta.requiresAuth);
  const requiresAdmin = to.matched.some(record => record.meta.requiresAdmin);

  const requiresVerification = to.matched.some(record => record.meta.requiresVerification);

  if (requiresAuth && !token) {
    next({ path: '/login', query: { redirect: to.fullPath } });
  } else if (requiresAdmin && !isAdmin) {
    next('/user/default');
    // } else if (requiresVerification && !email) {
    //     next({ path: '/login/verify2', query: { redirect: to.fullPath } });
  } else if (token && (to.path.startsWith('/login') || to.path === '/register' || to.path === '/home/default')) {
    next(isAdmin ? '/admin/auctionManagement' : '/user/default');
  } else if (token && isAdmin && to.path.startsWith('/user')) {
    next('/admin/auctionManagement');
  } else {
    next();
  }
});


export default router;
