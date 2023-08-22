import React from "react";
import styles from "../css/user/LoginPage.module.css";
import Header from "../component/header/Header";
import Footer from "../component/footer/Footer";

const LoginPage = () => {
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
          <div className={styles.loginInput}>
            <input placeholder="이메일" />
            <input placeholder="비밀번호" />
            <button>로그인</button>
          </div>
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
