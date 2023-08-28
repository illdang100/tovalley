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
  const [available, setAvailable] = useState({
    check: false,
    available: 0,
    alert: "한글, 영어, 숫자 포함 20자 이내",
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

  const checkNickname = (e: React.ChangeEvent<HTMLInputElement>) => {
    const regExp = /^[가-힣a-zA-Z0-9]{1,10}$/;
    if (regExp.test(e.target.value) === true) {
      setAvailable({ ...available, check: true });
    } else {
      setAvailable({ ...available, check: false });
    }
  };

  const checkDuplication = () => {
    const data = {
      nickname: inputInfo.nickName,
    };

    axios
      .post(`${localhost}/api/members/check-nickname`, data)
      .then((res) => {
        console.log(res);
        if (res.status === 200) {
          setAvailable({
            ...available,
            available: 1,
            alert: res.data.msg,
          });
        }
      })
      .catch((err) => {
        console.log(err);
        if (err.response.status === 400) {
          setAvailable({
            ...available,
            available: 2,
            alert: err.response.data.msg,
          });
        }
      });
  };

  const handleSignUp = () => {
    const data = {
      name: inputInfo.name,
      email: inputInfo.email,
      nickname: inputInfo.nickName,
      password: password.password,
    };

    if (
      password.password === password.confirmPassword &&
      available.available === 1
    ) {
      axios
        .post(`${localhost}/api/members`, data)
        .then((res) => console.log(res))
        .catch((err) => console.log(err));
    }
  };

  return (
    <>
      <Header />
      {/* 회원가입 컨테이너 */}
      <div className={styles.body}>
        <form className={styles.signupContainer}>
          <span>회원가입</span>
          <div className={styles.signupInput}>
            <div>
              <span>이름</span>
              <input
                required
                value={inputInfo.name}
                onChange={(e) => {
                  setInputInfo({ ...inputInfo, name: e.target.value });
                }}
              />
            </div>
            <div>
              <span>이메일</span>
              <input
                type="email"
                required
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
                required
                value={inputInfo.nickName}
                onChange={(e) => {
                  setInputInfo({ ...inputInfo, nickName: e.target.value });
                }}
                onBlur={checkNickname}
                maxLength={20}
              />
              <span
                className={
                  available.check ? styles.confirmBtn : styles.disableBtn
                }
                onClick={() => {
                  available.check && checkDuplication();
                }}
              >
                중복체크
              </span>
              <span className={styles.charCnt}>
                {inputInfo.nickName.length}/20
              </span>
            </div>
            <span
              className={
                available.available === 0
                  ? styles.alertDefault
                  : available.available === 1
                  ? styles.availAlert
                  : styles.alert
              }
            >
              {available.alert}
            </span>
            <div>
              <span>비밀번호</span>
              <input
                required
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
                required
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
            <button onClick={handleSignUp}>가입하기</button>
          </div>
        </form>
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
