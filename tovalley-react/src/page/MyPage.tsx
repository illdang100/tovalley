import React from "react";
import Header from "../component/header/Header";
import Footer from "../component/footer/Footer";
import styles from "../css/user/MyPage.module.css";

const MyPage = () => {
  return (
    <div>
      <Header />
      <div className={styles.body}>
        <div className={styles.userInfo}>
          <h1>마이페이지</h1>
          <div>
            <div>
              <div>
                <img
                  src={process.env.PUBLIC_URL + "/img/계곡test이미지.png"}
                  alt="사용자 프로필 이미지"
                />
                <span>프로필 이미지 변경</span>
              </div>
              <div>
                <span>닉네임</span>
                <span>금오공대</span>
                <span>수정</span>
              </div>
              <div>
                <span>이메일</span>
                <span>kumoh@naver.com</span>
              </div>
            </div>
            <div></div>
          </div>
        </div>
        <div className={styles.schedule}></div>
      </div>
      <Footer />
    </div>
  );
};

export default MyPage;
