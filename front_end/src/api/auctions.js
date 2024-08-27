import api from "./axios.js";
import { message } from "ant-design-vue";

const auctionApi = {
  async addAuction(data) {
    try {
      const token = localStorage.getItem("token");
      const response = await api.post("/v1/auctions/", data, {
        headers: { Authorization: `Bearer ${token}` },
      });
      message.success("Đấu giá đã được tạo thành công. Vui lòng chờ duyệt.");
      return response.data.data;
    } catch (error) {
      message.error(error.response.data.message);
      throw error;
    }
  },

  async getAuctionById(id){
   try {
      const response = await api.get('/v1/auctions/' + id);
      return response.data.data;
    } catch (error) {
      message.error(error.response.data.message);
      throw error;
    }
  },
  async getMyAuction() {
    try {
      const token = localStorage.getItem("token");
      const response = await api.get("/v1/auctions/get-my-created", {
        headers: { Authorization: `Bearer ${token}` },
      });
      return response.data.data;
    } catch (error) {
      message.error(error.response.data.message);
      throw error;
    }
  },

  async getMyJoined() {
    const token = localStorage.getItem("token");
    const response = await api.get("/v1/auctions/get-my-registered", {
      headers: { Authorization: `Bearer ${token}` },
    });
    return response.data.data;
  },

  async getAllAuctionByStatus(status, pageNo,pageSize) {
    const response = await api.get(
      `/v1/auctions/get-all-auction-by-status?statusAuction=${status}&pageNo=${pageNo}&pageSize=${pageSize}`
    );
    return response.data.data;
  },

  async registerAuction(id) {
    const response = await api.post("/v1/auctions/" + id + "/regis-join");
  },

  async getAuctionById(id){
    try {
       const response = await api.get('/v1/auctions/' + id);
       return response.data.data;
    } catch (error) {
       message.error(error.response.data.message);
    }   
   }
};
export default auctionApi;
