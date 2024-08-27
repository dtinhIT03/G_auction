import api from './axios.js';
import { message } from 'ant-design-vue';

const adminApi = {

  // USER
  async getAllUsers(pageNo) {
    try {
      const token = localStorage.getItem('token');
      if (!pageNo) {
        pageNo = 0;
      }
      const response = await api.get('/v1/users/get-all-info?pageNo=' + pageNo, { headers: { Authorization: `Bearer ${token}` } });
      console.log(response.data.data.content);
      return response.data.data;
    } catch (error) {
      message.error(error.response.data.message);
      throw error;
    }
  },

  async getAllUserByStatus(status, pageNo) {
    try {
      const token = localStorage.getItem('token');
      if (!pageNo) {
        pageNo = 0;
      }
      const response = await api.get('/v1/users/get-all-info-by-status?statusAccount=' + status + '&pageNo=' + pageNo, { headers: { Authorization: `Bearer ${token}` } });
      console.log(response.data.data.content);
      return response.data.data;
    } catch (error) {
      message.error(error.response.data.message);
      throw error;
    }
  },

  async updateStatusUser(data) {
    try {
      const token = localStorage.getItem('token');
      const response = await api.patch('/v1/users/update-status/' + data.UserId + '?status=' + data.newStatus, { headers: { Authorization: `Bearer ${token}` } });
      message.success("Đã cập nhật trạng thái thành công");
    } catch (error) {
      message.error(error.response.data.message);
      throw error;
    }
  },

  // AUCTION
  async getAllAuctions(pageNo) {
    try {
      const token = localStorage.getItem('token');
      if (!pageNo) {
        pageNo = 0;
      }
      const response = await api.get('/v1/auctions/get-all-auction?sortBy=createdAt&sortDir=desc&pageNo=' + pageNo, { headers: { Authorization: `Bearer ${token}` } });
      console.log(response.data.data.content);
      return response.data.data;
    } catch (error) {
      message.error(error.response.data.message);
      throw error;
    }
  },
  async getAllAuctionsByStatus(status, pageNo) {
    try {
      const token = localStorage.getItem('token');
      if (!pageNo) {
        pageNo = 0;
      }
      const response = await api.get('/v1/auctions/get-all-auction-by-status?sortBy=createdAt&sortDir=desc&statusAuction=' + status + '&pageNo=' + pageNo, { headers: { Authorization: `Bearer ${token}` } });
      console.log(response.data.data.content);
      return response.data.data;
    } catch (error) {
      message.error(error.response.data.message);
      throw error;
    }
  },

  async confirmAuction(id) {
    try {
      const token = localStorage.getItem('token');
      const response = await api.post('/v1/auctions/confirm/' + id, { headers: { Authorization: `Bearer ${token}` } });
      message.success("Đã xác nhận đấu giá thành công");
      //window.location.reload();
    } catch (error) {
      message.error(error.response.data.message);
      throw error;
    }
  }


};

export default adminApi;
