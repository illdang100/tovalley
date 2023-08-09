import React, { useState } from "react";
import styles from "../../css/header/Header.module.css";
import { useNavigate } from "react-router-dom";

const Header = () => {
  const menu: string[] = ["전국 계곡", "안전가이드"];
  const [clicked, setClicked] = useState("");
  const navigation = useNavigate();

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
            width="130px"
          />
        </div>
        <div className={styles.signup}>
          <span>회원가입</span>
          <span>로그인</span>
        </div>
      </div>
      <div className={styles.nav}>
        {menu.map((item) => {
          return (
            <span
              onClick={() => {
                setClicked(`${item}`);
              }}
              className={
                clicked === item ? styles.navMenuClicked : styles.navMenu
              }
            >
              {item}
            </span>
          );
        })}
      </div>
    </div>
  );
};

export default Header;
