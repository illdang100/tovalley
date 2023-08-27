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
  height: 540px;
  display: flex;
  flex-direction: column;
  margin-top: 6em;
  border: 1px solid #8e8e8e;
  border-radius: 10px;
  padding: 2em 0;
`;

const ValleySchedule: FC<Props> = ({ tripPlanToWaterPlace }) => {
  const [nowDate, setNowDate] = useState<Date>(new Date());
  const [clickedDate, setClickedDate] = useState<Date>();

  return (
    <Container>
      <ControlDate nowDate={nowDate} setNowDate={setNowDate} />
      <DateBox
        nowDate={nowDate}
        setNowDate={setNowDate}
        clickedDate={clickedDate}
        setClickedDate={setClickedDate}
        tripPlanToWaterPlace={tripPlanToWaterPlace}
      />
    </Container>
  );
};

export default ValleySchedule;
