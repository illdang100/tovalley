import React from "react";
import Header from "../component/header/Header";
import Footer from "../component/footer/Footer";
import styles from "../css/user/MyPage.module.css";
import RatingStar from "../component/common/RatingStar";

const MyPage = () => {
  const review = [
    {
      rating: 2.7,
      writeDate: "2023.07.30",
      content: "물 개더러움 ㄷㄷ",
    },
    {
      rating: 4.3,
      writeDate: "2023.07.30",
      content: "물 개더러움 ㄷㄷ",
    },
  ];
  return (
    <div>
      <Header />
      <div className={styles.body}>
        <div className={styles.myPage}>
          <h1>마이페이지</h1>
          <div className={styles.userInfo}>
            <div className={styles.userBasicInfo}>
              <span>기본정보</span>
              <div className={styles.profileImg}>
                <img
                  src={process.env.PUBLIC_URL + "/img/계곡test이미지.png"}
                  alt="사용자 프로필 이미지"
                  width="180px"
                />
                <span>프로필 이미지 변경</span>
              </div>
              <div className={styles.userNickname}>
                <span>닉네임</span>
                <span>금오공대</span>
                <span>수정</span>
              </div>
              <div className={styles.userEmail}>
                <span>이메일</span>
                <span>kumoh@naver.com</span>
              </div>
            </div>
            <div className={styles.myReview}>
              <h4>내 리뷰</h4>
              {review.map((item) => {
                return (
                  <div>
                    <img
                      src={process.env.PUBLIC_URL + "/img/계곡test이미지.png"}
                      alt="리뷰 이미지"
                      width="100px"
                    />
                    <span>
                      <RatingStar rating={item.rating} size="20px" />
                    </span>
                  </div>
                );
              })}
            </div>
          </div>
        </div>
        <div className={styles.schedule}></div>
      </div>
      <Footer />
    </div>
  );
};

export default MyPage;
