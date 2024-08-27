import Authenticate from "../views/auth/authenticate/index.vue";
import Login from "../views/auth/login/index.vue";
import Register from "../views/auth/register/index.vue";
import ForgotPassword from "../views/auth/forgotPassword/index.vue";
import Verify from "../views/auth/verify/index.vue";

const authRoutes = [
  {
    path: "/login",
    component: Authenticate,
    children: [
      {
        path: "",
        name: "login",
        component: Login
      },
      {
        path: "forgotPassword",
        name: "login-forgotPassword",
        component: ForgotPassword,

      },
      {
        path: "verify",
        name: "login-verify",
        component: Verify
      },
      {
        path: "/register",
        name: "register",
        component: Register
      },
    ]
  }
];

export default authRoutes;
