import React, { FC, useEffect, useState } from "react";
import styles from "../../../css/valley/ValleyInfo.module.css";
import { TbChartDonut4, TbJumpRope } from "react-icons/tb";
import { MdEmojiPeople, MdHomeRepairService } from "react-icons/md";
import { FaVest } from "react-icons/fa";
import { LuUtilityPole } from "react-icons/lu";
import { BiImage } from "react-icons/bi";
import { IoMdClose } from "react-icons/io";
import { BsFillClipboardCheckFill } from "react-icons/bs";
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
    annualVisitors: string;
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
    { name: "습도", value: `${item.humidity.toFixed()}%` },
    {
      name: "풍속",
      value: `${item.windSpeed.toFixed()}m/s`,
    },
    { name: "강수량", value: `${item.rainPrecipitation.toFixed()}mm` },
    {
      name: "흐림정도",
      value: `${item.clouds.toFixed()}%`,
    },
    { name: "체감온도", value: `${item.dayFeelsLike.toFixed()}°` },
  ]);

  const [mapMenu, setMapMenu] = useState("계곡위치");
  const [clickImg, setClickImg] = useState(false);
  const menu = ["계곡위치", "병원", "약국"];
  const [innerWidth, setInnerWidth] = useState(window.innerWidth);

  useEffect(() => {
    const resizeListener = () => {
      setInnerWidth(window.innerWidth);
    };
    window.addEventListener("resize", resizeListener);
  });

  return (
    <div>
      <div
        className={styles.title}
        style={{
          backgroundImage: "url('/img/dummy/계곡이미지3.jpeg')",
        }}
      >
        <div className={styles.valleyName}>
          <span>{waterPlaceDetails.waterPlaceName}</span>
          {waterPlaceDetails.waterPlaceImage !== null && (
            <span onClick={() => setClickImg(!clickImg)}>
              <BiImage color="white" size="28px" />
            </span>
          )}
          {clickImg && (
            <div className={styles.valleyDetailImg}>
              <img src={waterPlaceDetails.waterPlaceImage!} alt="계곡 이미지" />
              <span onClick={() => setClickImg(false)}>
                <IoMdClose size="30px" />
              </span>
            </div>
          )}
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
                  key={item}
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
                  <div key={index} className={styles.weatherItem}>
                    <div>
                      <div className={styles.weatherInfoIcon}>
                        <span>
                          <img
                            src={`https://openweathermap.org/img/wn/${item.climateIcon}@2x.png`}
                            alt="날씨 아이콘"
                            width="55px"
                          />
                        </span>
                        <span
                          style={
                            item.climateDescription.length > 7
                              ? { fontSize: "0.6rem" }
                              : {}
                          }
                        >
                          {item.climateDescription}
                        </span>
                      </div>
                      <div>
                        <div className={styles.temperature}>
                          <span>{item.lowestTemperature.toFixed()}°</span>
                          <span> / </span>
                          <span>{item.highestTemperature.toFixed()}°</span>
                        </div>
                        {weatherDetail[index].map((detail) => {
                          return (
                            <div
                              key={detail.name}
                              className={styles.weatherItemDetail}
                            >
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
                  </div>
                );
              })}
            </div>
            <div className={styles.mobileWeatherList}>
              {weatherList.map((item, index) => {
                return (
                  <div key={index} className={styles.mobileWeatherItem}>
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
                    <div className={styles.weatherInfo}>
                      <div className={styles.weatherInfoIcon}>
                        <span>
                          <img
                            src={`https://openweathermap.org/img/wn/${item.climateIcon}@2x.png`}
                            alt="날씨 아이콘"
                            width="45px"
                          />
                        </span>
                      </div>
                      <div className={styles.temperature}>
                        <span>{item.lowestTemperature.toFixed()}°</span>
                        <span> / </span>
                        <span>{item.highestTemperature.toFixed()}°</span>
                      </div>
                    </div>
                    <div className={styles.mobileWeatherDetail}>
                      <div className={styles.moblieWeatherItemDetail}>
                        {weatherDetail[index].map((detail) => {
                          return (
                            <div key={detail.name}>
                              <span>{detail.value}</span>
                            </div>
                          );
                        })}
                      </div>
                      <div className={styles.mobileWeatherDesc}>
                        {weatherDetail[index].map((detail) => {
                          return (
                            <div key={detail.name}>
                              <span>{detail.name}</span>
                            </div>
                          );
                        })}
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
            <div className={styles.rescueSuppliesTitle}>
              <span>구조용품 및 안내표지판 현황</span>
            </div>
            <div className={styles.rescueList}>
              <div className={styles.rescueItem}>
                <span>
                  <TbChartDonut4
                    size={innerWidth <= 540 ? "22px" : "30px"}
                    color="#66A5FC"
                  />
                </span>
                <span>구명환</span>
                <span>
                  {rescueSupplies.lifeRingNum === -1
                    ? "-"
                    : rescueSupplies.lifeRingNum}
                </span>
              </div>
              <div className={styles.rescueItem}>
                <span>
                  <TbJumpRope
                    size={innerWidth <= 540 ? "22px" : "30px"}
                    color="#66A5FC"
                  />
                </span>
                <span>구명로프</span>
                <span>
                  {rescueSupplies.rescueRopeNum === -1
                    ? "-"
                    : rescueSupplies.rescueRopeNum}
                </span>
              </div>
              <div className={styles.rescueItem}>
                <span>
                  <MdEmojiPeople
                    size={innerWidth <= 540 ? "22px" : "30px"}
                    color="#66A5FC"
                  />
                </span>
                <span>인명구조함</span>
                <span>
                  {rescueSupplies.lifeBoatNum === -1
                    ? "-"
                    : rescueSupplies.lifeBoatNum}
                </span>
              </div>
              <div className={styles.rescueItem}>
                <span>
                  <FaVest
                    size={innerWidth <= 540 ? "22px" : "30px"}
                    color="#66A5FC"
                  />
                </span>
                <span>구명조끼</span>
                <span>
                  {rescueSupplies.lifeJacketNum === -1
                    ? "-"
                    : rescueSupplies.lifeJacketNum}
                </span>
              </div>
              <div className={styles.rescueItem}>
                <span>
                  <MdHomeRepairService
                    size={innerWidth <= 540 ? "22px" : "30px"}
                    color="#66A5FC"
                  />
                </span>
                <span>이동식 거치대</span>
                <span>
                  {rescueSupplies.portableStandNum === -1
                    ? "-"
                    : rescueSupplies.portableStandNum}
                </span>
              </div>
              <div className={styles.rescueItem}>
                <span>
                  <LuUtilityPole
                    size={innerWidth <= 540 ? "22px" : "30px"}
                    color="#66A5FC"
                  />
                </span>
                <span>구명봉</span>
                <span>
                  {rescueSupplies.rescueRodNum === -1
                    ? "-"
                    : rescueSupplies.rescueRodNum}
                </span>
              </div>
              <div className={styles.rescueItem}>
                <span>
                  <BsFillClipboardCheckFill
                    size={innerWidth <= 540 ? "22px" : "30px"}
                    color="#66A5FC"
                  />
                </span>
                <span>위험구역 안내표지판</span>
                <span>
                  {waterPlaceDetails.dangerSignboardsNum === ""
                    ? "-"
                    : waterPlaceDetails.dangerSignboardsNum}
                </span>
              </div>
            </div>
          </div>
        </div>
      </div>
    </div>
  );
};

export default ValleyInfo;
