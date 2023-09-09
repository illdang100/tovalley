import axios from "axios";

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
        return await axios
          .post(`http://localhost:8081/api/token/refresh`, null)
          .then(async (res) => {
            console.log(res);

            return await axios(prevRequest);
          })
          .catch((e) => {
            console.log("토큰 재발급 실패");
            if (e.response.status === 401) {
              alert(e.response.data.msg);
              window.location.replace("/login");
            } else if (e.response.status === 403) {
              alert(e.response.data.msg);
              axios.delete(`http://localhost:8081/api/auth/logout`);
              window.location.replace("/");
            }
          });
      } else if (errResponseStatus === 400) {
        console.log(error.response.data.msg);
      } else if (errResponseStatus === 401) {
        console.log("인증 실패");
      } else if (errResponseStatus === 403) {
        alert("권한이 없습니다.");
      }
    } catch (e) {
      return Promise.reject(e);
    }
  }
);

export default axiosInstance;
