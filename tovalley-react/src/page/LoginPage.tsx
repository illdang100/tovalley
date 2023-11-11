import React, { FC, useEffect, useState } from "react";
import styles from "../css/user/LoginPage.module.css";
import Header from "../component/header/Header";
import Footer from "../component/footer/Footer";
import axios from "axios";
import { MdOutlineClose } from "react-icons/md";
import { useNavigate } from "react-router-dom";
import axiosInstance from "./../axios_interceptor";
import { Cookies } from "react-cookie";

const localhost = process.env.REACT_APP_HOST;
const social_localhost = process.env.REACT_APP_SOCIAL_HOST;
const cookies = new Cookies();

const LoginPage = () => {
  const KAKAO_AUTH_URL = `${social_localhost}/oauth2/authorization/kakao`;
  const GOOGLE_AUTH_URL = `${social_localhost}/oauth2/authorization/google`;
  const NAVER_AUTH_URL = `${social_localhost}/oauth2/authorization/naver`;

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
    passwordConfirm: false,
  });

  const [findView, setFindView] = useState({
    findId: false,
    findPassword: false,
  });

  const navigation = useNavigate();

  useEffect(() => {
    const social_login_error = cookies.get("social_login_error");
    console.log("social_login_error : ", social_login_error);

    if (social_login_error === "email_already_registered") {
      alert("이미 자체 회원가입으로 등록된 회원입니다.");
      cookies.remove("social_login_error");
    }
  }, []);

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
      .catch((err) => {
        console.log(err);
        err.response.status === 400
          ? setLogin({ ...login, passwordConfirm: true })
          : console.log(err);
      });
  };

  return (
    <div className={styles.loginPage}>
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
              onKeyDown={(e) => {
                if (e.key === "Enter") {
                  handleLogin();
                }
              }}
            />
            {login.passwordConfirm && (
              <span className={styles.passwordAlert}>
                비밀번호가 틀렸습니다.
              </span>
            )}
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
                // onClick={kakaoLogin}
                onClick={() => navigation("/social-login-exception")}
              />
              <img
                src={process.env.PUBLIC_URL + "/img/login/naver-logo.png"}
                alt="naver logo"
                width="50px"
                // onClick={naverLogin}
                onClick={() => navigation("/social-login-exception")}
              />
              <img
                src={process.env.PUBLIC_URL + "/img/login/google-logo.png"}
                alt="google logo"
                width="50px"
                // onClick={googleLogin}
                onClick={() => navigation("/social-login-exception")}
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
    </div>
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
    setView({ ...view, codeView: false });
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
    resendView: false,
  });

  const [inputInfo, setInputInfo] = useState({
    email: "",
    code: "",
    password: "",
    confirmPassword: "",
  });

  const MINUTES_IN_MS = 3 * 60 * 1000;
  const INTERVAL = 1000;
  const [timeLeft, setTimeLeft] = useState<number>(MINUTES_IN_MS);

  const minutes = String(Math.floor((timeLeft / (1000 * 60)) % 60)).padStart(
    2,
    "0"
  );
  const second = String(Math.floor((timeLeft / 1000) % 60)).padStart(2, "0");

  useEffect(() => {
    const timer = setInterval(() => {
      setTimeLeft((prevTime) => prevTime - INTERVAL);
    }, INTERVAL);

    if (timeLeft <= 0) {
      clearInterval(timer);
      setTimeLeft(0);
      setView({ ...view, resendView: true });
    }

    return () => {
      clearInterval(timer);
    };
  }, [timeLeft]);

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
        res.status === 200 && setView({ ...view, emailConfirm: 1 });
      })
      .catch((err) => {
        console.log(err);
        if (err.response.status === 400) {
          setView({ ...view, emailConfirm: 2 });
        }
      });
  };

  const authEmail = () => {
    const data = {
      email: inputInfo.email,
    };

    setInputInfo({ ...inputInfo, code: "" });
    setView({ ...view, codeView: true, resendView: false });

    axios
      .post(`${localhost}/api/email-code`, data)
      .then((res) => {
        console.log(res);
        setTimeLeft(MINUTES_IN_MS);
      })
      .catch((err) => {
        console.log(err);
        if (err.response.status === 400) {
          if (err.response.data.msg === "유효성검사 실패") {
            alert("이메일 형식을 맞춰주세요.");
          } else if (err.response.data.msg === "이메일 수신함을 확인하세요") {
            alert(err.response.data.msg);
          } else {
            setTimeLeft(0);
            setView({ ...view, resendView: true });
          }
        }
      });
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
      .catch((err) => {
        console.log(err);
        if (err.response.status === 400) {
          setInputInfo({ ...inputInfo, code: "" });
          alert("잠시 후 다시 시도해주세요.");
        }
      });
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
                  ? "등록된 이메일입니다."
                  : "등록된 이메일이 아닙니다."}
              </span>
            </div>
            {view.codeView && (
              <div className={styles.confirmCode}>
                <span>인증 코드가 메일로 전송되었습니다.</span>
                <span className={styles.timer}>
                  {minutes} : {second}
                </span>
                <div className={styles.confirmCodeInput}>
                  <input
                    placeholder="확인 코드"
                    value={inputInfo.code}
                    onChange={(e) =>
                      setInputInfo({ ...inputInfo, code: e.target.value })
                    }
                  />
                  {!view.resendView ? (
                    <button onClick={authCode}>확인</button>
                  ) : (
                    <button
                      onClick={() => {
                        setInputInfo({ ...inputInfo, code: "" });
                        setTimeLeft(MINUTES_IN_MS);
                        authEmail();
                        setView({ ...view, resendView: false });
                      }}
                    >
                      재전송
                    </button>
                  )}
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
