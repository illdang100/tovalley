import React, { FC, useEffect, useState } from "react";
import styles from "../css/user/LoginPage.module.css";
import Header from "../component/header/Header";
import Footer from "../component/footer/Footer";
import axios from "axios";
import { MdOutlineClose } from "react-icons/md";
import { useNavigate } from "react-router-dom";

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

  const [findView, setFindView] = useState({
    findId: false,
    findPassword: false,
  });

  const navigation = useNavigate();

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

  const [codeView, setCodeView] = useState(false);
  const [passwordReset, setPasswordReset] = useState(false);

  return (
    <div className={styles.findInfoContainer}>
      <div className={styles.findInfoModal}>
        <span
          className={styles.findInfoClose}
          onClick={() => setFindView({ findId: false, findPassword: false })}
        >
          <MdOutlineClose color="#B8B8B8" size="30px" />
        </span>
        {!passwordReset ? (
          <>
            <h1>{info.title} 찾기</h1>
            <span>인증 코드를 받을 이메일과 성함을 입력하세요.</span>
            <div className={styles.findInfoInput}>
              <input placeholder="이름" />
              <input placeholder="이메일" />
              <button>{info.findKind} 찾기</button>
            </div>
            {codeView && (
              <div className={styles.confirmCode}>
                <span>인증 코드가 메일로 전송되었습니다.</span>
                <div className={styles.confirmCodeInput}>
                  <input placeholder="확인 코드" />
                  <button>확인</button>
                </div>
              </div>
            )}
          </>
        ) : (
          <div className={styles.passwordReset}>
            <h1>비밀번호 재설정</h1>
            <span>재설정 할 비밀번호를 입력하세요.</span>
            <div className={styles.findInfoInput}>
              <input placeholder="비밀번호" type="password" />
              <input placeholder="비밀번호 확인" type="password" />
              <button>확인</button>
            </div>
          </div>
        )}
      </div>
    </div>
  );
};

export default LoginPage;
