import React, { useEffect, useState } from "react";
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
import axiosInstance from "../axios_interceptor";

type user = {
  userProfile: {
    memberProfileImg: string | null;
    memberName: string;
    memberNick: string;
  };
  myReviews: {
    content: {
      reviewId: number; // 리뷰 Id(PK)
      waterPlaceId: number; // 물놀이 장소 Id(PK)
      waterPlaceName: string; // 물놀이 장소명
      rating: number; // 내가 작성한 평점
      createdReviewDate: string; // 내가 리뷰를 작성한 시간
      content: string; // 내가 작성한 리뷰 내용
      reviewImages: string | null; // 내가 추가한 리뷰 이미지들
      waterQuality: string; // 내가 작성한 수질 정보
    }[];
    pageable: {
      sort: {
        empty: boolean;
        unsorted: boolean;
        sorted: boolean;
      };
      offset: number;
      pageNumber: number;
      pageSize: number;
      paged: boolean;
      unpaged: boolean;
    };
    size: number; // 요청한 응답 개수
    number: number; // 응답된 페이지
    sort: {
      empty: boolean;
      unsorted: boolean;
      sorted: boolean;
    };
    first: boolean; // 첫번째 페이지인지 여부
    last: boolean; // 마지막 페이지인지 여부
    numberOfElements: number; // 조회된 개수
    empty: boolean;
  };
  myUpcomingTripSchedules: // 앞으로의 일정 리스트 (최대 5개)
  {
    tripScheduleId: number; // 여행 일정 Id(PK)
    waterPlaceId: number; // 물놀이 장소 Id(PK)
    waterPlaceName: string; // 물놀이 장소명
    waterPlaceImg: string | null; // 물놀이 장소 이미지
    waterPlaceAddr: string; // 물놀이 장소 주소
    waterPlaceRating: number; // 물놀이 장소 평점
    waterPlaceReviewCnt: number; // 물놀이 장소 리뷰 개수
    waterPlaceTraffic: number; // 물놀이 장소 혼잡도(해당 날짜에 해당 계곡에 가는 인원수)
    tripDate: string; // 내가 계획한 여행 날자
    tripPartySize: number; // 함께 가는 여행 인원수
    rescueSupplies: {
      lifeBoatNum: number; // 인명구조함
      portableStandNum: number; // 이동식거치대
      lifeJacketNum: number; // 구명조끼
      lifeRingNum: number; // 구명환
      rescueRopeNum: number; // 구명로프
      rescueRodNum: number; // 구조봉
    };
    hasReview: boolean; // 리뷰 작성 여부(앞으로의 일정은 리뷰를 작성할 수 없음)
  }[];
};

const MyPage = () => {
  const [user, setUser] = useState<user>({
    userProfile: {
      memberProfileImg: "",
      memberName: "",
      memberNick: "",
    },
    myReviews: {
      content: [
        {
          reviewId: 0,
          waterPlaceId: 0,
          waterPlaceName: "",
          rating: 0,
          createdReviewDate: "",
          content: "",
          reviewImages: "",
          waterQuality: "",
        },
      ],
      pageable: {
        sort: {
          empty: false,
          unsorted: false,
          sorted: false,
        },
        offset: 0,
        pageNumber: 0,
        pageSize: 0,
        paged: false,
        unpaged: false,
      },
      size: 0,
      number: 0,
      sort: {
        empty: false,
        unsorted: false,
        sorted: false,
      },
      first: false,
      last: false,
      numberOfElements: 0,
      empty: false,
    },
    myUpcomingTripSchedules: [
      {
        tripScheduleId: 0,
        waterPlaceId: 0,
        waterPlaceName: "",
        waterPlaceImg: "",
        waterPlaceAddr: "",
        waterPlaceRating: 0,
        waterPlaceReviewCnt: 0,
        waterPlaceTraffic: 0,
        tripDate: "",
        tripPartySize: 0,
        rescueSupplies: {
          lifeBoatNum: 0,
          portableStandNum: 0,
          lifeJacketNum: 0,
          lifeRingNum: 0,
          rescueRopeNum: 0,
          rescueRodNum: 0,
        },
        hasReview: false,
      },
    ],
  });

  useEffect(() => {
    axiosInstance
      .get("/api/auth/my-page")
      .then((res) => {
        console.log(res);
        setUser(res.data.data);
      })
      .catch((err) => console.log(err));

    setUser({
      userProfile: {
        // 사용자 정보 객체
        memberProfileImg: null, // 사용자 프로필 이미지
        memberName: "name", // 사용자 이름
        memberNick: "member1", // 사용자 닉네임
      },
      myReviews: {
        content: [
          {
            reviewId: 11, // 리뷰 Id(PK)
            waterPlaceId: 1001, // 물놀이 장소 Id(PK)
            waterPlaceName: "장흥저수지 상류계곡", // 물놀이 장소명
            rating: 1, // 내가 작성한 평점
            createdReviewDate: "2023-08-22 14:43:15", // 내가 리뷰를 작성한 시간
            content: "content1", // 내가 작성한 리뷰 내용
            reviewImages: null, // 내가 추가한 리뷰 이미지들
            waterQuality: "더러워요", // 내가 작성한 수질 정보
          },
          {
            reviewId: 5,
            waterPlaceId: 1000,
            waterPlaceName: "명곡저수지 상류계곡",
            rating: 1,
            createdReviewDate: "2023-08-22 14:43:15",
            content: "content1",
            reviewImages: null,
            waterQuality: "깨끗해요",
          },
        ],
        pageable: {
          sort: {
            empty: false,
            unsorted: false,
            sorted: true,
          },
          offset: 0,
          pageNumber: 0,
          pageSize: 5,
          paged: true,
          unpaged: false,
        },
        size: 5, // 요청한 응답 개수
        number: 0, // 응답된 페이지
        sort: {
          empty: false,
          unsorted: false,
          sorted: true,
        },
        first: true, // 첫번째 페이지인지 여부
        last: true, // 마지막 페이지인지 여부
        numberOfElements: 2, // 조회된 개수
        empty: false,
      },
      myUpcomingTripSchedules: [
        // 앞으로의 일정 리스트 (최대 5개)
        {
          tripScheduleId: 11, // 여행 일정 Id(PK)
          waterPlaceId: 1002, // 물놀이 장소 Id(PK)
          waterPlaceName: "의령천 구름다리일원", // 물놀이 장소명
          waterPlaceImg: null, // 물놀이 장소 이미지
          waterPlaceAddr: "경상남도 의령군 의령읍 서동리 644-1", // 물놀이 장소 주소
          waterPlaceRating: 0, // 물놀이 장소 평점
          waterPlaceReviewCnt: 0, // 물놀이 장소 리뷰 개수
          waterPlaceTraffic: 1, // 물놀이 장소 혼잡도(해당 날짜에 해당 계곡에 가는 인원수)
          tripDate: "2023-08-25", // 내가 계획한 여행 날자
          tripPartySize: 1, // 함께 가는 여행 인원수
          rescueSupplies: {
            lifeBoatNum: 5, // 인명구조함
            portableStandNum: 2, // 이동식거치대
            lifeJacketNum: 16, // 구명조끼
            lifeRingNum: 5, // 구명환
            rescueRopeNum: 2, // 구명로프
            rescueRodNum: 4, // 구조봉
          },
          hasReview: false, // 리뷰 작성 여부(앞으로의 일정은 리뷰를 작성할 수 없음)
        },
        {
          tripScheduleId: 12,
          waterPlaceId: 1003,
          waterPlaceName: "벽계계곡",
          waterPlaceImg: null,
          waterPlaceAddr: "경상남도 의령군 궁류면 벽계리 산 103",
          waterPlaceRating: 0,
          waterPlaceReviewCnt: 0,
          waterPlaceTraffic: 1,
          tripDate: "2023-08-27",
          tripPartySize: 1,
          rescueSupplies: {
            lifeBoatNum: 3,
            portableStandNum: 3,
            lifeJacketNum: 22,
            lifeRingNum: 2,
            rescueRopeNum: 2,
            rescueRodNum: 2,
          },
          hasReview: false,
        },
        {
          tripScheduleId: 10,
          waterPlaceId: 1001,
          waterPlaceName: "장흥저수지 상류계곡",
          waterPlaceImg: null,
          waterPlaceAddr: "경상남도 양산시 평산동 1070",
          waterPlaceRating: 3.8,
          waterPlaceReviewCnt: 6,
          waterPlaceTraffic: 1,
          tripDate: "2023-09-02",
          tripPartySize: 1,
          rescueSupplies: {
            lifeBoatNum: 4,
            portableStandNum: 0,
            lifeJacketNum: 4,
            lifeRingNum: 4,
            rescueRopeNum: 4,
            rescueRodNum: 1,
          },
          hasReview: false,
        },
      ],
    });
  }, []);

  const [scheduleBtn, setScheduleBtn] = useState("앞으로의 일정");

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
                  src={
                    user.userProfile.memberProfileImg === null
                      ? process.env.PUBLIC_URL + "/img/user-profile.png"
                      : user.userProfile.memberProfileImg
                  }
                  alt="사용자 프로필 이미지"
                  width="180px"
                />
                <span>프로필 이미지 변경</span>
              </div>
              <div className={styles.userNickname}>
                <span>닉네임</span>
                <span>{user.userProfile.memberNick}</span>
                <span>수정</span>
              </div>
              <div className={styles.userEmail}>
                <span>이름</span>
                <span>{user.userProfile.memberName}</span>
              </div>
            </div>
            <div className={styles.myReview}>
              <h4>내 리뷰</h4>
              <div className={styles.reviewContainer}>
                {user.myReviews.content.map((item) => {
                  return (
                    <div className={styles.reivewList}>
                      <img
                        src={
                          item.reviewImages === null
                            ? process.env.PUBLIC_URL + "/img/default-image.png"
                            : item.reviewImages
                        }
                        alt="리뷰 이미지"
                        width="120px"
                      />
                      {item.reviewImages !== null && <span>1</span>}
                      <div className={styles.reviewItem}>
                        <div className={styles.reviewInfo}>
                          <div>
                            <span>
                              <RatingStar rating={item.rating} size="20px" />
                            </span>
                            <span>{item.rating}</span>
                          </div>
                          <span>{item.createdReviewDate}</span>
                        </div>
                        <span>{item.content}</span>
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
                <span
                  onClick={() => {
                    setScheduleBtn("앞으로의 일정");
                  }}
                  style={
                    scheduleBtn === "앞으로의 일정" ? { color: "black" } : {}
                  }
                >
                  앞으로의 일정
                </span>
              </div>
              <div>
                <span
                  onClick={() => {
                    setScheduleBtn("지난 일정");
                  }}
                  style={scheduleBtn === "지난 일정" ? { color: "black" } : {}}
                >
                  지난 일정
                </span>
              </div>
            </div>
            <span>삭제</span>
          </div>
          <div className={styles.scheduleList}>
            {user.myUpcomingTripSchedules.map((item) => {
              return (
                <div className={styles.scheduleItem}>
                  <span className={styles.scheduleCheck}>
                    <MdCheckBoxOutlineBlank color="#66A5FC" size="25px" />
                  </span>
                  <img
                    src={
                      item.waterPlaceImg === null
                        ? process.env.PUBLIC_URL + "/img/default-image.png"
                        : item.waterPlaceImg
                    }
                    alt="계곡 사진"
                    width="180px"
                  />
                  <div className={styles.scheduleInfo}>
                    <h4>{item.waterPlaceName}</h4>
                    <span>{item.waterPlaceAddr}</span>
                    <span>{item.waterPlaceRating}</span>
                    <span>/5</span>
                    <span>리뷰 {item.waterPlaceReviewCnt}개</span>
                    <div className={styles.reservationInfo}>
                      <div>
                        <span>날짜</span>
                        <span>{item.tripDate}</span>
                      </div>
                      <div>
                        <span>인원</span>
                        <span>{item.tripPartySize}</span>
                      </div>
                    </div>
                  </div>
                  <div className={styles.valleyInfo}>
                    <div className={styles.congestion}>
                      <span>계곡 혼잡도</span>
                      <div
                        style={
                          item.waterPlaceTraffic >= 15
                            ? { backgroundColor: "#FA7F64" }
                            : item.waterPlaceTraffic >= 10
                            ? { backgroundColor: "#FFD874" }
                            : item.waterPlaceTraffic >= 5
                            ? { backgroundColor: "#8EBBFF" }
                            : { backgroundColor: "#E0E0E0" }
                        }
                      ></div>
                    </div>
                    <div className={styles.rescueList}>
                      <div className={styles.rescueItem}>
                        <span>
                          <TbChartDonut4 size="40px" color="#66A5FC" />
                        </span>
                        <span>{item.rescueSupplies.lifeRingNum}</span>
                      </div>
                      <div className={styles.rescueItem}>
                        <span>
                          <TbJumpRope size="40px" color="#66A5FC" />
                        </span>
                        <span>{item.rescueSupplies.rescueRopeNum}</span>
                      </div>
                      <div className={styles.rescueItem}>
                        <span>
                          <MdEmojiPeople size="40px" color="#66A5FC" />
                        </span>
                        <span>{item.rescueSupplies.lifeBoatNum}</span>
                      </div>
                      <div className={styles.rescueItem}>
                        <span>
                          <FaVest size="40px" color="#66A5FC" />
                        </span>
                        <span>{item.rescueSupplies.lifeJacketNum}</span>
                      </div>
                      <div className={styles.rescueItem}>
                        <span>
                          <MdHomeRepairService size="40px" color="#66A5FC" />
                        </span>
                        <span>{item.rescueSupplies.portableStandNum}</span>
                      </div>
                      <div className={styles.rescueItem}>
                        <span>
                          <LuUtilityPole size="40px" color="#66A5FC" />
                        </span>
                        <span>{item.rescueSupplies.rescueRodNum}</span>
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
