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
}

const Container = styled.div`
  width: 100%;
  display: flex;
  flex-direction: column;
  margin-top: 6em;
  border: 1px solid #8e8e8e;
  border-radius: 10px;
  padding-bottom: 2em;
`;

const ValleySchedule: FC<Props> = ({
  tripPlanToWaterPlace,
  waterPlaceName,
  detailAddress,
  setPeopleCnt,
}) => {
  const [nowDate, setNowDate] = useState<Date>(new Date());
  const [clickedDate, setClickedDate] = useState<Date>();
  const [addScheduleBtn, setAddScheduleBtn] = useState(false);

  return (
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
    </Container>
  );
};

export default ValleySchedule;
