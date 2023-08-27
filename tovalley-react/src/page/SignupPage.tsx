import React, { useState } from "react";
import Header from "../component/header/Header";
import Footer from "../component/footer/Footer";
import styles from "../css/user/SignupPage.module.css";
import axios from "axios";

const localhost = "http://localhost:8081";

const SignupPage = () => {
  const [inputInfo, setInputInfo] = useState({
    name: "",
    email: "",
    nickName: "",
  });
  const [password, setPassword] = useState({
    password: "",
    confirmPassword: "",
  });

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

  const checkDuplication = () => {
    const config = {
      params: {
        nickname: inputInfo.nickName,
      },
    };

    axios
      .post(`${localhost}/api/members/check-nickname`, null, config)
      .then((res) => console.log(res))
      .catch((err) => console.log(err));
  };

  return (
    <>
      <Header />
      {/* 회원가입 컨테이너 */}
      <div className={styles.body}>
        <div className={styles.signupContainer}>
          <span>회원가입</span>
          <div className={styles.signupInput}>
            <div>
              <span>이름</span>
              <input
                value={inputInfo.name}
                onChange={(e) => {
                  setInputInfo({ ...inputInfo, name: e.target.value });
                }}
              />
            </div>
            <div>
              <span>이메일</span>
              <input
                value={inputInfo.email}
                onChange={(e) => {
                  setInputInfo({ ...inputInfo, email: e.target.value });
                }}
              />
              <span className={styles.confirmBtn}>확인</span>
            </div>
            <div>
              <span>닉네임</span>
              <input
                value={inputInfo.nickName}
                onChange={(e) => {
                  setInputInfo({ ...inputInfo, nickName: e.target.value });
                }}
              />
              <span className={styles.confirmBtn} onClick={checkDuplication}>
                중복체크
              </span>
            </div>
            <span className={styles.alertNone}>중복된 닉네임입니다.</span>
            <div>
              <span>비밀번호</span>
              <input
                type="password"
                value={password.password}
                onChange={(e) => {
                  setPassword({ ...password, password: e.target.value });
                }}
              />
            </div>
            <div>
              <span>비밀번호 확인</span>
              <input
                type="password"
                value={password.confirmPassword}
                onChange={(e) => {
                  setPassword({ ...password, confirmPassword: e.target.value });
                }}
              />
              <span
                className={
                  password.password === password.confirmPassword
                    ? styles.alertNone
                    : styles.alert
                }
              >
                비밀번호가 일치하지 않습니다.
              </span>
            </div>
          </div>
          <div className={styles.signupBtn}>
            <button>가입하기</button>
          </div>
        </div>
        <div className={styles.socialSignup}>
          <div className={styles.socialSignupTitle}>
            <hr />
            <span>간편 회원가입</span>
            <hr />
          </div>
          <div className={styles.socialSignupLogo}>
            <img
              src={process.env.PUBLIC_URL + "/img/login/kakao-logo.png"}
              alt="kakao logo"
              width="40px"
              onClick={kakaoLogin}
            />
            <img
              src={process.env.PUBLIC_URL + "/img/login/naver-logo.png"}
              alt="naver logo"
              width="40px"
              onClick={naverLogin}
            />
            <img
              src={process.env.PUBLIC_URL + "/img/login/google-logo.png"}
              alt="google logo"
              width="40px"
              onClick={googleLogin}
            />
          </div>
        </div>
      </div>
      <Footer />
    </>
  );
};

export default SignupPage;
