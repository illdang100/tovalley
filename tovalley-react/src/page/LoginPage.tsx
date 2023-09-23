import React, { FC, useEffect, useState } from "react";
import styles from "../css/user/LoginPage.module.css";
import Header from "../component/header/Header";
import Footer from "../component/footer/Footer";
import axios from "axios";
import { MdOutlineClose } from "react-icons/md";
import { useNavigate } from "react-router-dom";
import axiosInstance from "./../axios_interceptor";

const localhost = "http://13.125.136.237";

const LoginPage = () => {
  const KAKAO_AUTH_URL = `http://ec2-13-125-136-237.ap-northeast-2.compute.amazonaws.com:8080/oauth2/authorization/kakao`;
  const GOOGLE_AUTH_URL = `http://ec2-13-125-136-237.ap-northeast-2.compute.amazonaws.com:8080/oauth2/authorization/google`;
  const NAVER_AUTH_URL = `http://ec2-13-125-136-237.ap-northeast-2.compute.amazonaws.com:8080/oauth2/authorization/naver`;

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

  const [findView, setFindView] = useState({
    findId: false,
    findPassword: false,
  });

  const navigation = useNavigate();

  const handleLogin = () => {
    const data = {
      username: login.email,
      password: login.password,
    };

    axiosInstance
      .post("/api/login", data)
      .then((res) => {
        console.log(res);
        res.status === 200 && navigation("/");
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
          <div className={styles.loginInput}>
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
            <button onClick={() => handleLogin()}>로그인</button>
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
          <span
            onClick={() => {
              setFindView({ ...findView, findId: true });
            }}
          >
            아이디 찾기
          </span>
          <span
            onClick={() => {
              setFindView({ ...findView, findPassword: true });
            }}
          >
            비밀번호 찾기
          </span>
          <span
            onClick={() => {
              navigation("/signup");
            }}
          >
            회원가입
          </span>
        </div>
        {findView.findId && (
          <FindInfo
            setFindView={setFindView}
            info={{ title: "아이디(이메일)", findKind: "아이디" }}
          />
        )}
        {findView.findPassword && (
          <FindInfo
            setFindView={setFindView}
            info={{ title: "비밀번호", findKind: "비밀번호" }}
          />
        )}
      </div>
      <Footer />
    </>
  );
};

interface Props {
  setFindView: React.Dispatch<
    React.SetStateAction<{
      findId: boolean;
      findPassword: boolean;
    }>
  >;
  info: {
    title: string;
    findKind: string;
  };
}

const FindInfo: FC<Props> = ({ setFindView, info }) => {
  useEffect(() => {
    document.body.style.cssText = `
      position: fixed; 
      top: -${window.scrollY}px;
      overflow-y: scroll;
      width: 100%;`;
    return () => {
      const scrollY = document.body.style.top;
      document.body.style.cssText = "";
      window.scrollTo(0, parseInt(scrollY || "0", 10) * -1);
    };
  }, []);

  const [view, setView] = useState({
    codeView: true,
    passwordReset: false,
    emailConfirm: 0,
  });

  const [inputInfo, setInputInfo] = useState({
    email: "",
    code: "",
    password: "",
    confirmPassword: "",
  });

  const handleFindId = () => {
    const config = {
      params: {
        email: inputInfo.email,
      },
    };

    axios
      .get(`${localhost}/api/members/find-id`, config)
      .then((res) => {
        console.log(res);
        res.status === 200
          ? setView({ ...view, emailConfirm: 1 })
          : setView({ ...view, emailConfirm: 2 });
      })
      .then((err) => console.log(err));
  };

  const authEmail = () => {
    const data = {
      email: inputInfo.email,
    };
    axios
      .post(`${localhost}/api/email-code`, data)
      .then((res) => {
        console.log(res);
        res.status === 200 && setView({ ...view, codeView: true });
      })
      .catch((err) => console.log(err));
  };

  const authCode = () => {
    const config = {
      params: {
        email: inputInfo.email,
        verifyCode: inputInfo.code,
      },
    };

    axios
      .get(`${localhost}/api/email-code`, config)
      .then((res) => {
        console.log(res);
        res.status === 200 && setView({ ...view, passwordReset: true });
      })
      .catch((err) => console.log(err));
  };

  const handleResetPassword = () => {
    const data = {
      email: inputInfo.email,
      newPassword: inputInfo.password,
    };

    inputInfo.password === inputInfo.confirmPassword &&
      axios
        .post(`${localhost}/api/members/reset-password`, data)
        .then((res) => {
          console.log(res);
          res.status === 200 &&
            setFindView({ findId: false, findPassword: false });
        })
        .catch((err) => console.log(err));
  };

  return (
    <div className={styles.findInfoContainer}>
      <div className={styles.findInfoModal}>
        <span
          className={styles.findInfoClose}
          onClick={() => setFindView({ findId: false, findPassword: false })}
        >
          <MdOutlineClose color="#B8B8B8" size="30px" />
        </span>
        {!view.passwordReset ? (
          <>
            <h1>{info.title} 찾기</h1>
            <span>
              {info.findKind === "아이디"
                ? "회원가입 시 등록했던 이메일을 입력하세요"
                : "인증 코드를 받을 이메일을 입력하세요."}
            </span>
            <div className={styles.findInfoInput}>
              <input
                placeholder="이메일"
                value={inputInfo.email}
                onChange={(e) =>
                  setInputInfo({ ...inputInfo, email: e.target.value })
                }
              />
              <button
                onClick={() => {
                  info.findKind === "아이디" ? handleFindId() : authEmail();
                }}
              >
                {info.findKind} 찾기
              </button>
              <span>
                {" "}
                {view.emailConfirm === 0
                  ? ""
                  : view.emailConfirm === 1
                  ? `아이디는 ${inputInfo.email} 입니다.`
                  : "등록된 이메일이 아닙니다."}
              </span>
            </div>
            {view.codeView && (
              <div className={styles.confirmCode}>
                <span>인증 코드가 메일로 전송되었습니다.</span>
                <span className={styles.timer}>00:00</span>
                <div className={styles.confirmCodeInput}>
                  <input
                    placeholder="확인 코드"
                    value={inputInfo.code}
                    onChange={(e) =>
                      setInputInfo({ ...inputInfo, code: e.target.value })
                    }
                  />
                  <button onClick={authCode}>확인</button>
                </div>
              </div>
            )}
          </>
        ) : (
          <div className={styles.passwordReset}>
            <h1>비밀번호 재설정</h1>
            <span>재설정 할 비밀번호를 입력하세요.</span>
            <div className={styles.findInfoInput}>
              <input
                placeholder="비밀번호"
                type="password"
                value={inputInfo.password}
                onChange={(e) =>
                  setInputInfo({ ...inputInfo, password: e.target.value })
                }
              />
              <input
                placeholder="비밀번호 확인"
                type="password"
                value={inputInfo.confirmPassword}
                onChange={(e) =>
                  setInputInfo({
                    ...inputInfo,
                    confirmPassword: e.target.value,
                  })
                }
              />
              {inputInfo.password !== inputInfo.confirmPassword && (
                <p className={styles.confirmPassword}>
                  비밀번호가 일치하지 않습니다.
                </p>
              )}
              <button onClick={handleResetPassword}>확인</button>
            </div>
          </div>
        )}
      </div>
    </div>
  );
};

export default LoginPage;
