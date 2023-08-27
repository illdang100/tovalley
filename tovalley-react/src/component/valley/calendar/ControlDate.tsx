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
}

const ControlDate = ({ nowDate, setNowDate }: Props) => {
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
          <span>일정 추가</span>
        </div>
      </div>
      <div className={styles.congestionInfo}></div>
    </div>
  );
};

export default ControlDate;
