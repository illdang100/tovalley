import React, { useState } from "react";
import styles from "../../css/main/Weather.module.css";
import KakaoMap from "./KakaoMap";

const Weather = () => {
  const day: string[] = ["토", "일", "월", "화", "수"];
  const date: string[] = ["7.29", "7.30", "7.31", "8.1", "8.2"];
  const [clicked, setClicked] = useState(day[0]);

  return (
    <div className={styles.weather}>
      <h4>전국날씨</h4>
      <div className={styles.dayContainer}>
        {day.map((item, index) => {
          return (
            <div
              onClick={() => {
                setClicked(`${item}`);
              }}
              className={clicked === item ? styles.dateClicked : styles.date}
            >
              <span>{item}</span>
              <span>{date[index]}</span>
            </div>
          );
        })}
      </div>
      <div className={styles.weatherMap}>
        <KakaoMap />
      </div>
    </div>
  );
};

export default Weather;
