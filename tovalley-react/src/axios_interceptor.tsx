import axios from "axios";

const Axios = axios.create({
  baseURL: process.env.REACT_APP_HOST,
  withCredentials: true,
});

const axiosInstance = axios.create({
  baseURL: process.env.REACT_APP_HOST,
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
        window.location.replace("/login");
      } else if (errResponseStatus === 400) {
        console.log(error.response.data.msg);
        if (errMsg === "여행 일정 추가는 최대 5개까지 가능합니다") {
          alert("여행 일정 추가는 최대 5개까지 가능합니다");
        } else if (errMsg === "로그인 실패") {
          alert("등록되지 않은 회원입니다.");
          window.location.reload();
        } else if (errMsg === "사용 불가능한 닉네임입니다.") {
          alert("사용 불가능한 닉네임입니다.");
        }
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
