import React, { useEffect, useState } from "react";
import Header from "../component/header/Header";
import Footer from "../component/footer/Footer";
import ValleyInfo from "../component/valley/valleyInfo/ValleyInfo";
import ValleyQuality from "../component/valley/valleyQuality/ValleyQuality";
import ValleySchedule from "../component/valley/ValleySchedule";
import ValleyReview from "../component/valley/ValleyReview";
import axiosInstance from "../axios_interceptor";
import { useNavigate, useParams } from "react-router-dom";
import styles from "../css/valley/ValleyPage.module.css";

type valley = {
  waterPlaceWeathers: {
    weatherDate: string;
    climateIcon: string;
    climateDescription: string;
    lowestTemperature: number;
    highestTemperature: number;
    humidity: number;
    windSpeed: number;
    rainPrecipitation: number;
    clouds: number;
    dayFeelsLike: number;
  }[];
  waterPlaceDetails: {
    waterPlaceImage: string | null;
    waterPlaceName: string;
    latitude: string;
    longitude: string;
    managementType: string;
    detailAddress: string;
    town: string;
    annualVisitors: string;
    safetyMeasures: number | string;
    waterPlaceSegment: number;
    dangerSegments: number | string;
    dangerSignboardsNum: number | string;
    deepestDepth: number;
    avgDepth: number;
    waterTemperature: number;
    bod: number;
    turbidity: number;
    waterQualityReviews: {
      깨끗해요: number;
      괜찮아요: number;
      더러워요: number;
    };
  };
  rescueSupplies: {
    lifeBoatNum: number;
    portableStandNum: number;
    lifeJacketNum: number;
    lifeRingNum: number;
    rescueRopeNum: number;
    rescueRodNum: number;
  };
  accidents: {
    totalDeathCnt: number;
    totalDisappearanceCnt: number;
    totalInjuryCnt: number;
  };
  tripPlanToWaterPlace: {
    [key: string]: number;
  };
  reviewRespDto: {
    waterPlaceRating: number;
    reviewCnt: number;
    ratingRatio: {
      "1": number;
      "2": number;
      "3": number;
      "4": number;
      "5": number;
    };
    reviews: {
      content: {
        reviewId: number;
        memberProfileImg: string | null;
        nickname: string;
        rating: number;
        createdReviewDate: string;
        content: string;
        reviewImages: string[];
        waterQuality: string;
      }[];
      pageable: {
        sort: {
          empty: boolean;
          unsorted: boolean;
          sorted: boolean;
        };
        offset: number;
        pageNumber: number;
        pageSize: number;
        paged: boolean;
        unpaged: boolean;
      };
      last: boolean;
      totalPages: number;
      totalElements: number;
      first: boolean;
      sort: {
        empty: boolean;
        unsorted: boolean;
        sorted: boolean;
      };
      number: number;
      size: number;
      numberOfElements: number;
      empty: boolean;
    };
  };
};

type valleyReview = {
  waterPlaceRating: number;
  reviewCnt: number;
  ratingRatio: {
    "1": number;
    "2": number;
    "3": number;
    "4": number;
    "5": number;
  };
  reviews: {
    content: {
      reviewId: number;
      memberProfileImg: string | null;
      nickname: string;
      rating: number;
      createdReviewDate: string;
      content: string;
      reviewImages: string[];
      waterQuality: string;
    }[];
    pageable: {
      sort: {
        empty: boolean;
        unsorted: boolean;
        sorted: boolean;
      };
      offset: number;
      pageNumber: number;
      pageSize: number;
      paged: boolean;
      unpaged: boolean;
    };
    last: boolean;
    totalPages: number;
    totalElements: number;
    first: boolean;
    sort: {
      empty: boolean;
      unsorted: boolean;
      sorted: boolean;
    };
    number: number;
    size: number;
    numberOfElements: number;
    empty: boolean;
  };
};

type tripPeopleCnt = {
  tripPlanToWaterPlace: {
    [key: string]: number;
  };
};

const ValleyPage = () => {
  const [valley, setValley] = useState<valley>({
    waterPlaceWeathers: [
      {
        weatherDate: "",
        climateIcon: "",
        climateDescription: "",
        lowestTemperature: 0,
        highestTemperature: 0,
        humidity: 0,
        windSpeed: 0,
        rainPrecipitation: 0,
        clouds: 0,
        dayFeelsLike: 0,
      },
    ],
    waterPlaceDetails: {
      waterPlaceImage: null,
      waterPlaceName: "",
      latitude: "0",
      longitude: "0",
      managementType: "",
      detailAddress: "",
      town: "",
      annualVisitors: "",
      safetyMeasures: "",
      waterPlaceSegment: 0,
      dangerSegments: "",
      dangerSignboardsNum: "",
      deepestDepth: 0,
      avgDepth: 0,
      waterTemperature: 0,
      bod: 0,
      turbidity: 0,
      waterQualityReviews: {
        깨끗해요: 0,
        괜찮아요: 0,
        더러워요: 0,
      },
    },
    rescueSupplies: {
      lifeBoatNum: 0,
      portableStandNum: 0,
      lifeJacketNum: 0,
      lifeRingNum: 0,
      rescueRopeNum: 0,
      rescueRodNum: 0,
    },
    accidents: {
      totalDeathCnt: 0,
      totalDisappearanceCnt: 0,
      totalInjuryCnt: 0,
    },
    tripPlanToWaterPlace: {
      "2023-01-01": 0,
    },
    reviewRespDto: {
      waterPlaceRating: 0,
      reviewCnt: 0,
      ratingRatio: {
        "1": 0,
        "2": 0,
        "3": 0,
        "4": 0,
        "5": 0,
      },
      reviews: {
        content: [
          {
            reviewId: 0,
            memberProfileImg: null,
            nickname: "",
            rating: 0,
            createdReviewDate: "",
            content: "",
            reviewImages: [],
            waterQuality: "",
          },
          {
            reviewId: 0,
            memberProfileImg: null,
            nickname: "",
            rating: 0,
            createdReviewDate: "",
            content: "",
            reviewImages: [],
            waterQuality: "",
          },
        ],
        pageable: {
          sort: {
            empty: false,
            unsorted: false,
            sorted: false,
          },
          offset: 0,
          pageNumber: 0,
          pageSize: 0,
          paged: false,
          unpaged: false,
        },
        last: false,
        totalPages: 0,
        totalElements: 0,
        first: false,
        sort: {
          empty: false,
          unsorted: false,
          sorted: false,
        },
        number: 0,
        size: 0,
        numberOfElements: 0,
        empty: false,
      },
    },
  });

  const [valleyReview, setValleyReview] = useState<valleyReview>({
    waterPlaceRating: 0,
    reviewCnt: 0,
    ratingRatio: {
      "1": 0,
      "2": 0,
      "3": 0,
      "4": 0,
      "5": 0,
    },
    reviews: {
      content: [
        {
          reviewId: 0,
          memberProfileImg: null,
          nickname: "",
          rating: 0,
          createdReviewDate: "",
          content: "",
          reviewImages: [],
          waterQuality: "",
        },
        {
          reviewId: 0,
          memberProfileImg: null,
          nickname: "",
          rating: 0,
          createdReviewDate: "",
          content: "",
          reviewImages: [],
          waterQuality: "",
        },
      ],
      pageable: {
        sort: {
          empty: false,
          unsorted: false,
          sorted: false,
        },
        offset: 0,
        pageNumber: 0,
        pageSize: 0,
        paged: false,
        unpaged: false,
      },
      last: false,
      totalPages: 0,
      totalElements: 0,
      first: false,
      sort: {
        empty: false,
        unsorted: false,
        sorted: false,
      },
      number: 0,
      size: 0,
      numberOfElements: 0,
      empty: false,
    },
  });

  const [peopleCnt, setPeopleCnt] = useState<tripPeopleCnt>({
    tripPlanToWaterPlace: {
      "2023-01-01": 0,
    },
  });

  const [loginModal, setLoginModal] = useState(false);

  const { id } = useParams();

  useEffect(() => {
    axiosInstance
      .get(`/api/auth/water-places/${id}`)
      .then((res) => {
        console.log(res);
        setValley(res.data.data);
        setValleyReview(res.data.data.reviewRespDto);
        setPeopleCnt({
          tripPlanToWaterPlace: res.data.data.tripPlanToWaterPlace,
        });
      })
      .catch((err) => {
        console.log(err);
        err.response.status === 401 && setLoginModal(true);
      });
  }, []);

  return (
    <div>
      <Header />
      <ValleyInfo
        waterPlaceDetails={valley.waterPlaceDetails}
        weatherList={valley.waterPlaceWeathers}
        accidents={valley.accidents}
        rescueSupplies={valley.rescueSupplies}
      />
      <div style={{ padding: "0 10% 8em 10%" }}>
        <ValleyQuality waterPlaceDetails={valley.waterPlaceDetails} />
        <ValleySchedule
          tripPlanToWaterPlace={peopleCnt}
          setPeopleCnt={setPeopleCnt}
          waterPlaceName={valley.waterPlaceDetails.waterPlaceName}
          detailAddress={valley.waterPlaceDetails.detailAddress}
          annualVisitors={valley.waterPlaceDetails.annualVisitors}
        />
        <ValleyReview
          reviewRespDto={valleyReview}
          setValleyReview={setValleyReview}
        />
      </div>
      {loginModal && <LoginModal />}
      <Footer />
    </div>
  );
};

const LoginModal = () => {
  useEffect(() => {
    document.body.style.cssText = `
          position: fixed; 
          top: -${window.scrollY}px;
          overflow-y: scroll;
          width: 100%;`;
    return () => {
      const scrollY = document.body.style.top;
      document.body.style.cssText = "";
      window.scrollTo(0, parseInt(scrollY || "0", 10) * -1);
    };
  }, []);

  const navigation = useNavigate();

  return (
    <div className={styles.modalContainer}>
      <div className={styles.modalBox}>
        <div className={styles.modalContent}>
          <span>로그인이 필요합니다.</span>
        </div>
        <div className={styles.confirm} onClick={() => navigation("/login")}>
          로그인
        </div>
      </div>
    </div>
  );
};

export default ValleyPage;
