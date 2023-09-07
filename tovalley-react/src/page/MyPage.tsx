import React from "react";
import Header from "../component/header/Header";
import Footer from "../component/footer/Footer";
import styles from "../css/user/MyPage.module.css";
import RatingStar from "../component/common/RatingStar";
import { TbChartDonut4, TbJumpRope } from "react-icons/tb";
import {
  MdEmojiPeople,
  MdHomeRepairService,
  MdCheckBoxOutlineBlank,
  MdCheckBox,
} from "react-icons/md";
import { FaVest } from "react-icons/fa";
import { LuUtilityPole } from "react-icons/lu";

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
    {
      rating: 4.3,
      writeDate: "2023.07.30",
      content: "물 개더러움 ㄷㄷ",
    },
    {
      rating: 4.3,
      writeDate: "2023.07.30",
      content: "물 개더러움 ㄷㄷ",
    },
  ];

  const schedule = [
    {
      title: "금오계곡",
      address: "경상북도 구미시",
      rating: 4.7,
      reviewCnt: 195,
      scheduleDate: "2023.08.20",
      people: 6,
      congestion: "혼잡",
    },
    {
      title: "금오계곡",
      address: "경상북도 구미시",
      rating: 4.7,
      reviewCnt: 195,
      scheduleDate: "2023.08.20",
      people: 6,
      congestion: "혼잡",
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
              <div className={styles.reviewContainer}>
                {review.map((item) => {
                  return (
                    <div className={styles.reivewList}>
                      <img
                        src={process.env.PUBLIC_URL + "/img/계곡test이미지.png"}
                        alt="리뷰 이미지"
                        width="120px"
                      />
                      <span>2</span>
                      <div className={styles.reviewItem}>
                        <div className={styles.reviewInfo}>
                          <div>
                            <span>
                              <RatingStar rating={item.rating} size="20px" />
                            </span>
                            <span>2</span>
                          </div>
                          <span>2023.07.30</span>
                        </div>
                        <span>물 개더러움 ㄷㄷ</span>
                      </div>
                    </div>
                  );
                })}
              </div>
            </div>
          </div>
        </div>
        <div className={styles.schedule}>
          <div className={styles.scheduleControl}>
            <div className={styles.scheduleBtn}>
              <div>
                <span>앞으로의 일정</span>
              </div>
              <div>
                <span>지난 일정</span>
              </div>
            </div>
            <span>삭제</span>
          </div>
          <div className={styles.scheduleList}>
            {schedule.map((item) => {
              return (
                <div className={styles.scheduleItem}>
                  <span className={styles.scheduleCheck}>
                    <MdCheckBoxOutlineBlank color="#66A5FC" size="25px" />
                  </span>
                  <img
                    src={process.env.PUBLIC_URL + "/img/계곡test이미지.png"}
                    alt="계곡 사진"
                    width="180px"
                  />
                  <div className={styles.scheduleInfo}>
                    <h4>{item.title}</h4>
                    <span>{item.address}</span>
                    <span>{item.rating}</span>
                    <span>/5</span>
                    <span>리뷰 {item.reviewCnt}개</span>
                    <div className={styles.reservationInfo}>
                      <div>
                        <span>날짜</span>
                        <span>{item.scheduleDate}</span>
                      </div>
                      <div>
                        <span>인원</span>
                        <span>{item.people}</span>
                      </div>
                    </div>
                  </div>
                  <div className={styles.valleyInfo}>
                    <div className={styles.congestion}>
                      <span>계곡 혼잡도</span>
                      <div></div>
                    </div>
                    <div className={styles.rescueList}>
                      <div className={styles.rescueItem}>
                        <span>
                          <TbChartDonut4 size="40px" color="#66A5FC" />
                        </span>
                        <span>10</span>
                      </div>
                      <div className={styles.rescueItem}>
                        <span>
                          <TbJumpRope size="40px" color="#66A5FC" />
                        </span>
                        <span>10</span>
                      </div>
                      <div className={styles.rescueItem}>
                        <span>
                          <MdEmojiPeople size="40px" color="#66A5FC" />
                        </span>
                        <span>10</span>
                      </div>
                      <div className={styles.rescueItem}>
                        <span>
                          <FaVest size="40px" color="#66A5FC" />
                        </span>
                        <span>10</span>
                      </div>
                      <div className={styles.rescueItem}>
                        <span>
                          <MdHomeRepairService size="40px" color="#66A5FC" />
                        </span>
                        <span>10</span>
                      </div>
                      <div className={styles.rescueItem}>
                        <span>
                          <LuUtilityPole size="40px" color="#66A5FC" />
                        </span>
                        <span>10</span>
                      </div>
                    </div>
                  </div>
                </div>
              );
            })}
          </div>
        </div>
      </div>
      <Footer />
    </div>
  );
};

export default MyPage;
