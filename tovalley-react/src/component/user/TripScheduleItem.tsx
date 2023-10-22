import React, { FC, useEffect, useState } from "react";
import styles from "../../css/user/TripScheduleItem.module.css";
import { TbChartDonut4, TbJumpRope } from "react-icons/tb";
import {
  MdEmojiPeople,
  MdHomeRepairService,
  MdCheckBoxOutlineBlank,
  MdCheckBox,
} from "react-icons/md";
import { FaVest } from "react-icons/fa";
import { LuUtilityPole } from "react-icons/lu";
import WriteReview from "./WriteReview";
import { useNavigate } from "react-router-dom";

type schedule = {
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
};

interface Props {
  schedule: schedule;
  scheduleBtn: string;
  checkItemHandler: (id: schedule, isChecked: boolean) => void;
  checkedItems: schedule[];
}

const TripScheduleItem: FC<Props> = ({
  schedule,
  scheduleBtn,
  checkItemHandler,
  checkedItems,
}) => {
  const [writeReviewView, setWriteReviewView] = useState(false);
  const [check, setCheck] = useState(false);
  const navigation = useNavigate();

  return (
    <div className={styles.scheduleItem}>
      <span
        className={styles.scheduleCheck}
        onClick={() => {
          checkItemHandler(schedule, !check);
          setCheck(!check);
        }}
      >
        {checkedItems.includes(schedule) ? (
          <MdCheckBox color="#66A5FC" size="25px" />
        ) : (
          <MdCheckBoxOutlineBlank color="#66A5FC" size="25px" />
        )}
      </span>
      <img
        src={
          schedule.waterPlaceImg === null
            ? process.env.PUBLIC_URL + "/img/default-image.png"
            : schedule.waterPlaceImg
        }
        alt="계곡 사진"
        width="180px"
      />
      <div
        className={styles.scheduleInfo}
        onClick={() => navigation(`/valley/${schedule.waterPlaceId}`)}
      >
        <h4>{schedule.waterPlaceName}</h4>
        <span>{schedule.waterPlaceAddr}</span>
        <span>
          {schedule.waterPlaceRating === "" ? 0 : schedule.waterPlaceRating}
        </span>
        <span>/5</span>
        <span>
          리뷰{" "}
          {schedule.waterPlaceReviewCnt === ""
            ? 0
            : schedule.waterPlaceReviewCnt}
          개
        </span>
        <div className={styles.reservationInfo}>
          <div>
            <span>날짜</span>
            <span>{schedule.tripDate}</span>
          </div>
          <div>
            <span>인원</span>
            <span>{schedule.tripPartySize}</span>
          </div>
        </div>
      </div>
      <div className={styles.valleyInfo}>
        {scheduleBtn === "앞으로의 일정" && (
          <div className={styles.congestion}>
            <span>계곡 혼잡도</span>
            <div
              style={
                schedule.waterPlaceTraffic >= 15
                  ? { backgroundColor: "#FA7F64" }
                  : schedule.waterPlaceTraffic >= 10
                  ? { backgroundColor: "#FFD874" }
                  : schedule.waterPlaceTraffic >= 5
                  ? { backgroundColor: "#8EBBFF" }
                  : { backgroundColor: "#E0E0E0" }
              }
            ></div>
          </div>
        )}
        {scheduleBtn === "지난 일정" && (
          <div className={styles.writeReviewBtn}>
            <span onClick={() => setWriteReviewView(true)}>리뷰 쓰기</span>
            {writeReviewView && (
              <WriteReview
                setWriteReviewView={setWriteReviewView}
                valleyInfo={{
                  id: schedule.tripScheduleId,
                  title: schedule.waterPlaceName,
                  addr: schedule.waterPlaceAddr,
                  tripDate: schedule.tripDate,
                  people: schedule.tripPartySize,
                  img: schedule.waterPlaceImg,
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
            <span>
              {schedule.rescueSupplies.lifeRingNum === -1
                ? "-"
                : schedule.rescueSupplies.lifeRingNum}
            </span>
          </div>
          <div className={styles.rescueItem}>
            <span>
              <TbJumpRope size="40px" color="#66A5FC" />
            </span>
            <span>
              {schedule.rescueSupplies.rescueRopeNum === -1
                ? "-"
                : schedule.rescueSupplies.rescueRopeNum}
            </span>
          </div>
          <div className={styles.rescueItem}>
            <span>
              <MdEmojiPeople size="40px" color="#66A5FC" />
            </span>
            <span>
              {schedule.rescueSupplies.lifeBoatNum === -1
                ? "-"
                : schedule.rescueSupplies.lifeBoatNum}
            </span>
          </div>
          <div className={styles.rescueItem}>
            <span>
              <FaVest size="40px" color="#66A5FC" />
            </span>
            <span>
              {schedule.rescueSupplies.lifeJacketNum === -1
                ? "-"
                : schedule.rescueSupplies.lifeJacketNum}
            </span>
          </div>
          <div className={styles.rescueItem}>
            <span>
              <MdHomeRepairService size="40px" color="#66A5FC" />
            </span>
            <span>
              {schedule.rescueSupplies.portableStandNum === -1
                ? "-"
                : schedule.rescueSupplies.portableStandNum}
            </span>
          </div>
          <div className={styles.rescueItem}>
            <span>
              <LuUtilityPole size="40px" color="#66A5FC" />
            </span>
            <span>
              {schedule.rescueSupplies.rescueRodNum === -1
                ? "-"
                : schedule.rescueSupplies.rescueRodNum}
            </span>
          </div>
        </div>
        {scheduleBtn === "지난 일정" && (
          <div className={styles.preCongestion}>
            <span>계곡 혼잡도</span>
            <div
              style={
                schedule.waterPlaceTraffic >= 15
                  ? { backgroundColor: "#FA7F64" }
                  : schedule.waterPlaceTraffic >= 10
                  ? { backgroundColor: "#FFD874" }
                  : schedule.waterPlaceTraffic >= 5
                  ? { backgroundColor: "#8EBBFF" }
                  : { backgroundColor: "#E0E0E0" }
              }
            ></div>
          </div>
        )}
      </div>
    </div>
  );
};

export default TripScheduleItem;
