import React, { FC, useEffect, useState } from "react";
import styles from "../../css/main/Weather.module.css";
import Report from "./Report";
import WeatherDetail from "./WeatherDetail";

interface Props {
  nationalWeather: {
    weatherDate: string;
    dailyNationalWeather: {
      clouds: number;
      dayFeelsLike: number;
      humidity: number;
      windSpeed: number;
      region: string;
      weatherIcon: string;
      weatherDesc: string;
      minTemp: number;
      maxTemp: number;
      rainPrecipitation: number;
    }[];
  }[];

  alert: {
    weatherAlerts: {
      weatherAlertType: string;
      title: string;
      announcementTime: string;
      effectiveTime: string;
      content: string;
    }[];
    weatherPreAlerts: {
      announcementTime: string;
      title: string;
      weatherAlertType: string;
      contents: {
        content: string;
      }[];
    }[];
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

const Weather: FC<Props> = ({ nationalWeather, alert }) => {
  let dateArr = nationalWeather;

  const [loading, setLoading] = useState(false);
  const [clicked, setClicked] = useState(nationalWeather[0]);
  const [regionClicked, setRegionClicked] = useState(
    nationalWeather[0].dailyNationalWeather[0]
  );
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
    setLoading(true);
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
    setRegionClicked(nationalWeather[0].dailyNationalWeather[1]);
    setLoading(false);
  }, []);

  useEffect(() => {
    setRegionClicked(clicked.dailyNationalWeather[1]);
  }, [clicked]);

  if (loading) {
    return <div>loading</div>;
  }

  return (
    <div className={styles.weather}>
      <h4>전국날씨</h4>
      <div className={styles.dayContainer}>
        {dateArr.map((item, index) => {
          return (
            <div
              key={index}
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
      <div className={styles.weatherInfo}>
        <div className={styles.weatherMap}>
          <div className={styles.weatherMapContainer}>
            <img
              src={process.env.PUBLIC_URL + "/img/map_img.png"}
              alt="지도 이미지"
            ></img>
            {region.map((item, index) => {
              return (
                <div
                  key={item}
                  onClick={() =>
                    setRegionClicked(clicked.dailyNationalWeather[index])
                  }
                  style={
                    regionClicked.region === item
                      ? { borderColor: "#66A5FC" }
                      : {}
                  }
                  id={styles.regionContainer}
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
                >
                  <span
                    style={
                      regionClicked.region === item && index === 6
                        ? { color: "#66A5FC", fontSize: "0.6rem" }
                        : regionClicked.region === item
                        ? { color: "#66A5FC" }
                        : index === 6
                        ? { fontSize: "0.6rem" }
                        : {}
                    }
                  >
                    {item}
                  </span>
                  {clicked.dailyNationalWeather[0].weatherIcon !== "" && (
                    <img
                      src={`https://openweathermap.org/img/wn/${clicked.dailyNationalWeather[index].weatherIcon}@2x.png`}
                      alt="날씨 아이콘"
                      width="50px"
                    />
                  )}
                  {clicked.dailyNationalWeather[0].weatherIcon !== "" && (
                    <span>
                      <span style={{ fontSize: "0.7rem", color: "#3378FC" }}>
                        {clicked.dailyNationalWeather[index].minTemp.toFixed()}°
                      </span>
                      /
                      <span style={{ color: "#fd4848" }}>
                        {clicked.dailyNationalWeather[index].maxTemp.toFixed()}°
                      </span>
                    </span>
                  )}
                </div>
              );
            })}
          </div>
        </div>
        <div className={styles.weatherInfoDetail}>
          <Report alert={alert} />
          <WeatherDetail dailyNationalWeather={regionClicked} />
        </div>
      </div>
    </div>
  );
};

export default Weather;
