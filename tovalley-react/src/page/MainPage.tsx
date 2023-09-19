import styles from "../css/main/MainPage.module.css";
import Header from "../component/header/Header";
import Weather from "../component/main/Weather";
import Report from "../component/main/Report";
import Accident from "../component/main/Accident/Accident";
import PopularValley from "../component/main/PopularValley";
import Footer from "../component/footer/Footer";
import React, { useEffect, useState } from "react";
import axios from "axios";

const localhost = "http://43.202.36.150";

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

  const [regionAccident, setRegionAccident] = useState({
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
  });

  const [popularValley, setPopularValley] = useState([
    {
      waterPlaceId: 0,
      waterPlaceName: "",
      waterPlaceImage: "",
      location: "",
      rating: 0,
      reviewCnt: 0,
    },
  ]);

  useEffect(() => {
    axios
      .get(`${localhost}/api/main-page`)
      .then((res) => {
        console.log(res);
        setMain(res.data.data);
        setRegionAccident(res.data.data.accidentCountDto);
        setPopularValley(res.data.data.nationalPopularWaterPlaces);
      })
      .catch((err) => console.log(err));

    setMain({
      nationalWeather: [
        {
          weatherDate: "2023-08-11",
          dailyNationalWeather: [
            {
              region: "춘천",
              weatherIcon: "10d",
              weatherDesc: "실 비",
              minTemp: 24.26,
              maxTemp: 24.72,
              rainPrecipitation: 10.53,
            },
            {
              region: "백령",
              weatherIcon: "10d",
              weatherDesc: "실 비",
              minTemp: 26.28,
              maxTemp: 27.16,
              rainPrecipitation: 1.42,
            },
          ],
        },
        {
          weatherDate: "2023-08-12",
          dailyNationalWeather: [
            {
              region: "백령",
              weatherIcon: "10d",
              weatherDesc: "실 비",
              minTemp: 24.43,
              maxTemp: 24.91,
              rainPrecipitation: 1.51,
            },
            {
              region: "여수",
              weatherIcon: "04d",
              weatherDesc: "튼구름",
              minTemp: 25.65,
              maxTemp: 26.99,
              rainPrecipitation: 0.0,
            },
          ],
        },
        {
          weatherDate: "2023-08-13",
          dailyNationalWeather: [
            {
              region: "백령",
              weatherIcon: "10d",
              weatherDesc: "실 비",
              minTemp: 24.62,
              maxTemp: 25.38,
              rainPrecipitation: 1.86,
            },
            {
              region: "여수",
              weatherIcon: "10d",
              weatherDesc: "실 비",
              minTemp: 25.65,
              maxTemp: 27.27,
              rainPrecipitation: 0.13,
            },
          ],
        },
        {
          weatherDate: "2023-08-14",
          dailyNationalWeather: [
            {
              region: "백령",
              weatherIcon: "10d",
              weatherDesc: "실 비",
              minTemp: 25.05,
              maxTemp: 25.89,
              rainPrecipitation: 1.33,
            },
            {
              region: "여수",
              weatherIcon: "10d",
              weatherDesc: "실 비",
              minTemp: 26.55,
              maxTemp: 27.29,
              rainPrecipitation: 3.79,
            },
          ],
        },
        {
          weatherDate: "2023-08-15",
          dailyNationalWeather: [
            {
              region: "백령",
              weatherIcon: "10d",
              weatherDesc: "실 비",
              minTemp: 25.27,
              maxTemp: 25.82,
              rainPrecipitation: 2.99,
            },
            {
              region: "여수",
              weatherIcon: "10d",
              weatherDesc: "실 비",
              minTemp: 26.59,
              maxTemp: 27.88,
              rainPrecipitation: 3.69,
            },
          ],
        },
      ],
      weatherAlert: {
        weatherAlerts: [
          {
            weatherAlertType: "폭염",
            title: "폭염경보",
            announcementTime: "2023-08-14 16:00:00",
            effectiveTime: "2023-08-14 16:00:00",
            content:
              "경기도(양주, 안성, 양평), 강원도(홍천평지), 전라남도(담양, 곡성, 구례, 화순, 순천, 영암), 광주",
          },
          {
            weatherAlertType: "폭염",
            title: "폭염주의보",
            announcementTime: "2023-08-14 16:00:00",
            effectiveTime: "2023-08-14 16:00:00",
            content:
              "경기도(양주, 안성, 양평 제외), 강원도(영월, 정선평지, 횡성, 원주, 철원, 화천, 춘천, 인제평지, 강원북부산지), 충청남도(서천 제외), 충청북도, 전라남도(나주, 장성, 고흥, 보성, 여수, 광양, 장흥, 강진, 해남, 완도, 무안, 함평, 영광, 목포, 신안(흑산면제외), 진도, 흑산도.홍도, 거문도.초도), 전라북도, 경상북도(구미, 영천, 경산, 군위, 청도, 고령, 성주, 칠곡, 김천, 상주, 예천, 안동, 영주, 의성), 경상남도(거창 제외), 제주도(제주도서부, 제주도북부, 제주도동부), 서울, 인천(옹진군 제외), 대전, 대구, 부산, 세종",
          },
        ],
        weatherPreAlerts: [
          {
            announcementTime: "2023-08-14 16:00:00",
            title: "강풍 예비특보",
            weatherAlertType: "강풍",
            contents: [
              {
                content: "08월 15일 새벽(00시~06시) : 울릉도.독도",
              },
            ],
          },
          {
            announcementTime: "2023-08-14 16:00:00",
            title: "풍랑 예비특보",
            weatherAlertType: "풍랑",
            contents: [
              {
                content: "08월 14일 밤(21시~24시) : 동해중부바깥먼바다",
              },
              {
                content:
                  "08월 15일 새벽(00시~06시) : 동해남부남쪽안쪽먼바다, 동해남부남쪽바깥먼바다, 동해남부북쪽안쪽먼바다, 동해남부북쪽바깥먼바다, 동해중부안쪽먼바다",
              },
              {
                content:
                  "08월 15일 오전(06시~12시) : 동해남부앞바다, 동해중부앞바다, 남해동부안쪽먼바다",
              },
              {
                content:
                  "08월 15일 오후(12시~18시) : 남해동부앞바다(부산앞바다)",
              },
            ],
          },
        ],
      },
      accidentCountDto: {
        accidentCountPerMonth: [
          {
            month: 1,
            deathCnt: 0,
            disappearanceCnt: 0,
            injuryCnt: 0,
          },
          {
            month: 2,
            deathCnt: 0,
            disappearanceCnt: 0,
            injuryCnt: 0,
          },
          {
            month: 3,
            deathCnt: 0,
            disappearanceCnt: 0,
            injuryCnt: 0,
          },
          {
            month: 4,
            deathCnt: 0,
            disappearanceCnt: 0,
            injuryCnt: 0,
          },
        ],
        province: "전국",
        totalDeathCnt: 10,
        totalDisappearanceCnt: 20,
        totalInjuryCnt: 0,
      },
      nationalPopularWaterPlaces: [
        {
          waterPlaceId: 1,
          waterPlaceName: "관악산계곡어린이물놀이장",
          waterPlaceImage: "http://www.dsfoifsdjoij3.com",
          location: "서울특별시 관악구",
          rating: 0.0,
          reviewCnt: 0,
        },
        {
          waterPlaceId: 1,
          waterPlaceName: "관악산계곡어린이물놀이장",
          waterPlaceImage: "http://www.dsfoifsdjoij3.com",
          location: "서울특별시 관악구",
          rating: 0.0,
          reviewCnt: 0,
        },
        {
          waterPlaceId: 1,
          waterPlaceName: "관악산계곡어린이물놀이장",
          waterPlaceImage: "http://www.dsfoifsdjoij3.com",
          location: "서울특별시 관악구",
          rating: 0.0,
          reviewCnt: 0,
        },
        {
          waterPlaceId: 1,
          waterPlaceName: "관악산계곡어린이물놀이장",
          waterPlaceImage: "http://www.dsfoifsdjoij3.com",
          location: "서울특별시 관악구",
          rating: 0.0,
          reviewCnt: 0,
        },
        {
          waterPlaceId: 1,
          waterPlaceName: "관악산계곡어린이물놀이장",
          waterPlaceImage: "http://www.dsfoifsdjoij3.com",
          location: "서울특별시 관악구",
          rating: 0.0,
          reviewCnt: 0,
        },
        {
          waterPlaceId: 1,
          waterPlaceName: "관악산계곡어린이물놀이장",
          waterPlaceImage: "http://www.dsfoifsdjoij3.com",
          location: "서울특별시 관악구",
          rating: 0.0,
          reviewCnt: 0,
        },
        {
          waterPlaceId: 4,
          waterPlaceName: "장안사 계곡",
          waterPlaceImage: "http://www.dsfoifsdjoij3.com",
          location: "부산광역시 기장군",
          rating: 0.0,
          reviewCnt: 0,
        },
        {
          waterPlaceId: 4,
          waterPlaceName: "장안사 계곡",
          waterPlaceImage: "http://www.dsfoifsdjoij3.com",
          location: "부산광역시 기장군",
          rating: 0.0,
          reviewCnt: 0,
        },
      ],
    });

    setRegionAccident({
      accidentCountPerMonth: [
        {
          month: 1,
          deathCnt: 0,
          disappearanceCnt: 0,
          injuryCnt: 0,
        },
        {
          month: 2,
          deathCnt: 0,
          disappearanceCnt: 0,
          injuryCnt: 0,
        },
        {
          month: 3,
          deathCnt: 0,
          disappearanceCnt: 0,
          injuryCnt: 0,
        },
        {
          month: 4,
          deathCnt: 0,
          disappearanceCnt: 0,
          injuryCnt: 0,
        },
      ],
      province: "전국",
      totalDeathCnt: 10,
      totalDisappearanceCnt: 0,
      totalInjuryCnt: 0,
    });

    setPopularValley([
      {
        waterPlaceId: 1,
        waterPlaceName: "관악산계곡어린이물놀이장",
        waterPlaceImage: "http://www.dsfoifsdjoij3.com",
        location: "서울특별시 관악구",
        rating: 0.0,
        reviewCnt: 0,
      },
      {
        waterPlaceId: 1,
        waterPlaceName: "관악산계곡어린이물놀이장",
        waterPlaceImage: "http://www.dsfoifsdjoij3.com",
        location: "서울특별시 관악구",
        rating: 0.0,
        reviewCnt: 0,
      },
      {
        waterPlaceId: 1,
        waterPlaceName: "관악산계곡어린이물놀이장",
        waterPlaceImage: "http://www.dsfoifsdjoij3.com",
        location: "서울특별시 관악구",
        rating: 0.0,
        reviewCnt: 0,
      },
      {
        waterPlaceId: 1,
        waterPlaceName: "관악산계곡어린이물놀이장",
        waterPlaceImage: "http://www.dsfoifsdjoij3.com",
        location: "서울특별시 관악구",
        rating: 0.0,
        reviewCnt: 0,
      },
      {
        waterPlaceId: 1,
        waterPlaceName: "관악산계곡어린이물놀이장",
        waterPlaceImage: "http://www.dsfoifsdjoij3.com",
        location: "서울특별시 관악구",
        rating: 0.0,
        reviewCnt: 0,
      },
      {
        waterPlaceId: 1,
        waterPlaceName: "관악산계곡어린이물놀이장",
        waterPlaceImage: "http://www.dsfoifsdjoij3.com",
        location: "서울특별시 관악구",
        rating: 0.0,
        reviewCnt: 0,
      },
      {
        waterPlaceId: 4,
        waterPlaceName: "장안사 계곡",
        waterPlaceImage: "http://www.dsfoifsdjoij3.com",
        location: "부산광역시 기장군",
        rating: 0.0,
        reviewCnt: 0,
      },
      {
        waterPlaceId: 4,
        waterPlaceName: "장안사 계곡",
        waterPlaceImage: "http://www.dsfoifsdjoij3.com",
        location: "부산광역시 기장군",
        rating: 0.0,
        reviewCnt: 0,
      },
    ]);
  }, []);

  return (
    <div>
      <Header />
      <div className={styles.body}>
        <div className={styles.top}>
          <Weather nationalWeather={main.nationalWeather} />
          <Report alert={main.weatherAlert} />
          <Accident
            accident={regionAccident}
            setRegionAccident={setRegionAccident}
          />
        </div>
        <div>
          <PopularValley
            place={popularValley}
            setPopularValley={setPopularValley}
          />
        </div>
      </div>
      <Footer />
    </div>
  );
};

export default MainPage;
