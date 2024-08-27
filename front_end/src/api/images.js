import axios from "axios";

const API_BASE_URL = import.meta.env.VITE_API_BASE_URL;

const api = axios.create({
  baseURL: API_BASE_URL,
  headers: {
    'Content-Type': 'multipart/form-data',
  },
  timeout: 5000,
});


const imageApi = {
  async uploadImage(images) {
    try {
      const token = localStorage.getItem("token");
      const response = await api.post('/v1/images',images , {
        headers: { Authorization: `Bearer ${token}` ,
        },
      });
      return response.data.data;
    } catch (error) {
      throw error;
    }
  },
};
export default imageApi;
