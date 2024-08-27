import { createStore } from "vuex";
import createPersistedState from "vuex-persistedstate";

import state from "./state.js";
import mutations from "./mutations.js";
import actions from "./actions.js";
import getters from "./getters.js";

const store = createStore({
  strict: false,
  plugins: [
    createPersistedState({
      storage: window.sessionStorage,
    }),
  ],
  state,
  mutations,
  actions,
  getters,
});

export default store;
