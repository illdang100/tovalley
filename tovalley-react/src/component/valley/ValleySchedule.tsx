import React, { FC, useState } from "react";
import ControlDate from "./calendar/ControlDate";
import DateBox from "./calendar/DateBox";
import styled from "styled-components";

interface Props {
  tripPlanToWaterPlace: {
    [key: string]: number;
  };
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

const ValleySchedule: FC<Props> = ({ tripPlanToWaterPlace }) => {
  const [nowDate, setNowDate] = useState<Date>(new Date());
  const [clickedDate, setClickedDate] = useState<Date>();
  const [tripDate, setTripDate] = useState<{
    startClick: boolean;
    start: Date;
    endClick: boolean;
    end: Date;
  }>({
    startClick: false,
    start: new Date(1000 - 1 - 1),
    endClick: false,
    end: new Date(1000 - 1 - 1),
  });
  const [addScheduleBtn, setAddScheduleBtn] = useState(false);

  return (
    <Container>
      <ControlDate
        nowDate={nowDate}
        setNowDate={setNowDate}
        addScheduleBtn={addScheduleBtn}
        setAddScheduleBtn={setAddScheduleBtn}
        tripDate={tripDate}
      />
      <DateBox
        nowDate={nowDate}
        setNowDate={setNowDate}
        clickedDate={clickedDate}
        setClickedDate={setClickedDate}
        tripPlanToWaterPlace={tripPlanToWaterPlace}
        addScheduleBtn={addScheduleBtn}
        tripDate={tripDate}
        setTripDate={setTripDate}
      />
    </Container>
  );
};

export default ValleySchedule;
