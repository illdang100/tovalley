import React, { FC, useState } from "react";
import ControlDate from "./calendar/ControlDate";
import DateBox from "./calendar/DateBox";
import styled from "styled-components";

type tripPeopleCnt = {
  tripPlanToWaterPlace: {
    [key: string]: number;
  };
};

interface Props {
  tripPlanToWaterPlace: tripPeopleCnt;
  waterPlaceName: string;
  detailAddress: string;
  setPeopleCnt: React.Dispatch<React.SetStateAction<tripPeopleCnt>>;
  annualVisitors: string;
}

const Container = styled.div`
  width: 100%;
  display: flex;
  flex-direction: column;
  border: 1px solid #8e8e8e;
  border-radius: 10px;
  padding-bottom: 1em;
  margin-top: 1em;
`;

const AnnualVisitors = styled.div`
  padding: 1em 0 0 1.5em;

  span {
    &:first-child {
      font-weight: bold;
      font-size: 1.1rem;
      margin-right: 0.5em;
      color: #434343;
    }

    &:last-child {
      font-weight: bold;
      font-size: 1rem;
      background-color: #8f8f8f;
      color: white;
      padding: 0.2em 0.5em;
      border-radius: 5px;
    }
  }
`;

const ValleySchedule: FC<Props> = ({
  tripPlanToWaterPlace,
  waterPlaceName,
  detailAddress,
  setPeopleCnt,
  annualVisitors,
}) => {
  const [nowDate, setNowDate] = useState<Date>(new Date());
  const [clickedDate, setClickedDate] = useState<Date>();
  const [addScheduleBtn, setAddScheduleBtn] = useState(false);

  return (
    <div style={{ marginTop: "5em" }}>
      <span style={{ fontWeight: "bold", fontSize: "1.6em" }}>계곡 혼잡도</span>
      <Container>
        <ControlDate
          nowDate={nowDate}
          setNowDate={setNowDate}
          addScheduleBtn={addScheduleBtn}
          setAddScheduleBtn={setAddScheduleBtn}
          clickedDate={clickedDate}
          waterPlaceName={waterPlaceName}
          detailAddress={detailAddress}
          setClickedDate={setClickedDate}
          tripPlanToWaterPlace={tripPlanToWaterPlace}
          setPeopleCnt={setPeopleCnt}
        />
        <DateBox
          nowDate={nowDate}
          setNowDate={setNowDate}
          clickedDate={clickedDate}
          setClickedDate={setClickedDate}
          tripPlanToWaterPlace={tripPlanToWaterPlace}
          addScheduleBtn={addScheduleBtn}
        />
        {annualVisitors !== "" && (
          <AnnualVisitors>
            <span>연평균 방문자 수</span>
            <span>{annualVisitors}</span>
          </AnnualVisitors>
        )}
      </Container>
    </div>
  );
};

export default ValleySchedule;
