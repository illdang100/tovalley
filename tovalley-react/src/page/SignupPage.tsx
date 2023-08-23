import React, { useState } from "react";
import Header from "../component/header/Header";
import Footer from "../component/footer/Footer";
import styles from "../css/user/SignupPage.module.css";

const SignupPage = () => {
  const [password, setPassword] = useState({
    password: "",
    confirmPassword: "",
  });

  const KAKAO_AUTH_URL = `http://localhost:8080/oauth2/authorization/kakao`;
  const GOOGLE_AUTH_URL = `http://localhost:8080/oauth2/authorization/google`;

  const kakaoLogin = () => {
    window.location.href = KAKAO_AUTH_URL;
  };

  const googleLogin = () => {
    window.location.href = GOOGLE_AUTH_URL;
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
              <input />
            </div>
            <div>
              <span>이메일</span>
              <input />
              <span className={styles.confirmBtn}>확인</span>
            </div>
            <div>
              <span>닉네임</span>
              <input />
              <span className={styles.confirmBtn}>중복체크</span>
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
