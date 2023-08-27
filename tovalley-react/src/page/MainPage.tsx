import styles from "../css/main/MainPage.module.css";
import Header from "../component/header/Header";
import Weather from "../component/main/Weather";
import Report from "../component/main/Report";
import Accident from "../component/main/Accident/Accident";
import PopularValley from "../component/main/PopularValley";
import Footer from "../component/footer/Footer";
import React, { useEffect, useState } from "react";
import axios from "axios";

const localhost = "http://localhost:8081";

type main = {
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
  weatherAlert: {
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
  accidentCountDto: {
    accidentCountPerMonth: {
      month: number;
      deathCnt: number;
      disappearanceCnt: number;
      injuryCnt: number;
    }[];
    province: string;
    totalDeathCnt: number;
    totalDisappearanceCnt: number;
    totalInjuryCnt: number;
  };
  nationalPopularWaterPlaces: {
    waterPlaceId: number;
    waterPlaceName: string;
    waterPlaceImage: string;
    location: string;
    rating: number;
    reviewCnt: number;
  }[];
};

const MainPage = () => {
  const [main, setMain] = useState<main>({
    nationalWeather: [
      {
        weatherDate: "",
        dailyNationalWeather: [
          {
            region: "",
            weatherIcon: "",
            weatherDesc: "",
            minTemp: 0,
            maxTemp: 0,
            rainPrecipitation: 0,
          },
        ],
      },
    ],
    weatherAlert: {
      weatherAlerts: [
        {
          weatherAlertType: "",
          title: "",
          announcementTime: "",
          effectiveTime: "",
          content: "",
        },
      ],
      weatherPreAlerts: [
        {
          announcementTime: "",
          title: "",
          weatherAlertType: "",
          contents: [
            {
              content: "",
            },
          ],
        },
      ],
    },
    accidentCountDto: {
      accidentCountPerMonth: [
        {
          month: 0,
          deathCnt: 0,
          disappearanceCnt: 0,
          injuryCnt: 0,
        },
      ],
      province: "전국",
      totalDeathCnt: 0,
      totalDisappearanceCnt: 0,
      totalInjuryCnt: 0,
    },
    nationalPopularWaterPlaces: [
      {
        waterPlaceId: 0,
        waterPlaceName: "",
        waterPlaceImage: "",
        location: "",
        rating: 0,
        reviewCnt: 0,
      },
    ],
  });

  useEffect(() => {
    axios
      .get(`${localhost}/api/main-page`)
      .then((res) => {
        console.log(res.data.data);
        setMain(res.data.data);
      })
      .catch((err) => console.log(err));
  }, []);

  return (
    <div>
      <Header />
      <div className={styles.body}>
        <div className={styles.top}>
          <Weather />
          <Report alert={main.weatherAlert} />
          <Accident accident={main.accidentCountDto} />
        </div>
        <div>
          <PopularValley place={main.nationalPopularWaterPlaces} />
        </div>
      </div>
      <Footer />
    </div>
  );
};

export default MainPage;
