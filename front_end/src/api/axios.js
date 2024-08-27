import axios from "axios";

const API_BASE_URL = import.meta.env.VITE_API_BASE_URL;

const api = axios.create({
  baseURL: API_BASE_URL,
  headers: {
    "Content-Type": "application/json",
  },
  timeout: 10000,
});

// Add an interceptor to include the token in the header for every request
api.interceptors.request.use((request) => {
  const token = localStorage.getItem("token"); // Assuming the token is stored in localStorage
  if (token) {
    request.headers.Authorization = `Bearer ${token}`;
  }
  return request;
});

// Add an interceptor to handle errors
api.interceptors.response.use(
  (response) => response,
  (error) => {
    // Handle error here
    console.error("An error occurred:", error);
    return Promise.reject(error);
  }
);

export default api;
