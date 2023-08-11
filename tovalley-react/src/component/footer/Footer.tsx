import React from "react";
import styles from "../../css/footer/Footer.module.css";

const Footer = () => {
  return (
    <div className={styles.footer}>
      <img
        src={process.env.PUBLIC_URL + "/img/투계곡-logo.png"}
        alt="tovalley logo"
        width="130px"
      />
      <div className={styles.terms}>
        <span>이용약관</span>
        <span>개인정보 처리방침</span>
        <span>문의사항</span>
        <span>관리자 로그인</span>
      </div>
      <div className={styles.engineerInfo}>
        <span>BackEnd</span>
        <div>
          <span>정연준 jyj000217@gmail.com</span>
          <span>손지민 wl2258@kumoh.ac.kr</span>
        </div>
        <span>FrontEnd</span>
        <span>장윤정 yunej27@gmail.com</span>
      </div>
    </div>
  );
};

export default Footer;
