import AuthenticatedLayout from "../layouts/AuthenticatedLayout.vue";
import Default from "../views/home/default/index.vue";
import AllSession from "../views/user/sessionManagement/allSession/index.vue";
import AddSession from "../views/user/sessionManagement/addSession/index.vue";
import AllProduct from "../views/user/productManagement/allProduct/index.vue";
import AddProduct from "../views/user/productManagement/addProduct/index.vue";
import Product from "../views/home/product/index.vue";
import Session from "../views/home/session/index.vue";
import News from "../views/home/news/index.vue";
import Introduction from "../views/home/introduction/index.vue";
import Contact from "../views/home/contact/index.vue";
import Profile from "../views/user/profileManagement/profile/index.vue";
import EditProfile from "../views/user/profileManagement/editProfile/index.vue";
import ChangePassword from "../views/user/profileManagement/changePassword/index.vue";
import AllAuction from "../views/user/auctionManagement/allAuction/index.vue";
import JoinAuction from "../views/user/auctionManagement/joinAuction/index.vue";
import ViewProduct from "../views/user/productManagement/productDetail/index.vue";
import History from "../views/user/auctionHistory/allAuctionHistory/index.vue";
import AuctionWon from "../views/user/auctionHistory/auctionWon/index.vue";

import SearchProduct from "../views/searchProduct/index.vue";

const userRoutes = [
  {
    path: "/user",
    component: AuthenticatedLayout,
    meta: { requiresAuth: true },
    children: [
      {
        path: "default",
        name: "user-default",
        component: Default
      },
      {
        path: "editProfile",
        name: "user-edit-profile",
        component: EditProfile
      },
      {
        path: "changePassword",
        name: "user-changePassword",
        component: ChangePassword,
        meta: { requiresVerification: true }
      },
      {
        path: "profile",
        name: "user-profile",
        component: Profile
      },
      {
        path: "allAuctionHistory",
        name: "user-history",
        component: History
      },
      {
        path: "auctionWon",
        name: "auction-won",
        component: AuctionWon
      },
      {
        path: "allAuction",
        name: "all-auction",
        component: AllAuction
      },
      {
        path: '/auction/:id',
        name: 'joinAuction',
        component: JoinAuction,
        props: true
      },
      {
        path: "allSession",
        name: "all-session",
        component: AllSession
      },
      {
        path: "addSession",
        name: "add-session",
        component: AddSession
      },
      {
        path: '/product/:id',
        name: 'ProductDetail',
        component: ViewProduct
      },
      {
        path: "allProduct",
        name: "all-product",
        component: AllProduct
      },
      {
        path: "addProduct",
        name: "add-product",
        component: AddProduct
      },
      {
        path: "product",
        name: "user-product",
        component: Product
      },
      {
        path: "session",
        name: "user-session",
        component: Session
      },
      {
        path: "news",
        name: "user-news",
        component: News
      },
      {
        path: "introduction",
        name: "user-introduction",
        component: Introduction
      },
      {
        path: "contact",
        name: "user-contact",
        component: Contact
      },
      {
        path: "search",
        name: "user-search",
        component: SearchProduct
      },
    ]
  }
];

export default userRoutes;
