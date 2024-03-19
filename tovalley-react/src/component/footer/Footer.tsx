import React from "react";
import styles from "../../css/footer/Footer.module.css";

const localhost = process.env.REACT_APP_HOST;

const Footer = () => {
  return (
    <div className={styles.footer}>
      <img
        src={process.env.PUBLIC_URL + "/img/투계곡-logo.png"}
        alt="tovalley logo"
        width="110px"
      />
      <div className={styles.terms}>
        <span>이용약관</span>
        <span>개인정보 처리방침</span>
        <span>문의사항</span>
        <span
          onClick={() => window.location.replace(`${localhost}/th/admin-login`)}
        >
          관리자 로그인
        </span>
      </div>
      <div className={styles.engineerInfo}>
        <div className={styles.backend}>
          <span>BackEnd</span>
          <div>
            <span>정연준 jyj000217@gmail.com</span>
            <span>손지민 wl2258@kumoh.ac.kr</span>
          </div>
        </div>
        <div className={styles.frontend}>
          <span>FrontEnd</span>
          <span>장하정 zhz0207@naver.com</span>
        </div>
      </div>
    </div>
  );
};

export default Footer;
