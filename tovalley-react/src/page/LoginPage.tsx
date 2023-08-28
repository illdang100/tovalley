import React, { useState } from "react";
import styles from "../css/user/LoginPage.module.css";
import Header from "../component/header/Header";
import Footer from "../component/footer/Footer";
import axios from "axios";

const localhost = "http://localhost:8081";

const LoginPage = () => {
  const KAKAO_AUTH_URL = `http://localhost:8081/oauth2/authorization/kakao`;
  const GOOGLE_AUTH_URL = `http://localhost:8081/oauth2/authorization/google`;
  const NAVER_AUTH_URL = `http://localhost:8081/oauth2/authorization/naver`;

  const kakaoLogin = () => {
    window.location.href = KAKAO_AUTH_URL;
  };

  const googleLogin = () => {
    window.location.href = GOOGLE_AUTH_URL;
  };

  const naverLogin = () => {
    window.location.href = NAVER_AUTH_URL;
  };

  const [login, setLogin] = useState({
    email: "",
    password: "",
  });

  const handleLogin = () => {
    const data = {
      email: login.email,
      password: login.password,
    };

    axios
      .post(`${localhost}/api/login`, data)
      .then((res) => {
        console.log(res);
      })
      .catch((err) => console.log(err));
  };

  return (
    <>
      <Header />
      {/* 로그인 컨테이너 */}
      <div className={styles.body}>
        <div className={styles.loginContainer}>
          <div className={styles.tovalleyLogo}>
            <img
              src={process.env.PUBLIC_URL + "/img/투계곡-logo.png"}
              alt="tovalley logo"
              width="120px"
            />
          </div>
          {/* 로그인 입력창 */}
          <form className={styles.loginInput}>
            <input
              type="email"
              required
              placeholder="이메일"
              value={login.email}
              onChange={(e) => setLogin({ ...login, email: e.target.value })}
            />
            <input
              type="password"
              required
              placeholder="비밀번호"
              value={login.password}
              onChange={(e) => setLogin({ ...login, password: e.target.value })}
            />
            <button onClick={handleLogin}>로그인</button>
          </form>
          {/* 소셜로그인 */}
          <div className={styles.socialLogin}>
            <div className={styles.socialLoginTitle}>
              <hr />
              <span>간편로그인</span>
              <hr />
            </div>
            <div className={styles.socialLoginLogo}>
              <img
                src={process.env.PUBLIC_URL + "/img/login/kakao-logo.png"}
                alt="kakao logo"
                width="50px"
                onClick={kakaoLogin}
              />
              <img
                src={process.env.PUBLIC_URL + "/img/login/naver-logo.png"}
                alt="naver logo"
                width="50px"
                onClick={naverLogin}
              />
              <img
                src={process.env.PUBLIC_URL + "/img/login/google-logo.png"}
                alt="google logo"
                width="50px"
                onClick={googleLogin}
              />
            </div>
          </div>
        </div>
        {/* 아이디/비밀번호 찾기 */}
        <div className={styles.loginFind}>
          <span>아이디 찾기</span>
          <span>비밀번호 찾기</span>
          <span>회원가입</span>
        </div>
      </div>
      <Footer />
    </>
  );
};

export default LoginPage;
