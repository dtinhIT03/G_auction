import api from './axios.js';
import { message } from 'ant-design-vue';

const authApi = {
  async login(email, password) {
    try {
      const response = await api.post('/v1/auths/authenticate', { email, password });
      //  message.success(response.data.message);
      return response.data.data;
    } catch (error) {
      if(error.response.data.message === "You do not have access") {
        message.error("Tài khoản của bạn đã bị khoá!!!");
      }
      else {message.error("Tài khoản hoặc mật khẩu không đúng. Vui lòng thử lại.")};
      throw error;
    }
  },

  async logout() {
    try {
      const token = localStorage.getItem('token');
      const response = await api.post('/v1/auths/logout', { token });
      localStorage.clear();
      sessionStorage.clear();
      //  return response.data.message;
    } catch (error) {
      throw error;
    }
  },

  async registry(data) {
    try {
      const response = await api.post('/v1/users/', data);
      message.success("Bạn đã đăng ký tài khoản thành công");
      console.log("Bạn đã đăng ký tài khoản thành công");
      return response.data.data;
    } catch (error) {
      console.log("Register Failed");
      message.error(error.response.data.message);
      throw error;
    }
  },

  async verify(data) {
    try {
      const response = await api.post('/v1/users/verify-otp?email=' + data.email + '&otp=' + data.otp);
      message.success("Xác thực thành công. Vui lòng đăng nhập để tiếp tục.");
      return response.data.data;
    } catch (error) {
      message.error(error.response.data.message);
      throw error;
    }
  },

  async resendOtp(email) {
    try {
      const response = await api.post('/v1/users/resend-otp?email=' + email);
      message.success("OTP đã được gửi lại. Vui lòng kiểm tra email.");
      return response.data.data;
    } catch (error) {
      message.error(error.response.data.message);
      throw error;
    }
  },

  async forgotPassword(email) {
    try {
      const response = await api.post('/v1/users/forgot-password', { email });
      message.success("Vui lòng kiểm tra email để đặt lại mật khẩu.");
      return response.data.data;
    } catch (error) {
      message.error(error.response.data.message);
      throw error;
    }
  },

  async changePassword(old_password, new_password) {
    try {
      const response = await api.put('/v1/users/change-password', { old_password, new_password });
      message.success("Đổi mật khẩu thành công.");
      return response.data.data;
    } catch (error) {
      message.error(error.response.data.message);
      throw error;
    }
  },

  async getMyInfo() {
    try {
      const token = localStorage.getItem('token');
      const response = await api.get('/v1/users/get-my-info', { headers: { Authorization: `Bearer ${token}` } });
      // message.success(response.data.message);
      return response.data.data;
    } catch (error) {
      message.error(error.response.data.message);
      throw error;
    }
  },

  async updateMyInfo(data) {
    try {
      const token = localStorage.getItem('token');
      console.log(data);
      const response = await api.put('v1/users/update-my-info', data, { headers: { Authorization: `Bearer ${token}` } });
      message.success("Cập nhật thông tin thành công.");
      return response.data.data;
    } catch (error) {
      message.error(error.response.data.message);
      throw error;
    }
  },

  async getAnotherInfo(ownerId) {
    try {
      const response = await api.get('v1/users/get-another-info/' + ownerId)
      return response.data.data;
    } catch (error) {
      throw error
    }
  }
}
export default authApi;
