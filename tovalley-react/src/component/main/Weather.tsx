import React, { FC, useEffect, useState } from "react";
import styles from "../../css/main/Weather.module.css";
import { FaTemperatureEmpty } from "react-icons/fa6";
import { BsCloudRainHeavyFill } from "react-icons/bs";

interface Props {
  nationalWeather: {
    weatherDate: string;
    dailyNationalWeather: {
      region: string;
      weatherIcon: string;
      weatherDesc: string;
      minTemp: number;
      maxTemp: number;
      rainPrecipitation: number;
    }[];
  }[];
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

const weatherSort = (date: string) => {
  let createDate = new Date(date);

  let formatedDate = `${createDate.getMonth() + 1}.${createDate.getDate()}`;

  return formatedDate;
};

const getDayOfWeek = (day: string) => {
  const week = ["일", "월", "화", "수", "목", "금", "토"];
  const dayOfWeek = week[new Date(day).getDay()];

  return dayOfWeek;
};

const Weather: FC<Props> = ({ nationalWeather }) => {
  let dateArr = nationalWeather;

  const [clicked, setClicked] = useState(nationalWeather[0]);
  const [hover, setHover] = useState("");
  const region = [
    "백령",
    "서울",
    "춘천",
    "강릉",
    "수원",
    "청주",
    "울릉/독도",
    "대전",
    "안동",
    "전주",
    "대구",
    "울산",
    "광주",
    "목포",
    "부산",
    "제주",
    "여수",
  ];

  useEffect(() => {
    for (let i = 0; i < dateArr.length; i++) {
      for (let j = 0; j < dateArr.length - 1; j++) {
        if (
          Number(weatherSort(dateArr[j].weatherDate)) >
          Number(weatherSort(dateArr[j + 1].weatherDate))
        ) {
          let temp = dateArr[j];
          dateArr[j] = dateArr[j + 1];
          dateArr[j + 1] = temp;
        }
      }
    }

    setClicked(dateArr[0]);
  }, []);

  return (
    <div className={styles.weather}>
      <h4>전국날씨</h4>
      <div className={styles.dayContainer}>
        {dateArr.map((item) => {
          return (
            <div
              onClick={() => {
                setClicked(item);
                console.log(item);
              }}
              className={clicked === item ? styles.dateClicked : styles.date}
            >
              {dateFormat(item.weatherDate) === "오늘" ? (
                <>
                  <span
                    style={
                      clicked.weatherDate === "" ? { color: "#66A5FC" } : {}
                    }
                  >
                    {getDayOfWeek(item.weatherDate)}
                  </span>
                  <span
                    style={
                      clicked.weatherDate === ""
                        ? {
                            color: "#66A5FC",
                          }
                        : {}
                    }
                  >
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
          );
        })}
      </div>
      <div className={styles.weatherMap}>
        <div className={styles.weatherMapContainer}>
          <img
            src={process.env.PUBLIC_URL + "/img/map_img.png"}
            alt="지도 이미지"
          ></img>
          {region.map((item, index) => {
            return (
              <div
                className={
                  index === 0
                    ? styles.baengnyeong
                    : index === 1
                    ? styles.seoul
                    : index === 2
                    ? styles.chuncheon
                    : index === 3
                    ? styles.gangneung
                    : index === 4
                    ? styles.suwon
                    : index === 5
                    ? styles.cheongju
                    : index === 6
                    ? styles.ulleung
                    : index === 7
                    ? styles.daejeon
                    : index === 8
                    ? styles.andong
                    : index === 9
                    ? styles.jeonju
                    : index === 10
                    ? styles.daegu
                    : index === 11
                    ? styles.ulsan
                    : index === 12
                    ? styles.gwangju
                    : index === 13
                    ? styles.mokpo
                    : index === 14
                    ? styles.busan
                    : index === 15
                    ? styles.jeju
                    : styles.yeosu
                }
                onMouseOver={() => setHover(item)}
                onMouseLeave={() => setHover("")}
              >
                {clicked.dailyNationalWeather[0].weatherIcon !== "" && (
                  <img
                    src={`https://openweathermap.org/img/wn/${clicked.dailyNationalWeather[index].weatherIcon}@2x.png`}
                    alt="날씨 아이콘"
                    width="70px"
                  />
                )}
                <span>{item}</span>
                {hover === item && (
                  <div className={styles.weatherDetail}>
                    <div>
                      <span>
                        <FaTemperatureEmpty />
                      </span>
                      <span>
                        {clicked.dailyNationalWeather[index].minTemp.toFixed()}
                        °/
                        {clicked.dailyNationalWeather[index].maxTemp.toFixed()}°
                      </span>
                    </div>
                    <div>
                      <span>
                        <BsCloudRainHeavyFill />
                      </span>
                      <span>
                        {clicked.dailyNationalWeather[
                          index
                        ].rainPrecipitation.toFixed()}
                        %
                      </span>
                    </div>
                  </div>
                )}
              </div>
            );
          })}
        </div>
      </div>
    </div>
  );
};

export default Weather;
