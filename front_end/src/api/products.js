import api from "./axios.js";
import { message } from "ant-design-vue";

const productApi = {
  async addProduct(data) {
    try {
      const token = localStorage.getItem("token");
      const response = await api.post("/v1/products", data, {
        headers: { Authorization: `Bearer ${token}` },
      });
      return response.data.data;
    } catch (error) {
      message.error(error.response.data.message);
      throw error;
    }
  },

  async getProducts() {
    try {
      const token = localStorage.getItem("token");
      const response = await api.get("/v1/products/get-my-all", {
        headers: { Authorization: `Bearer ${token}` },
      });
      return response.data.data;
    } catch (error) {
      message.error(error.response.data.message);
      throw error;
    }
  },

  async getProductById(id) {
    try {
      const response = await api.get("/v1/products/" + id);
      return response.data.data;
    } catch (error) {
      message.error(error.response.data.message);
      throw error;
    }
  },
  async getTopProducts() {
    try {
      const response = await api.get("/v1/products/top-popular?limit=4");
      return response.data.data;
    } catch (error) {
      message.error(error.response.data.message);
      throw error;
    }
  },

  async getNewProducts() {
    try {
      const response = await api.get(
        "/v1/products/get-all-product?sortDir=desc&sortBy=id"
      );
      return response.data.data;
    } catch (error) {
      message.error(error.response.data.message);
      throw error;
    }
  },

  async getAllProductByCategory(category, pageNo,pageSize) {
    try {
      const response = await api.get(
        `/v1/products/get-all-product-by-category?category=${category}&pageNo=${pageNo}&pageSize=${pageSize}`
      );

      if(!response ) return [];
      return response.data.data;
    } catch (error) {
      message.error(error.response.data.message);
      throw error;
    }
  },

  async getProductById(id) {
    try {
      const response = await api.get("/v1/products/" + id);
      return response.data.data;
    } catch (error) {
      message.error(error.response.data.message);
      throw error;
    }
  },

  async interestProduct(id) {
    try {
      const response = await api.post("/v1/products/interest/" + id);
      console.log(response);
      return response.data;
    } catch (error) {
      throw error;
    }
  },
  async searchProduct(keyword) {
    try {
      const response = await api.get(`/v1/products/search?key=${keyword}&page_size=8`);
      return response.data.data;
    }catch(error) {
      throw error;

    }
  },
  async favoriteProduct(){
    try {
       const res = await api.get(`/v1/products/list-product-favorite`); 
      return res.data.data;
    }catch(error) {
      throw error;
    }
  }
};
export default productApi;
