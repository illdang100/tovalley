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

const getDayOfWeek = (day: string) => {
  const week = ["일", "월", "화", "수", "목", "금", "토"];
  const dayOfWeek = week[new Date(day).getDay()];

  return dayOfWeek;
};

type regionType = {
  maxTemp: number;
  minTemp: number;
  rainPrecipitation: number;
  region: string;
  weatherDesc: string;
  weatherIcon: string;
}[];

const tempRegion = {
  maxTemp: 0,
  minTemp: 0,
  rainPrecipitation: 0,
  region: "",
  weatherDesc: "",
  weatherIcon: "",
};

const Weather: FC<Props> = ({ nationalWeather }) => {
  const [clicked, setClicked] = useState(nationalWeather[0]);
  const 춘천: regionType = clicked.dailyNationalWeather.map((item) => {
    return item.region === "춘천" ? item : tempRegion;
  });
  const 백령: regionType = clicked.dailyNationalWeather.map((item) => {
    return item.region === "백령" ? item : tempRegion;
  });
  const 서울: regionType = clicked.dailyNationalWeather.map((item) => {
    return item.region === "서울" ? item : tempRegion;
  });
  const 강릉: regionType = clicked.dailyNationalWeather.map((item) => {
    return item.region === "강릉" ? item : tempRegion;
  });
  const 청주: regionType = clicked.dailyNationalWeather.map((item) => {
    return item.region === "청주" ? item : tempRegion;
  });
  const 수원: regionType = clicked.dailyNationalWeather.map((item) => {
    return item.region === "수원" ? item : tempRegion;
  });
  const 울릉: regionType = clicked.dailyNationalWeather.map((item) => {
    return item.region === "울릉" ? item : tempRegion;
  });
  const 대전: regionType = clicked.dailyNationalWeather.map((item) => {
    return item.region === "대전" ? item : tempRegion;
  });
  const 안동: regionType = clicked.dailyNationalWeather.map((item) => {
    return item.region === "안동" ? item : tempRegion;
  });
  const 전주: regionType = clicked.dailyNationalWeather.map((item) => {
    return item.region === "전주" ? item : tempRegion;
  });
  const 대구: regionType = clicked.dailyNationalWeather.map((item) => {
    return item.region === "대구" ? item : tempRegion;
  });
  const 울산: regionType = clicked.dailyNationalWeather.map((item) => {
    return item.region === "울산" ? item : tempRegion;
  });
  const 광주: regionType = clicked.dailyNationalWeather.map((item) => {
    return item.region === "광주" ? item : tempRegion;
  });
  const 부산: regionType = clicked.dailyNationalWeather.map((item) => {
    return item.region === "부산" ? item : tempRegion;
  });
  const 목포: regionType = clicked.dailyNationalWeather.map((item) => {
    return item.region === "목포" ? item : tempRegion;
  });
  const 여수: regionType = clicked.dailyNationalWeather.map((item) => {
    return item.region === "여수" ? item : tempRegion;
  });
  const 제주: regionType = clicked.dailyNationalWeather.map((item) => {
    return item.region === "제주" ? item : tempRegion;
  });

  return (
    <div className={styles.weather}>
      <h4>전국날씨</h4>
      <div className={styles.dayContainer}>
        {nationalWeather.reverse().map((item) => {
          return (
            <div
              onClick={() => {
                setClicked(item);
              }}
              className={clicked === item ? styles.dateClicked : styles.date}
            >
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
          );
        })}
      </div>
      <div className={styles.weatherMap}>
        <img
          src={process.env.PUBLIC_URL + "/img/map_img.png"}
          alt="지도 이미지"
        ></img>
        <div className={styles.chuncheon}>
          <img
            src={`https://openweathermap.org/img/wn/${춘천[0].weatherIcon}@2x.png`}
            alt="날씨 아이콘"
            width="70px"
          />
          <span>춘천</span>
        </div>
        <div className={styles.baengnyeong}>
          <img
            src={`https://openweathermap.org/img/wn/${백령[0].weatherIcon}@2x.png`}
            alt="날씨 아이콘"
            width="70px"
          />
          <span>백령</span>
        </div>
        <div className={styles.seoul}>
          <img
            src={`https://openweathermap.org/img/wn/${서울[0].weatherIcon}@2x.png`}
            alt="날씨 아이콘"
            width="70px"
          />
          <span>서울</span>
        </div>
        <div className={styles.gangneung}>
          <img
            src={`https://openweathermap.org/img/wn/${강릉[0].weatherIcon}@2x.png`}
            alt="날씨 아이콘"
            width="70px"
          />
          <span>강릉</span>
        </div>
        <div className={styles.cheongju}>
          <img
            src={`https://openweathermap.org/img/wn/${청주[0].weatherIcon}@2x.png`}
            alt="날씨 아이콘"
            width="70px"
          />
          <span>청주</span>
        </div>
        <div className={styles.ulleung}>
          <img
            src={`https://openweathermap.org/img/wn/${울릉[0].weatherIcon}@2x.png`}
            alt="날씨 아이콘"
            width="70px"
          />
          <span>울릉</span>
        </div>
        <div className={styles.suwon}>
          <img
            src={`https://openweathermap.org/img/wn/${수원[0].weatherIcon}@2x.png`}
            alt="날씨 아이콘"
            width="70px"
          />
          <span>수원</span>
        </div>
        <div className={styles.andong}>
          <img
            src={`https://openweathermap.org/img/wn/${안동[0].weatherIcon}@2x.png`}
            alt="날씨 아이콘"
            width="70px"
          />
          <span>안동</span>
        </div>
        <div className={styles.daejeon}>
          <img
            src={`https://openweathermap.org/img/wn/${대전[0].weatherIcon}@2x.png`}
            alt="날씨 아이콘"
            width="70px"
          />
          <span>대전</span>
        </div>
        <div className={styles.jeonju}>
          <img
            src={`https://openweathermap.org/img/wn/${전주[0].weatherIcon}@2x.png`}
            alt="날씨 아이콘"
            width="70px"
          />
          <span>전주</span>
        </div>
        <div className={styles.daegu}>
          <img
            src={`https://openweathermap.org/img/wn/${대구[0].weatherIcon}@2x.png`}
            alt="날씨 아이콘"
            width="70px"
          />
          <span>대구</span>
        </div>
        <div className={styles.ulsan}>
          <img
            src={`https://openweathermap.org/img/wn/${울산[0].weatherIcon}@2x.png`}
            alt="날씨 아이콘"
            width="70px"
          />
          <span>울산</span>
        </div>
        <div className={styles.gwangju}>
          <img
            src={`https://openweathermap.org/img/wn/${광주[0].weatherIcon}@2x.png`}
            alt="날씨 아이콘"
            width="70px"
          />
          <span>광주</span>
        </div>
        <div className={styles.mokpo}>
          <img
            src={`https://openweathermap.org/img/wn/${목포[0].weatherIcon}@2x.png`}
            alt="날씨 아이콘"
            width="70px"
          />
          <span>목포</span>
        </div>
        <div className={styles.yeosu}>
          <img
            src={`https://openweathermap.org/img/wn/${여수[0].weatherIcon}@2x.png`}
            alt="날씨 아이콘"
            width="70px"
          />
          <span>여수</span>
        </div>
        <div className={styles.busan}>
          <img
            src={`https://openweathermap.org/img/wn/${부산[0].weatherIcon}@2x.png`}
            alt="날씨 아이콘"
            width="70px"
          />
          <span>부산</span>
        </div>
        <div className={styles.jeju}>
          <img
            src={`https://openweathermap.org/img/wn/${제주[0].weatherIcon}@2x.png`}
            alt="날씨 아이콘"
            width="70px"
          />
          <span>제주</span>
        </div>
      </div>
    </div>
  );
};

export default Weather;
