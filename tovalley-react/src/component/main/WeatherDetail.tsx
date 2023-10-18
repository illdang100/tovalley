import React, { FC } from "react";
import styles from "../../css/main/WeatherDetail.module.css";
import { FaMapMarkerAlt } from "react-icons/fa";

interface Props {
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
  };
}

const WeatherDetail: FC<Props> = ({ dailyNationalWeather }) => {
  const detailMenu = ["강수량", "습도", "풍속"];
  return (
    <div className={styles.weatherDetail}>
      <div className={styles.region}>
        <span>
          <FaMapMarkerAlt color="#383838" size="18px" />
        </span>
        <span>{dailyNationalWeather.region}</span>
      </div>
      <div className={styles.detailBox}>
        <div className={styles.detailMain}>
          <div>
            <img
              src={`https://openweathermap.org/img/wn/${dailyNationalWeather.weatherIcon}@2x.png`}
              alt="날씨 아이콘"
              width="90px"
            />
          </div>
          <div className={styles.detailMainInfo}>
            <div>
              <span
                style={
                  dailyNationalWeather.weatherDesc.length > 6
                    ? { fontSize: "0.8rem" }
                    : { fontSize: "1rem" }
                }
              >
                {dailyNationalWeather.weatherDesc}
              </span>
            </div>
            <div>
              <span>체감온도</span>
              <span>{dailyNationalWeather.dayFeelsLike.toFixed()}°</span>
            </div>
            <div>
              <span>흐림</span>
              <span>{dailyNationalWeather.clouds}%</span>
            </div>
          </div>
        </div>
        <div className={styles.temperature}>
          <div>
            <span>최저</span>
            <span style={{ color: "#3378FC" }}>
              {dailyNationalWeather.minTemp.toFixed()}°
            </span>
          </div>
          <div>
            <span>최고</span>
            <span style={{ color: "#FD4848" }}>
              {dailyNationalWeather.maxTemp.toFixed()}°
            </span>
          </div>
        </div>
        <div className={styles.weatherAddList}>
          {detailMenu.map((item, index) => {
            return (
              <div className={styles.weatherAdd}>
                <span>{item}</span>
                <div>
                  <span>
                    {index === 0
                      ? dailyNationalWeather.rainPrecipitation.toFixed()
                      : index === 1
                      ? dailyNationalWeather.humidity
                      : dailyNationalWeather.windSpeed.toFixed()}
                  </span>
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
