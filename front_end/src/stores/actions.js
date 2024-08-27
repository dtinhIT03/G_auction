import { commonInputProps } from "ant-design-vue/es/vc-input/inputProps.js";
import authApi from "../api/auths.js";
import imageApi from "../api/images.js";
import stompApi from "../api/stomp.js";
import productApi from "../api/products.js";
import auctionApi from "../api/auctions.js";
import auctionSessionApi from "../api/auctionSession.js";
import { jwtDecode } from "jwt-decode";

export default {
  // clearAllState({ commit }) {
  //    commit('RESET_STATE');
  //  },

  async login({ commit }, { email, password }) {
    try {
      commit("setLoading", true);
      const response = await authApi.login(email, password);

      localStorage.setItem("token", response.token);
      const decodedToken = jwtDecode(response.token);
      commit("setUser", { id: decodedToken.id, email: decodedToken.sub });
      commit("setLoginState", true);
      const scope = decodedToken.scope;
      if (scope === "ROLE_ADMIN") {
        commit("setAdmin", true);
      }

      stompApi.setup();
    } catch (error) {
      throw error;
    } finally {
      commit("setLoading", false);
    }
  },

  async logout({ commit }) {
    try {
      const promise = authApi.logout(); 
      commit("setUser", {
        id: null,
        fullName: '',
        dateOfBirth:'' ,
        email: '',
        phone: '',
        address: '',
        gender: '',
        avatar: '',
      });
      commit("setLoginState", false);
      commit("setAdmin", false);
      commit("setProducts", []);
      commit("setAuction",[]);
      commit("setSessions", []);
      localStorage.removeItem("token");
      sessionStorage.clear();
      stompApi.teardown();
      return promise;
    } catch (error) {
      throw error;
    }
  },

  async registry({ commit }, data) {
    try {
      commit("setLoading", true);
      commit("setUser", {email : data.email}); 
      await authApi.registry(data);
      // commit('setUser', data.email);
    } catch (error) {
      throw error;
    } finally {
      commit("setLoading", false);
    }
  },

  async verify({ commit }, data) {
    try {
      await authApi.verify(data);
    } catch (error) {
      throw error;
    }
  },

  async resendOtp({ commit }, { email }) {
    try {
      await authApi.resendOtp(email);
    } catch (error) {
      throw error;
    }
  },

  async getMyProfile({ commit }) {
    try {
      const response = await authApi.getMyInfo();
      console.log(response);
      commit("setUser", {
        fullName: response.fullName,
        dateOfBirth: response.dateOfBirth,
        email: response.email,
        phone: response.phone,
        address: response.address,
        gender: response.gender,
        avatar: response.avatar,
      });
    } catch (error) {
      throw error;
    }
  },

  async changePassword({ commit }, { oldPassword, newPassword }) {
    try {
      await authApi.changePassword(oldPassword, newPassword);
    } catch (error) {
      throw error;
    }
  },

  async updateMyInfo({ commit }, data) {
    try {
      const response = await authApi.updateMyInfo(data);
      commit("setUser",response); 
    } catch (error) {
      throw error;
    }
  },

  async uploadImage({ commit, state }, images) {
    try {
      const response = await imageApi.uploadImage(images);
      // const updateImages = [...state.images, response];
      // commit('setImages', updateImages);
      return response;
    } catch (error) {
      throw error;
    } finally {
      //commit('setImages', []);
    }
  },

  async addProduct({ commit, state }, data) {
    try {
      const response = await productApi.addProduct(data);
      const updatedProducts = [...state.products, response];
      commit("setProducts", updatedProducts);
      commit("setImages", []);
    } catch (error) {
      throw error;
    }
  },

  async getProducts({ commit }) {
    try {
      const response = await productApi.getProducts();
      commit("setProducts", response);
      console.log (response);
    } catch (error) {
      throw error;
    }
  },

  //auction
  async getMyJoined({commit}) {
    try {
      const response = await auctionApi.getMyJoined();
      commit("setAuction",response);
    }
    catch (error) {
      throw error;
    }
  },

  async getMyAuction({ commit }) {
    try {
      const response = await auctionApi.getMyAuction();
      commit("setSessions", response);
    }
    catch (error) {
      throw error;
    }
  },

  async getAnotherInfo({commit},ownerId) {
    try {
      const response = await authApi.getAnotherInfo(ownerId);
      return response;
    }
    catch (error) {
      throw error;
    }
  }
  
};
