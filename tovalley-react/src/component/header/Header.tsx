import React from "react";
import styles from "../../css/header/Header.module.css";
import { useLocation, useNavigate } from "react-router-dom";

const Header = () => {
  const navigation = useNavigate();
  const location = useLocation();

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
