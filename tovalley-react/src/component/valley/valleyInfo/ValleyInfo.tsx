import React, { FC, useState } from "react";
import styles from "../../../css/valley/ValleyInfo.module.css";
import { TbChartDonut4, TbJumpRope } from "react-icons/tb";
import { MdEmojiPeople, MdHomeRepairService } from "react-icons/md";
import { FaVest } from "react-icons/fa";
import { LuUtilityPole } from "react-icons/lu";
import ValleyMap from "./ValleyMap";

interface Props {
  waterPlaceDetails: {
    waterPlaceImage: string | null;
    waterPlaceName: string;
    latitude: string;
    longitude: string;
    managementType: string;
    detailAddress: string;
    town: string;
    annualVisitors: number;
    safetyMeasures: number | string;
    waterPlaceSegment: number;
    dangerSegments: number | string;
    dangerSignboardsNum: number | string;
    deepestDepth: number;
    avgDepth: number;
    waterTemperature: number;
    bod: number;
    turbidity: number;
    waterQualityReviews: {
      깨끗해요: number;
      괜찮아요: number;
      더러워요: number;
    };
  };
  weatherList: {
    weatherDate: string;
    climateIcon: string;
    climateDescription: string;
    lowestTemperature: number;
    highestTemperature: number;
    humidity: number;
    windSpeed: number;
    rainPrecipitation: number;
    clouds: number;
    dayFeelsLike: number;
  }[];
  accidents: {
    totalDeathCnt: number;
    totalDisappearanceCnt: number;
    totalInjuryCnt: number;
  };
  rescueSupplies: {
    lifeBoatNum: number;
    portableStandNum: number;
    lifeJacketNum: number;
    lifeRingNum: number;
    rescueRopeNum: number;
    rescueRodNum: number;
  };
}

const dateFormat = (date: string) => {
  let createDate = new Date(date);
  const nowDate = new Date();

  let formatedDate =
    nowDate.getMonth() + 1 === createDate.getMonth() + 1 &&
    nowDate.getDate() === createDate.getDate()
      ? "오늘"
      : `${createDate.getMonth() + 1}.${createDate.getDate()}`;

  return formatedDate;
};

const getDayOfWeek = (day: string) => {
  const week = ["일", "월", "화", "수", "목", "금", "토"];
  const dayOfWeek = week[new Date(day).getDay()];

  return dayOfWeek;
};

const ValleyInfo: FC<Props> = ({
  waterPlaceDetails,
  weatherList,
  accidents,
  rescueSupplies,
}) => {
  const weatherDetail = weatherList.map((item) => [
    { name: "습도", value: item.humidity },
    {
      name: "풍속",
      value: item.windSpeed,
    },
    { name: "강수량", value: item.rainPrecipitation },
    {
      name: "흐림정도",
      value: item.clouds,
    },
    { name: "체감온도", value: item.dayFeelsLike },
  ]);

  const [mapMenu, setMapMenu] = useState("계곡위치");
  const menu = ["계곡위치", "병원", "약국"];

  return (
    <div>
      <div
        className={styles.title}
        style={
          waterPlaceDetails.waterPlaceImage === null
            ? {
                backgroundImage: "url('/img/default-waterfall.jpg')",
              }
            : {
                backgroundImage: `url(${waterPlaceDetails.waterPlaceImage})`,
              }
        }
      >
        <div className={styles.valleyName}>
          <span>{waterPlaceDetails.waterPlaceName}</span>
          <span>{waterPlaceDetails.managementType}</span>
        </div>
        <div className={styles.valleyAddress}>
          <span>{waterPlaceDetails.detailAddress}</span>
        </div>
      </div>
      <div className={styles.valleyInfo}>
        <div className={styles.valleyMap}>
          <div className={styles.valleyPlaceMenu}>
            {menu.map((item) => {
              return (
                <span
                  onClick={() => setMapMenu(item)}
                  className={
                    item === mapMenu ? styles.clickedMenu : styles.placeMenu
                  }
                >
                  {item}
                </span>
              );
            })}
          </div>
          {mapMenu !== "계곡위치" && <span>반경 10km 이내로 조회됩니다.</span>}
          <div className={styles.valleyPlace}>
            <ValleyMap
              latitude={Number(waterPlaceDetails.latitude)}
              longitude={Number(waterPlaceDetails.longitude)}
              menu={mapMenu}
            />
          </div>
        </div>
        <div className={styles.valleyDetail}>
          <div className={styles.valleyWeather}>
            <span>{waterPlaceDetails.town} 날씨</span>
            <div className={styles.weatherList}>
              {weatherList.map((item, index) => {
                return (
                  <div className={styles.weatherItem}>
                    <div>
                      <span>
                        <img
                          src={`https://openweathermap.org/img/wn/${item.climateIcon}@2x.png`}
                          alt="날씨 아이콘"
                          width="55px"
                        />
                      </span>
                      <span>{item.climateDescription}</span>
                      <div className={styles.temperature}>
                        <span>{item.lowestTemperature.toFixed()}°</span>
                        <span> / </span>
                        <span>{item.highestTemperature.toFixed()}°</span>
                      </div>
                      {weatherDetail[index].map((detail) => {
                        return (
                          <div className={styles.weatherItemDetail}>
                            <span>{detail.name}</span>
                            <span>{detail.value}</span>
                          </div>
                        );
                      })}
                      <div className={styles.dayInfo}>
                        {dateFormat(item.weatherDate) === "오늘" ? (
                          <>
                            <span style={{ color: "#66A5FC" }}>
                              {getDayOfWeek(item.weatherDate)}
                            </span>
                            <span style={{ color: "#66A5FC" }}>
                              {dateFormat(item.weatherDate)}
                            </span>
                          </>
                        ) : (
                          <>
                            <span>{getDayOfWeek(item.weatherDate)}</span>
                            <span>{dateFormat(item.weatherDate)}</span>
                          </>
                        )}
                      </div>
                    </div>
                  </div>
                );
              })}
            </div>
          </div>
          <div className={styles.valleyAccident}>
            <span>최근 5년간 사고 수</span>
            <div className={styles.graph}>
              <div className={styles.graphTitle}>
                <span>사망</span>
                <span>실종</span>
                <span>부상</span>
              </div>
              <div className={styles.graphContent}>
                <span>{accidents.totalDeathCnt}</span>
                <span>{accidents.totalDisappearanceCnt}</span>
                <span>{accidents.totalInjuryCnt}</span>
              </div>
            </div>
          </div>
          <div className={styles.rescueSupplies}>
            <span>구조용품 현황</span>
            <div className={styles.rescueList}>
              <div className={styles.rescueItem}>
                <span>
                  <TbChartDonut4 size="40px" color="#66A5FC" />
                </span>
                <span>구명환</span>
                <span>{rescueSupplies.lifeRingNum}</span>
              </div>
              <div className={styles.rescueItem}>
                <span>
                  <TbJumpRope size="40px" color="#66A5FC" />
                </span>
                <span>구명로프</span>
                <span>{rescueSupplies.rescueRopeNum}</span>
              </div>
              <div className={styles.rescueItem}>
                <span>
                  <MdEmojiPeople size="40px" color="#66A5FC" />
                </span>
                <span>인명구조함</span>
                <span>{rescueSupplies.lifeBoatNum}</span>
              </div>
              <div className={styles.rescueItem}>
                <span>
                  <FaVest size="40px" color="#66A5FC" />
                </span>
                <span>구명조끼</span>
                <span>{rescueSupplies.lifeJacketNum}</span>
              </div>
              <div className={styles.rescueItem}>
                <span>
                  <MdHomeRepairService size="40px" color="#66A5FC" />
                </span>
                <span>이동식 거치대</span>
                <span>{rescueSupplies.portableStandNum}</span>
              </div>
              <div className={styles.rescueItem}>
                <span>
                  <LuUtilityPole size="40px" color="#66A5FC" />
                </span>
                <span>구명봉</span>
                <span>{rescueSupplies.rescueRodNum}</span>
              </div>
            </div>
          </div>
        </div>
      </div>
    </div>
  );
};

export default ValleyInfo;
