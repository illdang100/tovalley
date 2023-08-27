import React from "react";
import styled, { css } from "styled-components";

interface ContainerProps {
  sameMonth: boolean;
  sameDay: boolean;
  clickDay: boolean;
  peopleCnt: number;
}

const Container = styled.div<ContainerProps>`
  border: 1px solid #8e8e8e;
  border-top: none;
  border-left: none;
  display: flex;
  justify-content: right;
  align-items: top;
  padding: 0 1.2em;
  position: relative;

  &:nth-child(7n) {
    border-right: none;
  }

  &:nth-child(-n + 14) {
    border-top: 1px solid #8e8e8e;
  }

  p {
    margin-top: 0.7em;
    height: fit-content;
    text-align: center;
    font-weight: ${({ sameMonth }) => (sameMonth ? `700` : `500`)};
    color: ${({ sameMonth }) => (sameMonth ? `black` : `#BCBCBC`)};

    ${({ sameDay }) =>
      sameDay
        ? css`
            padding: 0.3em;
            color: white;
            background-color: red;
            border-radius: 100%;
          `
        : css``}

    ${({ clickDay }) =>
      clickDay
        ? css`
            border: 1px solid skyblue;
          `
        : css``}
  }

  &:nth-child(7n + 8) {
    p {
      color: ${({ sameMonth }) => (sameMonth ? `#F52E2E` : ``)};
      color: ${({ sameDay }) =>
        sameDay
          ? css`
              padding: 0.3em;
              color: white;
              background-color: red;
              border-radius: 100%;
            `
          : css``};
    }
  }

  &:nth-child(7n) {
    p {
      color: ${({ sameMonth }) => (sameMonth ? `#567BFD` : ``)};
      color: ${({ sameDay }) =>
        sameDay
          ? css`
              padding: 0.3em;
              color: white;
              background-color: red;
              border-radius: 100%;
            `
          : css``};
    }
  }

  div {
    width: 85%;
    height: 8px;
    position: absolute;
    bottom: 10px;
    left: 10px;
    border-radius: 10px;
    background-color: ${({ peopleCnt }) =>
      peopleCnt >= 15
        ? `#FA7F64`
        : peopleCnt >= 10
        ? `#FFD874`
        : peopleCnt >= 5
        ? `#8EBBFF`
        : `#E0E0E0`};
  }
`;

interface Props {
  day: Date;
  nowDate: Date;
  setNowDate: React.Dispatch<React.SetStateAction<Date>>;
  clickedDate: Date | undefined;
  setClickedDate: React.Dispatch<React.SetStateAction<Date | undefined>>;
  tripPlanToWaterPlace: {
    [key: string]: number;
  };
}

let formatedDate;

const dateFormat = (
  nowDate: Date,
  day: Date,
  tripPlanToWaterPlace: {
    [key: string]: number;
  }
) => {
  if (nowDate.getMonth() < 10 && day.getDate() < 10) {
    formatedDate =
      tripPlanToWaterPlace[
        `${nowDate.getFullYear()}-0${nowDate.getMonth() + 1}-0${day.getDate()}`
      ];
  } else if (nowDate.getMonth() < 10 && day.getDate() >= 10) {
    formatedDate =
      tripPlanToWaterPlace[
        `${nowDate.getFullYear()}-0${nowDate.getMonth() + 1}-${day.getDate()}`
      ];
  } else if (nowDate.getMonth() >= 10 && day.getDate() < 10) {
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
}: Props) => {
  const nowTime = new Date();

  const sameMonth = nowDate.getMonth() === day.getMonth();
  const sameDay =
    nowTime.getFullYear() === day.getFullYear() &&
    nowTime.getMonth() === day.getMonth() &&
    nowTime.getDate() === day.getDate();

  const clickDay: boolean = clickedDate
    ? clickedDate.getFullYear() === day.getFullYear() &&
      clickedDate.getMonth() === day.getMonth() &&
      clickedDate.getDate() === day.getDate()
    : false;

  const clickDate = () => {
    setClickedDate(day);
  };

  return (
    <Container
      onClick={() => clickDate()}
      sameMonth={sameMonth}
      sameDay={sameDay}
      clickDay={clickDay}
      peopleCnt={dateFormat(nowDate, day, tripPlanToWaterPlace)}
    >
      <p>{day.getDate()}</p>
      {(sameMonth && dateFormat(nowDate, day, tripPlanToWaterPlace) === 0) ||
      (sameMonth &&
        dateFormat(nowDate, day, tripPlanToWaterPlace) === undefined) ? (
        <></>
      ) : sameMonth ? (
        <div />
      ) : (
        <></>
      )}
    </Container>
  );
};

export default allDay;
