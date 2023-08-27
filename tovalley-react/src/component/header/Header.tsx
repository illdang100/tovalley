import React, { useEffect, useState } from "react";
import styles from "../../css/header/Header.module.css";
import { useLocation, useNavigate } from "react-router-dom";
import { Cookies } from "react-cookie";
import axios from "axios";

const cookies = new Cookies();
const localhost = "http://localhost:8081";

const Header = () => {
  const navigation = useNavigate();
  const location = useLocation();
  const [login, setLogin] = useState(false);

  useEffect(() => {
    const loginStatus = cookies.get("ISLOGIN");
    console.log("loginStatus : ", loginStatus);

    if (loginStatus === true) {
      setLogin(true);
    }
  }, [login]);

  const handleLogout = () => {
    axios
      .delete(`${localhost}/api/auth/logout`)
      .then((res) => console.log(res));
    setLogin(false);
  };

  return (
    <div className={styles.header}>
      <div className={styles.headertop}>
        <div
          className={styles.logo}
          onClick={() => {
            navigation("/");
          }}
        >
          <img
            src={process.env.PUBLIC_URL + "/img/투계곡-logo.png"}
            alt="tovalley logo"
            width="120px"
          />
        </div>
        {login ? (
          <div className={styles.login}>
            <span>마이페이지</span>
            <span onClick={handleLogout}>로그아웃</span>
          </div>
        ) : (
          <div className={styles.signup}>
            <span
              onClick={() => {
                navigation("/signup");
              }}
            >
              회원가입
            </span>
            <span
              onClick={() => {
                navigation("/login");
              }}
            >
              로그인
            </span>
          </div>
        )}
      </div>
      <div className={styles.nav}>
        <span
          onClick={() => {
            navigation("/valleylist");
          }}
          className={
            location.pathname === "/valleylist"
              ? styles.navMenuClicked
              : styles.navMenu
          }
        >
          전국 계곡
        </span>
        <span
          onClick={() => {
            navigation("/guide");
          }}
          className={
            location.pathname === "/guide"
              ? styles.navMenuClicked
              : styles.navMenu
          }
        >
          안전 가이드
        </span>
      </div>
    </div>
  );
};

export default Header;
