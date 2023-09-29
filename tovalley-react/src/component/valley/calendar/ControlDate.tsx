import React, { useEffect, useState } from "react";
import styles from "../../../css/valley/calendar/ControlDate.module.css";
import {
  MdArrowDropDown,
  MdArrowDropUp,
  MdNavigateBefore,
  MdNavigateNext,
} from "react-icons/md";
import axiosInstance from "../../../axios_interceptor";
import { useParams } from "react-router-dom";
import { AiFillPlusCircle, AiFillMinusCircle } from "react-icons/ai";

type tripPeopleCnt = {
  tripPlanToWaterPlace: {
    [key: string]: number;
  };
};

interface Props {
  nowDate: Date;
  setNowDate: React.Dispatch<React.SetStateAction<Date>>;
  addScheduleBtn: boolean;
  setAddScheduleBtn: React.Dispatch<React.SetStateAction<boolean>>;
  clickedDate: Date | undefined;
  waterPlaceName: string;
  detailAddress: string;
  setClickedDate: React.Dispatch<React.SetStateAction<Date | undefined>>;
  tripPlanToWaterPlace: tripPeopleCnt;
  setPeopleCnt: React.Dispatch<React.SetStateAction<tripPeopleCnt>>;
}

const ControlDate = ({
  nowDate,
  setNowDate,
  addScheduleBtn,
  setAddScheduleBtn,
  clickedDate,
  waterPlaceName,
  detailAddress,
  setClickedDate,
  tripPlanToWaterPlace,
  setPeopleCnt,
}: Props) => {
  const { id } = useParams();

  const changeYear = (change: number) => {
    const date = new Date(nowDate.getTime());
    date.setFullYear(date.getFullYear() + change);
    setNowDate(date);
  };

  const changeMonth = (change: number) => {
    const date = new Date(nowDate.getTime());
    date.setMonth(date.getMonth() + change);
    setNowDate(date);
  };

  const getMonthTripSchedule = (change: number) => {
    const date = new Date(nowDate.getTime());
    date.setMonth(date.getMonth() + change);

    const nowMonth =
      date.getMonth() + 1 < 10
        ? `0${date.getMonth() + 1}`
        : `${date.getMonth() + 1}`;

    const yearMonth = `${nowDate.getFullYear()}-${nowMonth}`;

    const config = {
      params: {
        yearMonth: yearMonth,
      },
    };

    axiosInstance
      .get(`/api/auth/water-places/${id}/trip-schedules`, config)
      .then((res) => {
        setPeopleCnt({ tripPlanToWaterPlace: res.data.data });
      });
  };

  const [scheduleInfo, setScheduleInfo] = useState(false);
  const [yearDropdown, setYearDropdown] = useState(false);

  const congestionInfo = [
    { peopleCnt: "60명", color: "#FA7F64" },
    { peopleCnt: "45명", color: "#FFD874" },
    { peopleCnt: "30명", color: "#8EBBFF" },
    { peopleCnt: "15명", color: "#E0E0E0" },
  ];

  return (
    <div className={styles.container}>
      <div className={styles.controlCalendar}>
        <div className={styles.controlYear}>
          <h1>{`${nowDate.getFullYear()}`}</h1>
          {!yearDropdown ? (
            <span
              onClick={() => {
                setYearDropdown(true);
                changeYear(-1);
              }}
            >
              <MdArrowDropDown color="#D9D9D9" size="45px" />
            </span>
          ) : (
            <span
              onClick={() => {
                setYearDropdown(false);
                changeYear(+1);
              }}
            >
              <MdArrowDropUp color="#D9D9D9" size="45px" />
            </span>
          )}
        </div>
        <div className={styles.controlMonth}>
          <span
            onClick={() => {
              if (
                nowDate.getFullYear() < new Date().getFullYear() &&
                nowDate.getMonth() + 1 === 1
              ) {
              } else {
                changeMonth(-1);
                getMonthTripSchedule(-1);
              }
            }}
          >
            <MdNavigateBefore color="#D9D9D9" size="40px" />
          </span>
          <h1>{`${nowDate.getMonth() + 1}`}</h1>
          <h2>월</h2>
          <span
            onClick={() => {
              if (
                nowDate.getFullYear() === new Date().getFullYear() &&
                nowDate.getMonth() + 1 === 12
              ) {
              } else {
                changeMonth(+1);
                getMonthTripSchedule(+1);
              }
            }}
          >
            <MdNavigateNext color="#D9D9D9" size="40px" />
          </span>
        </div>
        <div className={styles.addSchedule}>
          {addScheduleBtn ? (
            <span
              onClick={() => clickedDate !== undefined && setScheduleInfo(true)}
              style={
                clickedDate !== undefined
                  ? {}
                  : { backgroundColor: "#D3D3D3", cursor: "auto" }
              }
            >
              등록하기
            </span>
          ) : (
            <span onClick={() => setAddScheduleBtn(true)}>일정 추가</span>
          )}
        </div>
      </div>
      <div className={styles.congestionInfo}>
        {congestionInfo.map((item) => {
          return (
            <div>
              <span>{item.peopleCnt}↑</span>
              <div style={{ backgroundColor: item.color }} />
            </div>
          );
        })}
      </div>
      {scheduleInfo && (
        <ScheduleInfo
          nowDate={nowDate}
          tripDate={clickedDate}
          waterPlaceName={waterPlaceName}
          detailAddress={detailAddress}
          setScheduleInfo={setScheduleInfo}
          setAddScheduleBtn={setAddScheduleBtn}
          setClickedDate={setClickedDate}
          tripPlanToWaterPlace={tripPlanToWaterPlace}
          setPeopleCnt={setPeopleCnt}
        />
      )}
    </div>
  );
};

interface ScheduleInfoProps {
  nowDate: Date;
  tripDate: Date | undefined;
  waterPlaceName: string;
  detailAddress: string;
  setScheduleInfo: React.Dispatch<React.SetStateAction<boolean>>;
  setAddScheduleBtn: React.Dispatch<React.SetStateAction<boolean>>;
  setClickedDate: React.Dispatch<React.SetStateAction<Date | undefined>>;
  tripPlanToWaterPlace: tripPeopleCnt;
  setPeopleCnt: React.Dispatch<React.SetStateAction<tripPeopleCnt>>;
}

const ScheduleInfo = ({
  nowDate,
  tripDate,
  waterPlaceName,
  detailAddress,
  setScheduleInfo,
  setAddScheduleBtn,
  setClickedDate,
  setPeopleCnt,
}: ScheduleInfoProps) => {
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

  const { id } = useParams();
  const [peopleCount, setPeopleCount] = useState(0);

  const dateFormat = (day: Date) => {
    if (nowDate.getMonth() + 1 < 10 && day.getDate() < 10) {
      return `${day.getFullYear()}-0${day.getMonth() + 1}-0${day.getDate()}`;
    } else if (nowDate.getMonth() + 1 < 10 && day.getDate() >= 10) {
      return `${day.getFullYear()}-0${day.getMonth() + 1}-${day.getDate()}`;
    } else if (nowDate.getMonth() + 1 >= 10 && day.getDate() < 10) {
      return `${day.getFullYear()}-${day.getMonth() + 1}-0${day.getDate()}`;
    } else return `${day.getFullYear()}-${day.getMonth() + 1}-${day.getDate()}`;
  };

  const addSchedule = (tripDate: Date) => {
    const data = {
      waterPlaceId: Number(id),
      tripDate: dateFormat(tripDate),
      tripPartySize: peopleCount,
    };

    console.log(data);
    axiosInstance
      .post("/api/auth/trip-schedules", data)
      .then((res) => {
        console.log(res);
        setPeopleCnt({ tripPlanToWaterPlace: res.data.data });
        setScheduleInfo(false);
        setAddScheduleBtn(false);
        setClickedDate(undefined);
      })
      .catch((err) => console.log(err));
  };

  return (
    <div className={styles.infoContainer}>
      <div className={styles.infoBox}>
        <div>
          <h1>{dateFormat(tripDate!)}</h1>
          <div className={styles.valleyInfo}>
            <h4>{waterPlaceName}</h4>
            <span>{detailAddress}</span>
          </div>
          <div className={styles.peopleCnt}>
            <span>인원</span>
            <span
              onClick={() => {
                peopleCount !== 0 && setPeopleCount((prev) => prev - 1);
              }}
            >
              <AiFillMinusCircle color="#c3c3c3" size="25px" />
            </span>
            <input value={peopleCount} readOnly />
            <span
              onClick={() => {
                setPeopleCount((prev) => prev + 1);
              }}
            >
              <AiFillPlusCircle color="#c3c3c3" size="25px" />
            </span>
          </div>
        </div>
        <div className={styles.addBtn}>
          <span
            onClick={() => {
              setScheduleInfo(false);
              setAddScheduleBtn(false);
              setClickedDate(undefined);
            }}
          >
            취소하기
          </span>
          <span
            onClick={() => peopleCount !== 0 && addSchedule(tripDate!)}
            style={
              peopleCount === 0
                ? { backgroundColor: "#cdcdcd", cursor: "auto" }
                : {}
            }
          >
            등록하기
          </span>
        </div>
      </div>
    </div>
  );
};

export default ControlDate;
