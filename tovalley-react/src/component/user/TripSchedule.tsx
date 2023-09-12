import React, { FC, useState } from "react";
import { TbChartDonut4, TbJumpRope } from "react-icons/tb";
import {
  MdEmojiPeople,
  MdHomeRepairService,
  MdCheckBoxOutlineBlank,
  MdCheckBox,
} from "react-icons/md";
import { FaVest } from "react-icons/fa";
import { LuUtilityPole } from "react-icons/lu";
import styles from "../../css/user/TripSchedule.module.css";
import WriteReview from "./WriteReview";

interface Props {
  scheduleBtn: string;
  tripSchedules: // 앞으로의 일정 리스트 (최대 5개)
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
}

const TripSchedule: FC<Props> = ({ scheduleBtn, tripSchedules }) => {
  const [writeReviewView, setWriteReviewView] = useState(false);

  return (
    <div>
      {tripSchedules.map((item) => {
        return (
          <div className={styles.scheduleItem}>
            {/* <span
            className={styles.scheduleCheck}
            onClick={() => {
              if (deleteSchedule[index].check === false) {
                deleteSchedule[index].check = true;
                setDeleteSchedule(deleteSchedule);
              } else {
                deleteSchedule[index].check = false;
                setDeleteSchedule(deleteSchedule);
              }
              console.log(deleteSchedule);
            }}
          >
            {deleteSchedule[index].check === false ? (
              <MdCheckBoxOutlineBlank color="#66A5FC" size="25px" />
            ) : (
              <MdCheckBox color="#66A5FC" size="25px" />
            )}
          </span> */}
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
              {scheduleBtn === "앞으로의 일정" && (
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
              )}
              {scheduleBtn === "지난 일정" && (
                <div className={styles.writeReviewBtn}>
                  <span onClick={() => setWriteReviewView(true)}>
                    리뷰 쓰기
                  </span>
                  {writeReviewView && (
                    <WriteReview
                      setWriteReviewView={setWriteReviewView}
                      valleyInfo={{
                        id: item.tripScheduleId,
                        title: item.waterPlaceName,
                        addr: item.waterPlaceAddr,
                        tripDate: item.tripDate,
                        people: item.tripPartySize,
                        img: item.waterPlaceImg,
                      }}
                    />
                  )}
                </div>
              )}
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
              {scheduleBtn === "지난 일정" && (
                <div className={styles.preCongestion}>
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
              )}
            </div>
          </div>
        );
      })}
    </div>
  );
};

export default TripSchedule;
