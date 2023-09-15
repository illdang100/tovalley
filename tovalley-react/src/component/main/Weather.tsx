import React, { FC, useState } from "react";
import styles from "../../css/main/Weather.module.css";

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

  const [clicked, setClicked] = useState(dateArr[0]);

  return (
    <div className={styles.weather}>
      <h4>전국날씨</h4>
      <div className={styles.dayContainer}>
        {dateArr.map((item) => {
          return (
            <div
              onClick={() => {
                setClicked(item);
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
        <img
          src={process.env.PUBLIC_URL + "/img/map_img.png"}
          alt="지도 이미지"
        ></img>
        {/* <div className={styles.chuncheon}>
          <img
            src={`https://openweathermap.org/img/wn/${clicked.dailyNationalWeather[2].weatherIcon}@2x.png`}
            alt="날씨 아이콘"
            width="70px"
          />
          <span>춘천</span>
        </div>
        <div className={styles.baengnyeong}>
          <img
            src={`https://openweathermap.org/img/wn/${clicked.dailyNationalWeather[0].weatherIcon}@2x.png`}
            alt="날씨 아이콘"
            width="70px"
          />
          <span>백령</span>
        </div>
        <div className={styles.seoul}>
          <img
            src={`https://openweathermap.org/img/wn/${clicked.dailyNationalWeather[1].weatherIcon}@2x.png`}
            alt="날씨 아이콘"
            width="70px"
          />
          <span>서울</span>
        </div>
        <div className={styles.gangneung}>
          <img
            src={`https://openweathermap.org/img/wn/${clicked.dailyNationalWeather[3].weatherIcon}@2x.png`}
            alt="날씨 아이콘"
            width="70px"
          />
          <span>강릉</span>
        </div>
        <div className={styles.cheongju}>
          <img
            src={`https://openweathermap.org/img/wn/${clicked.dailyNationalWeather[5].weatherIcon}@2x.png`}
            alt="날씨 아이콘"
            width="70px"
          />
          <span>청주</span>
        </div>
        <div className={styles.ulleung}>
          <img
            src={`https://openweathermap.org/img/wn/${clicked.dailyNationalWeather[6].weatherIcon}@2x.png`}
            alt="날씨 아이콘"
            width="70px"
          />
          <span>울릉</span>
        </div>
        <div className={styles.suwon}>
          <img
            src={`https://openweathermap.org/img/wn/${clicked.dailyNationalWeather[4].weatherIcon}@2x.png`}
            alt="날씨 아이콘"
            width="70px"
          />
          <span>수원</span>
        </div>
        <div className={styles.andong}>
          <img
            src={`https://openweathermap.org/img/wn/${clicked.dailyNationalWeather[8].weatherIcon}@2x.png`}
            alt="날씨 아이콘"
            width="70px"
          />
          <span>안동</span>
        </div>
        <div className={styles.daejeon}>
          <img
            src={`https://openweathermap.org/img/wn/${clicked.dailyNationalWeather[7].weatherIcon}@2x.png`}
            alt="날씨 아이콘"
            width="70px"
          />
          <span>대전</span>
        </div>
        <div className={styles.jeonju}>
          <img
            src={`https://openweathermap.org/img/wn/${clicked.dailyNationalWeather[9].weatherIcon}@2x.png`}
            alt="날씨 아이콘"
            width="70px"
          />
          <span>전주</span>
        </div>
        <div className={styles.daegu}>
          <img
            src={`https://openweathermap.org/img/wn/${clicked.dailyNationalWeather[10].weatherIcon}@2x.png`}
            alt="날씨 아이콘"
            width="70px"
          />
          <span>대구</span>
        </div>
        <div className={styles.ulsan}>
          <img
            src={`https://openweathermap.org/img/wn/${clicked.dailyNationalWeather[11].weatherIcon}@2x.png`}
            alt="날씨 아이콘"
            width="70px"
          />
          <span>울산</span>
        </div>
        <div className={styles.gwangju}>
          <img
            src={`https://openweathermap.org/img/wn/${clicked.dailyNationalWeather[12].weatherIcon}@2x.png`}
            alt="날씨 아이콘"
            width="70px"
          />
          <span>광주</span>
        </div>
        <div className={styles.mokpo}>
          <img
            src={`https://openweathermap.org/img/wn/${clicked.dailyNationalWeather[13].weatherIcon}@2x.png`}
            alt="날씨 아이콘"
            width="70px"
          />
          <span>목포</span>
        </div>
        <div className={styles.yeosu}>
          <img
            src={`https://openweathermap.org/img/wn/${clicked.dailyNationalWeather[16].weatherIcon}@2x.png`}
            alt="날씨 아이콘"
            width="70px"
          />
          <span>여수</span>
        </div>
        <div className={styles.busan}>
          <img
            src={`https://openweathermap.org/img/wn/${clicked.dailyNationalWeather[14].weatherIcon}@2x.png`}
            alt="날씨 아이콘"
            width="70px"
          />
          <span>부산</span>
        </div>
        <div className={styles.jeju}>
          <img
            src={`https://openweathermap.org/img/wn/${clicked.dailyNationalWeather[15].weatherIcon}@2x.png`}
            alt="날씨 아이콘"
            width="70px"
          />
          <span>제주</span> 
        </div>*/}
      </div>
    </div>
  );
};

export default Weather;
