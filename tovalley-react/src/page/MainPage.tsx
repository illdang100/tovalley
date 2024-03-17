import styles from "../css/main/MainPage.module.css";
import Header from "../component/header/Header";
import Weather from "../component/main/Weather";
import Accident from "../component/main/Accident/Accident";
import PopularValley from "../component/main/PopularValley";
import Footer from "../component/footer/Footer";
import React, { useEffect, useState } from "react";
import axios from "axios";
import RecentPost from "../component/main/RecentPost";
import { RecentPostType } from "../typings/db";

const localhost = process.env.REACT_APP_HOST;

type main = {
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
    waterPlaceImageUrl: string;
    location: string;
    rating: number;
    reviewCnt: number;
  }[];
};

const MainPage = () => {
  const [loading, setLoading] = useState(false);
  const [main, setMain] = useState<main>({
    nationalWeather: [
      {
        weatherDate: "",
        dailyNationalWeather: [
          {
            clouds: 0,
            dayFeelsLike: 0,
            humidity: 0,
            windSpeed: 0,
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
        waterPlaceImageUrl: "",
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
      waterPlaceImageUrl: "",
      location: "",
      rating: 0,
      reviewCnt: 0,
    },
  ]);

  useEffect(() => {
    setLoading(true);
    axios
      .get(`${localhost}/api/main-page`)
      .then((res) => {
        console.log(res);
        setMain(res.data.data);
        setRegionAccident(res.data.data.accidentCountDto);
        setPopularValley(res.data.data.nationalPopularWaterPlaces);
      })
      .catch((err) => console.log(err))
      .finally(() => setLoading(false));
  }, []);

  const recentLostItem: RecentPostType[] = [
    {
      lostFoundBoardId: 1,
      lostFoundBoardCategory: "LOST",
      lostFoundBoardTitle: "핸드폰을 잃어버렸어요",
      lostFoundBoardContent: "보신 분 있으신가요?",
      lostFoundBoardCreatedAt: "2024/03/13",
    },
    {
      lostFoundBoardId: 2,
      lostFoundBoardCategory: "FOUND",
      lostFoundBoardTitle: "가천 계곡에서 우산 찾았습니다.",
      lostFoundBoardContent: "주인 있으시면 채팅 주세요.",
      lostFoundBoardCreatedAt: "2024/03/13",
    },
    {
      lostFoundBoardId: 3,
      lostFoundBoardCategory: "LOST",
      lostFoundBoardTitle: "튜브 보신 분 있으시면 연락 부탁드립니다.",
      lostFoundBoardContent: "분홍색 튜브입니다.",
      lostFoundBoardCreatedAt: "2024/03/13",
    },
    {
      lostFoundBoardId: 4,
      lostFoundBoardCategory: "LOST",
      lostFoundBoardTitle: "귀걸이 잃어버렸어요.",
      lostFoundBoardContent: "급하게 찾습니다.",
      lostFoundBoardCreatedAt: "2024/03/13",
    },
  ];

  const recentReview = [
    {
      reviewId: 1,
      reviewRating: 3,
      reviewTitle: "물놀이 하기 좋아요",
      reviewContent: "물이 깨끗해요",
      reviewCreatedAt: "2024/03/13",
    },
    {
      reviewId: 2,
      reviewRating: 3,
      reviewTitle: "돌이 미끄러워서 위험해요",
      reviewContent: "아이들하고 갈 때는 조심해야 할 것 같아요.",
      reviewCreatedAt: "2024/03/13",
    },
    {
      reviewId: 3,
      reviewRating: 3,
      reviewTitle: "사람이 많이 없어서 좋았어요.",
      reviewContent: "추천합니다.",
      reviewCreatedAt: "2024/03/13",
    },
    {
      reviewId: 4,
      reviewRating: 3,
      reviewTitle: "다음에도 오고싶어요.",
      reviewContent: "물이 깨끗하고 경치가 좋아요.",
      reviewCreatedAt: "2024/03/13",
    },
  ];

  const dummyValley = [
    {
      waterPlaceId: 1,
      waterPlaceName: "장흥저수지 상류계곡",
      waterPlaceImageUrl: "/img/dummy/계곡이미지5.jpg",
      location: "경상남도 양산시",
      rating: 4.2,
      reviewCnt: 320,
    },
    {
      waterPlaceId: 2,
      waterPlaceName: "명곡저수지 상류계곡",
      waterPlaceImageUrl: "/img/dummy/계곡이미지7.jpg",
      location: "경상남도 양산시",
      rating: 4.1,
      reviewCnt: 258,
    },
    {
      waterPlaceId: 3,
      waterPlaceName: "관악산계곡어린이물놀이장",
      waterPlaceImageUrl: "/img/dummy/계곡이미지6.jpg",
      location: "서울특별시 관악구",
      rating: 3.9,
      reviewCnt: 158,
    },
    {
      waterPlaceId: 4,
      waterPlaceName: "대천천계곡",
      waterPlaceImageUrl: "/img/dummy/계곡이미지8.jpg",
      location: "부산광역시 북구",
      rating: 3.8,
      reviewCnt: 162,
    },
  ];

  if (loading) {
    return <div>loading</div>;
  } else
    return (
      <div className={styles.mainPage}>
        <Header />
        <div className={styles.body}>
          <div className={styles.top}>
            <Weather
              nationalWeather={main.nationalWeather}
              alert={main.weatherAlert}
            />
            <Accident
              accident={regionAccident}
              setRegionAccident={setRegionAccident}
            />
          </div>
          <div>
            <PopularValley
              place={dummyValley}
              setPopularValley={setPopularValley}
            />
          </div>
          <div className={styles.recentPost}>
            <RecentPost recentLostPost={recentLostItem} />
            <RecentPost recentReviewPost={recentReview} />
          </div>
        </div>
        <Footer />
      </div>
    );
};

export default MainPage;
