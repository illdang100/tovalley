import React from "react";
import styles from "../../css/main/WeatherDetail.module.css";
import { FaMapMarkerAlt } from "react-icons/fa";

const WeatherDetail = () => {
  const detailMenu = ["강수량", "습도", "풍속"];
  return (
    <div className={styles.weatherDetail}>
      <div className={styles.region}>
        <span>
          <FaMapMarkerAlt color="#383838" size="18px" />
        </span>
        <span>서울</span>
      </div>
      <div className={styles.detailBox}>
        <div className={styles.detailMain}>
          <div>
            <img
              src={`https://openweathermap.org/img/wn/10d@2x.png`}
              alt="날씨 아이콘"
              width="90px"
            />
          </div>
          <div className={styles.detailMainInfo}>
            <div>
              <span>주간 체감온도</span>
              <span>23.7°</span>
            </div>
            <div>
              <span>흐림</span>
              <span>10%</span>
            </div>
          </div>
        </div>
        <div className={styles.temperature}>
          <div>
            <span>최저</span>
            <span style={{ color: "#3378FC" }}>19°</span>
          </div>
          <div>
            <span>최고</span>
            <span style={{ color: "#FD4848" }}>28°</span>
          </div>
        </div>
        <div className={styles.weatherAddList}>
          {detailMenu.map((item, index) => {
            return (
              <div className={styles.weatherAdd}>
                <span>{item}</span>
                <div>
                  <span>10</span>
                  <span>{index === 0 ? "mm" : index === 1 ? "%" : "m/s"}</span>
                </div>
              </div>
            );
          })}
        </div>
      </div>
    </div>
  );
};

export default WeatherDetail;
