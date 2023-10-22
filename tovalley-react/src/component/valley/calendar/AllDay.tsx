import React from "react";
import styled, { css } from "styled-components";

interface ContainerProps {
  sameMonth: boolean;
  sameDay: boolean;
  clickDay: boolean;
  peopleCnt: number;
  addScheduleBtn: boolean;
  afterToday: boolean;
}

const Container = styled.div<ContainerProps>`
  height: 5.5em;
  border: 1px solid #8e8e8e;
  border-top: none;
  border-left: none;
  display: flex;
  justify-content: right;
  align-items: top;
  padding: 0 1.2em;
  position: relative;
  cursor: ${({ sameMonth, addScheduleBtn, afterToday }) =>
    sameMonth && addScheduleBtn && afterToday ? `pointer` : ``};

  &:nth-child(7n) {
    border-right: none;
  }

  &:nth-child(-n + 14) {
    border-top: 1px solid #8e8e8e;
  }

  p {
    position: relative;
    z-index: 2;
    margin-top: 0.7em;
    height: fit-content;
    text-align: center;
    font-weight: ${({ sameMonth }) => (sameMonth ? `700` : `500`)};
    color: ${({ sameMonth, addScheduleBtn, afterToday }) =>
      sameMonth && addScheduleBtn && afterToday
        ? ``
        : sameMonth && !addScheduleBtn
        ? `black`
        : !sameMonth && !addScheduleBtn
        ? `#BCBCBC`
        : `rgb(212, 212, 212);`};

    ${({ sameDay }) =>
      sameDay
        ? css`
            padding: 0.3em;
            color: white;
            background-color: red;
            border-radius: 100%;
            width: 1.3em;
            height: 1.3em;
          `
        : css``}

    ${({ clickDay }) =>
      clickDay
        ? css`
            background-color: #3378fc;
            color: white;
            border-radius: 100%;
            padding: 0.3em;
            width: 1.3em;
          `
        : css``}
  }

  &:nth-child(7n + 8) {
    p {
      position: relative;
      z-index: 2;
      color: ${({ sameMonth, addScheduleBtn, afterToday }) =>
        sameMonth && addScheduleBtn && afterToday
          ? `#F52E2E`
          : sameMonth && !addScheduleBtn
          ? `#F52E2E`
          : `rgb(212, 212, 212);`};
      color: ${({ sameDay }) =>
        sameDay
          ? css`
              padding: 0.3em;
              color: white;
              background-color: red;
              border-radius: 100%;
              width: 1.3em;
              height: 1.3em;
            `
          : css``};
      color: ${({ clickDay }) =>
        clickDay
          ? css`
              background-color: #3378fc;
              color: white;
              border-radius: 100%;
              padding: 0.3em;
              width: 1.3em;
            `
          : css``};
    }
  }

  &:nth-child(7n) {
    p {
      position: relative;
      z-index: 2;
      color: ${({ sameMonth, addScheduleBtn, afterToday }) =>
        sameMonth && addScheduleBtn && afterToday
          ? `#567BFD`
          : sameMonth && !addScheduleBtn
          ? `#567BFD`
          : `rgb(212, 212, 212);`};
      color: ${({ sameDay }) =>
        sameDay
          ? css`
              padding: 0.3em;
              color: white;
              background-color: red;
              border-radius: 100%;
              width: 1.3em;
              height: 1.3em;
            `
          : css``};
      color: ${({ clickDay }) =>
        clickDay
          ? css`
              background-color: #3378fc;
              color: white;
              border-radius: 100%;
              padding: 0.3em;
              width: 1.3em;
            `
          : css``};
    }
  }

  .peopleCnt {
    width: 85%;
    height: 8px;
    position: absolute;
    bottom: 10px;
    left: 10px;
    border-radius: 10px;
    background-color: ${({ peopleCnt }) =>
      peopleCnt >= 46
        ? `#FA7F64`
        : peopleCnt >= 31
        ? `#FFD874`
        : peopleCnt >= 16
        ? `#8EBBFF`
        : `#E0E0E0`};
  }
`;

type tripPeopleCnt = {
  tripPlanToWaterPlace: {
    [key: string]: number;
  };
};

interface Props {
  day: Date;
  nowDate: Date;
  setNowDate: React.Dispatch<React.SetStateAction<Date>>;
  clickedDate: Date | undefined;
  setClickedDate: React.Dispatch<React.SetStateAction<Date | undefined>>;
  tripPlanToWaterPlace: tripPeopleCnt;
  addScheduleBtn: boolean;
}

let formatedDate;

const dateFormat = (
  nowDate: Date,
  day: Date,
  tripPlanToWaterPlace: {
    [key: string]: number;
  }
) => {
  if (nowDate.getMonth() + 1 < 10 && day.getDate() < 10) {
    formatedDate =
      tripPlanToWaterPlace[
        `${nowDate.getFullYear()}-0${nowDate.getMonth() + 1}-0${day.getDate()}`
      ];
  } else if (nowDate.getMonth() + 1 < 10 && day.getDate() >= 10) {
    formatedDate =
      tripPlanToWaterPlace[
        `${nowDate.getFullYear()}-0${nowDate.getMonth() + 1}-${day.getDate()}`
      ];
  } else if (nowDate.getMonth() + 1 >= 10 && day.getDate() < 10) {
    formatedDate =
      tripPlanToWaterPlace[
        `${nowDate.getFullYear()}-${nowDate.getMonth() + 1}-0${day.getDate()}`
      ];
  } else {
    formatedDate =
      tripPlanToWaterPlace[
        `${nowDate.getFullYear()}-${nowDate.getMonth() + 1}-${day.getDate()}`
      ];
  }

  return formatedDate;
};

const allDay = ({
  day,
  nowDate,
  setNowDate,
  clickedDate,
  setClickedDate,
  tripPlanToWaterPlace,
  addScheduleBtn,
}: Props) => {
  const nowTime = new Date();

  const sameMonth = nowDate.getMonth() === day.getMonth();
  const sameDay =
    nowTime.getFullYear() === day.getFullYear() &&
    nowTime.getMonth() === day.getMonth() &&
    nowTime.getDate() === day.getDate();

  const afterToday =
    nowTime.getFullYear() < day.getFullYear() ||
    nowTime.getMonth() < day.getMonth() ||
    (nowTime.getMonth() <= day.getMonth() &&
      nowTime.getFullYear() <= day.getFullYear() &&
      nowTime.getDate() <= day.getDate());

  const clickDay: boolean = clickedDate
    ? clickedDate.getFullYear() === day.getFullYear() &&
      clickedDate.getMonth() === day.getMonth() &&
      clickedDate.getDate() === day.getDate()
    : false;

  const clickDate = () => {
    if (addScheduleBtn) {
      setClickedDate(day);
    }
  };

  return (
    <Container
      onClick={() => afterToday && clickDate()}
      sameMonth={sameMonth}
      sameDay={sameDay}
      clickDay={clickDay}
      peopleCnt={dateFormat(
        nowDate,
        day,
        tripPlanToWaterPlace.tripPlanToWaterPlace
      )}
      addScheduleBtn={addScheduleBtn}
      afterToday={afterToday}
    >
      <p>{day.getDate()}</p>
      {(sameMonth &&
        dateFormat(nowDate, day, tripPlanToWaterPlace.tripPlanToWaterPlace) ===
          0) ||
      (sameMonth &&
        dateFormat(nowDate, day, tripPlanToWaterPlace.tripPlanToWaterPlace) ===
          undefined) ? (
        <></>
      ) : sameMonth ? (
        <div className="peopleCnt" />
      ) : (
        <></>
      )}
    </Container>
  );
};

export default allDay;
