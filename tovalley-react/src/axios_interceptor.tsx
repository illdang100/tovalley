import axios from "axios";

const Axios = axios.create({
  baseURL: "http://localhost:8081",
  withCredentials: true,
});

const axiosInstance = axios.create({
  baseURL: "http://localhost:8081",
  withCredentials: true,
});

axiosInstance.interceptors.response.use(
  (res) => {
    console.log(res);
    return res;
  },
  async (error) => {
    try {
      const errResponseStatus = error.response.status;
      const prevRequest = error.config;
      const errMsg = error.response.data.msg;

      if (errResponseStatus === 400 && errMsg === "만료된 토큰입니다.") {
        Axios.delete("/api/auth/logout");
        window.location.replace("/login");
      } else if (errResponseStatus === 400) {
        console.log(error.response.data.msg);
      } else if (errResponseStatus === 401) {
        console.log("인증 실패");
        return Axios.request(prevRequest);
      } else if (errResponseStatus === 403) {
        alert("권한이 없습니다.");
      }
    } catch (e) {
      return Promise.reject(e);
    }
  }
);

export default axiosInstance;
