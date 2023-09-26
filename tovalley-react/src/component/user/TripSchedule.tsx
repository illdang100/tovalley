import React, { FC, useEffect, useState } from "react";
import TripScheduleItem from "./TripScheduleItem";
import useDidMountEffect from "../../useDidMountEffect";

type preSchedule = {
  content: {
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
  }[];
  pageable: {
    sort: {
      empty: boolean;
      unsorted: boolean;
      sorted: boolean;
    };
    offset: number;
    pageNumber: number;
    pageSize: number;
    paged: boolean;
    unpaged: boolean;
  };
  number: number;
  sort: {
    empty: boolean;
    unsorted: boolean;
    sorted: boolean;
  };
  first: boolean;
  last: boolean;
  size: number;
  numberOfElements: number;
  empty: boolean;
};

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
}[];

type scheduleItem = {
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
  scheduleBtn: string;
  tripSchedules: // 앞으로의 일정 리스트 (최대 5개)
  {
    tripScheduleId: number; // 여행 일정 Id(PK)
    waterPlaceId: number; // 물놀이 장소 Id(PK)
    waterPlaceName: string; // 물놀이 장소명
    waterPlaceImg: string | null; // 물놀이 장소 이미지
    waterPlaceAddr: string; // 물놀이 장소 주소
    waterPlaceRating: number | string; // 물놀이 장소 평점
    waterPlaceReviewCnt: number | string; // 물놀이 장소 리뷰 개수
    waterPlaceTraffic: number; // 물놀이 장소 혼잡도(해당 날짜에 해당 계곡에 가는 인원수)
    tripDate: string; // 내가 계획한 여행 날자
    tripPartySize: number; // 함께 가는 여행 인원수
    rescueSupplies: {
      lifeBoatNum: number; // 인명구조함
      portableStandNum: number; // 이동식거치대
      lifeJacketNum: number; // 구명조끼
      lifeRingNum: number; // 구명환
      rescueRopeNum: number; // 구명로프
      rescueRodNum: number; // 구조봉
    };
    hasReview: boolean; // 리뷰 작성 여부(앞으로의 일정은 리뷰를 작성할 수 없음)
  }[];
  setUpCommingSchedule: React.Dispatch<React.SetStateAction<schedule>>;
  setPreSchedule: React.Dispatch<React.SetStateAction<preSchedule>>;
  deleteBtn: boolean;
  preSchedule: preSchedule;
  setDeleteBtn: React.Dispatch<React.SetStateAction<boolean>>;
}

const TripSchedule: FC<Props> = ({
  scheduleBtn,
  tripSchedules,
  deleteBtn,
  setDeleteBtn,
  setUpCommingSchedule,
  setPreSchedule,
}) => {
  const [checkedItems, setCheckedItems] = useState(new Set());
  let checkedItemsArr: schedule = [];

  const checkedItemHandler = (id: scheduleItem, isChecked: boolean) => {
    // if (isChecked) {
    //   checkedItems.add(id);
    //   checkedItemsArr = Array.from(checkedItems);
    //   console.log(checkedItemsArr);
    // } else if (!isChecked && checkedItems.has(id)) {
    //   checkedItems.delete(id);
    //   setCheckedItems(checkedItems);
    //   checkedItemsArr = Array.from(checkedItems);
    //   console.log(checkedItemsArr);
    // }
  };

  useDidMountEffect(() => {
    // if (deleteBtn) {
    //   scheduleBtn === "앞으로의 일정"
    //     ? setUpCommingSchedule(checkedItemsArr)
    //     : setPreSchedule(checkedItemsArr);
    //   setDeleteBtn(false);
    // }
  }, [deleteBtn]);

  return (
    <div>
      {tripSchedules.map((item) => {
        return (
          <TripScheduleItem
            schedule={item}
            scheduleBtn={scheduleBtn}
            checkItemHandler={checkedItemHandler}
          />
        );
      })}
    </div>
  );
};

export default TripSchedule;
