import React from "react";
import styles from "../../../css/valley/calendar/ControlDate.module.css";
import {
  MdArrowDropDown,
  MdNavigateBefore,
  MdNavigateNext,
} from "react-icons/md";

interface Props {
  nowDate: Date;
  setNowDate: React.Dispatch<React.SetStateAction<Date>>;
  addScheduleBtn: boolean;
  setAddScheduleBtn: React.Dispatch<React.SetStateAction<boolean>>;
  tripDate: {
    startClick: boolean;
    start: Date;
    endClick: boolean;
    end: Date;
  };
}

const ControlDate = ({
  nowDate,
  setNowDate,
  addScheduleBtn,
  setAddScheduleBtn,
  tripDate,
}: Props) => {
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

  const congestionInfo = [
    { peopleCnt: "15명", color: "#FA7F64" },
    { peopleCnt: "10명", color: "#FFD874" },
    { peopleCnt: "5명", color: "#8EBBFF" },
    { peopleCnt: "1명", color: "#E0E0E0" },
  ];

  return (
    <div className={styles.container}>
      <div className={styles.controlCalendar}>
        <div className={styles.controlYear}>
          <h1>{`${nowDate.getFullYear()}`}</h1>
          <span>
            <MdArrowDropDown color="#D9D9D9" size="45px" />
          </span>
        </div>
        <div className={styles.controlMonth}>
          <span onClick={() => changeMonth(-1)}>
            <MdNavigateBefore color="#D9D9D9" size="40px" />
          </span>
          <h1>{`${nowDate.getMonth() + 1}`}</h1>
          <h2>월</h2>
          <span onClick={() => changeMonth(+1)}>
            <MdNavigateNext color="#D9D9D9" size="40px" />
          </span>
        </div>
        <div className={styles.addSchedule}>
          {addScheduleBtn ? (
            <span
              onClick={() => {}}
              style={
                tripDate.startClick && tripDate.endClick
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
    </div>
  );
};

export default ControlDate;
