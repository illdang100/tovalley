import React, { FC, useEffect, useRef, useState } from "react";
import Header from "../component/header/Header";
import Footer from "../component/footer/Footer";
import styles from "../css/user/MyPage.module.css";
import RatingStar from "../component/common/RatingStar";
import axiosInstance from "../axios_interceptor";
import ConfirmModal from "../component/common/ConfirmModal";
import TripSchedule from "../component/user/TripSchedule";

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

type preSchedule = {
  content: {
    tripScheduleId: number;
    waterPlaceId: number;
    waterPlaceName: string;
    waterPlaceImg: string | null;
    waterPlaceAddr: string;
    waterPlaceRating: number | string;
    waterPlaceReviewCnt: number | string;
    waterPlaceTraffic: number;
    tripDate: string;
    tripPartySize: number;
    rescueSupplies: {
      lifeBoatNum: number;
      portableStandNum: number;
      lifeJacketNum: number;
      lifeRingNum: number;
      rescueRopeNum: number;
      rescueRodNum: number;
    };
    hasReview: boolean;
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
  number: number;
  sort: {
    empty: boolean;
    unsorted: boolean;
    sorted: boolean;
  };
  first: boolean;
  last: boolean;
  size: number;
  numberOfElements: number;
  empty: boolean;
};

const MyPage = () => {
  const [nickUpdate, setNickUpdate] = useState({
    click: false,
    duplicateCheck: false,
    inputNick: "",
    available: false,
  });
  const [confirmView, setConfirmView] = useState({
    view: false,
    content: "",
  });
  const [changeImg, setChangeImg] = useState<{
    modal: boolean;
    imgFile: string | null | ArrayBuffer;
  }>({
    modal: false,
    imgFile: "",
  });

  const imgRef = useRef<HTMLInputElement>(null);
  const [scheduleBtn, setScheduleBtn] = useState("앞으로의 일정");

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

  const [preSchedule, setPreSchedule] = useState<preSchedule>({
    content: [
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
    number: 0,
    sort: {
      empty: false,
      unsorted: false,
      sorted: false,
    },
    first: false,
    last: false,
    size: 0,
    numberOfElements: 0,
    empty: false,
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

  const checkNickname = (e: React.ChangeEvent<HTMLInputElement>) => {
    const regExp = /^[가-힣a-zA-Z0-9]{1,10}$/;
    if (regExp.test(e.target.value) === true) {
      setNickUpdate({ ...nickUpdate, available: true });
    } else {
      setNickUpdate({ ...nickUpdate, available: false });
    }
  };

  const checkDuplication = () => {
    const data = {
      nickname: nickUpdate.inputNick,
    };

    nickUpdate.available
      ? axiosInstance
          .post(`/api/members/check-nickname`, data)
          .then((res) => {
            console.log(res);
            if (res.status === 200) {
              setConfirmView({
                view: true,
                content: "사용 가능한 닉네임입니다.",
              });
              setNickUpdate({ ...nickUpdate, duplicateCheck: true });
            }
          })
          .catch((err) => {
            console.log(err);
            if (err.response.status === 400) {
              setConfirmView({
                view: true,
                content: "사용 불가능한 닉네임입니다.",
              });
            }
          })
      : setConfirmView({
          view: true,
          content: "한/영, 숫자 포함 20자 이내로 작성해주세요.",
        });
  };

  const handleResetNicname = () => {
    const data = {
      nickname: nickUpdate.inputNick,
    };

    axiosInstance
      .post("/api/members/set-nickname", data)
      .then((res) => {
        console.log(res);
        setNickUpdate({ ...nickUpdate, click: false });
      })
      .catch((err) => console.log(err));
  };

  //   const handleDeleteSchedule = () => {
  //     deleteScheduleArr = user.myUpcomingTripSchedules;

  //     for (let i = 0; i < deleteSchedule.length; i++) {
  //       deleteScheduleArr = deleteScheduleArr.filter(
  //         (schedule) => schedule.tripScheduleId !== deleteSchedule[i].id
  //       );
  //     }

  //     setUser({ ...user, myUpcomingTripSchedules: deleteScheduleArr });
  //   };

  const getPreSchedule = () => {
    setPreSchedule({
      content: [
        {
          tripScheduleId: 4, // 위의 앞으로의 일정 조회와 필드 동일
          waterPlaceId: 1000,
          waterPlaceName: "명곡저수지 상류계곡",
          waterPlaceImg: null,
          waterPlaceAddr: "경상남도 양산시 서창동 명동 산20-1",
          waterPlaceRating: 3.2,
          waterPlaceReviewCnt: 6,
          waterPlaceTraffic: 10,
          tripDate: "2023-08-19",
          rescueSupplies: {
            lifeBoatNum: 5,
            portableStandNum: 0,
            lifeJacketNum: 5,
            lifeRingNum: 5,
            rescueRopeNum: 5,
            rescueRodNum: 0,
          },
          tripPartySize: 10,
          hasReview: false, // 리뷰 작성 여부 (지난 일정의 경우 리뷰를 작성할 수 있음)
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
      number: 0,
      sort: {
        empty: false,
        unsorted: false,
        sorted: true,
      },
      first: true, // 첫번째 페이지인지 여부
      last: true, // 마지막 페이지인지 여부 (이게 false라면 다음에도 요청 가능!!)
      size: 5, // 요청한 여행 일정 개수
      numberOfElements: 5, // 현재 응답에서 조회된 여행 일정 개수 (0개 ~ 5개)
      empty: false,
    });
    axiosInstance
      .get("/api/auth/my-page/pre-schedules")
      .then((res) => {
        console.log(res);
        setPreSchedule(res.data.data);
      })
      .catch((err) => console.log(err));
  };

  const getUpcomingSchedule = () => {
    axiosInstance
      .get("/api/auth/my-page/upcoming-schedules")
      .then((res) => {
        console.log(res);
        setUser({ ...user, myUpcomingTripSchedules: res.data.data });
      })
      .catch((err) => console.log(err));
  };

  const saveImgFile = (e: React.ChangeEvent<HTMLInputElement>) => {
    if (e.target.files) {
      const file = e.target.files[0];
      const reader = new FileReader();
      reader.readAsDataURL(file);

      reader.onload = () => {
        setChangeImg({ imgFile: reader.result, modal: false });
      };

      const formData = new FormData();
      if (file !== undefined) {
        formData.append("image", file);
      }

      axiosInstance
        .post("/api/auth/members/profile-image", formData)
        .then((res) => {
          console.log(res);
        })
        .catch((err) => console.log(err));
    }
  };

  return (
    <div>
      <Header />
      <div className={styles.body}>
        <div className={styles.myPage}>
          <h1>마이페이지</h1>
          <div className={styles.userInfo}>
            <div className={styles.userBasicInfo}>
              <span>기본정보</span>
              <form encType="multipart/form-data">
                <div className={styles.profileImg}>
                  <div className={styles.profileUser}>
                    <img
                      src={
                        changeImg.imgFile === null || changeImg.imgFile === ""
                          ? process.env.PUBLIC_URL + "/img/user-profile.png"
                          : changeImg.imgFile.toString()
                      }
                      alt="사용자 프로필 이미지"
                    />
                  </div>
                  <input
                    name="accountProfileImage"
                    className={styles.profileInput}
                    type="file"
                    accept="image/*"
                    id="profileImg"
                    onChange={saveImgFile}
                    ref={imgRef}
                  />
                  <span
                    onClick={() =>
                      setChangeImg({ ...changeImg, modal: !changeImg.modal })
                    }
                  >
                    프로필 이미지 변경
                  </span>
                </div>
                {changeImg.modal && (
                  <Profile
                    changeImg={changeImg}
                    setChangeImg={setChangeImg}
                    imgRef={imgRef}
                  />
                )}
              </form>
              <div className={styles.userNickname}>
                <span>닉네임</span>
                {nickUpdate.click ? (
                  <input
                    placeholder="닉네임"
                    value={nickUpdate.inputNick}
                    onChange={(e) =>
                      setNickUpdate({
                        ...nickUpdate,
                        inputNick: e.target.value,
                      })
                    }
                    onBlur={checkNickname}
                    maxLength={20}
                  />
                ) : (
                  <span>{user.userProfile.memberNick}</span>
                )}
                <span
                  onClick={() => {
                    if (nickUpdate.click && !nickUpdate.duplicateCheck) {
                      checkDuplication();
                    } else if (nickUpdate.duplicateCheck) {
                      handleResetNicname();
                      setUser({
                        ...user,
                        userProfile: {
                          ...user.userProfile,
                          memberNick: nickUpdate.inputNick,
                        },
                      });
                    } else {
                      setNickUpdate({ ...nickUpdate, click: true });
                    }
                  }}
                >
                  {nickUpdate.click
                    ? nickUpdate.duplicateCheck
                      ? "저장"
                      : "중복검사"
                    : "수정"}
                </span>
                {nickUpdate.click && (
                  <span
                    onClick={() => {
                      if (nickUpdate.click) {
                        setNickUpdate({
                          ...nickUpdate,
                          click: false,
                        });
                      }
                    }}
                  >
                    취소
                  </span>
                )}
                {confirmView.view && (
                  <ConfirmModal
                    content={confirmView.content}
                    handleModal={setConfirmView}
                  />
                )}
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
                    getUpcomingSchedule();
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
                    getPreSchedule();
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
            {scheduleBtn === "앞으로의 일정" ? (
              <TripSchedule
                scheduleBtn={scheduleBtn}
                tripSchedules={user.myUpcomingTripSchedules}
              />
            ) : (
              <TripSchedule
                scheduleBtn={scheduleBtn}
                tripSchedules={preSchedule.content}
              />
            )}
          </div>
        </div>
      </div>
      <Footer />
    </div>
  );
};

interface ProfileProp {
  changeImg: {
    modal: boolean;
    imgFile: string | null | ArrayBuffer;
  };
  setChangeImg: React.Dispatch<
    React.SetStateAction<{
      modal: boolean;
      imgFile: string | null | ArrayBuffer;
    }>
  >;
  imgRef: React.RefObject<HTMLInputElement>;
}

const Profile: FC<ProfileProp> = ({ changeImg, setChangeImg, imgRef }) => {
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

  const deleteProfileImg = (e: React.MouseEvent) => {
    e.preventDefault();
    if (imgRef.current) {
      imgRef.current.value = "";
      setChangeImg({ imgFile: "", modal: false });
    }

    // const formData = new FormData();
    //   formData.append("image", file);

    //   axiosInstance
    //     .post("/api/auth/members/profile-image", formData)
    //     .then((res) => {
    //       console.log(res);
    //     })
    //     .catch((err) => console.log(err));
    // }
  };

  return (
    <div className={styles.modalContainer}>
      <div className={styles.profileUpdate}>
        <div>
          <h1>프로필 사진 바꾸기</h1>
        </div>
        <div>
          <label htmlFor="profileImg">
            <h2>사진 업로드</h2>
          </label>
        </div>
        <div>
          <h2 onClick={(e) => deleteProfileImg(e)}>기본 이미지로 변경</h2>
        </div>
        <div>
          <p onClick={() => setChangeImg({ ...changeImg, modal: false })}>
            취소
          </p>
        </div>
      </div>
    </div>
  );
};

export default MyPage;
