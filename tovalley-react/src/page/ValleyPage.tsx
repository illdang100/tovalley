import React, { useEffect, useState } from "react";
import Header from "../component/header/Header";
import Footer from "../component/footer/Footer";
import ValleyInfo from "../component/valley/valleyInfo/ValleyInfo";
import ValleyQuality from "../component/valley/valleyQuality/ValleyQuality";
import ValleySchedule from "../component/valley/ValleySchedule";
import ValleyReview from "../component/valley/ValleyReview";
import axiosInstance from "../axios_interceptor";
import { useParams } from "react-router-dom";

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
    latitude: number;
    longitude: number;
    managementType: string;
    detailAddress: string;
    town: string;
    annualVisitors: number;
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
      latitude: 0,
      longitude: 0,
      managementType: "",
      detailAddress: "",
      town: "",
      annualVisitors: 0,
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
      "2023-08-31": 0,
      "2023-08-30": 0,
      "2023-08-02": 0,
      "2023-08-01": 0,
      "2023-08-07": 0,
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
          },
          {
            reviewId: 0,
            memberProfileImg: null,
            nickname: "",
            rating: 0,
            createdReviewDate: "",
            content: "",
            reviewImages: [],
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
        },
        {
          reviewId: 0,
          memberProfileImg: null,
          nickname: "",
          rating: 0,
          createdReviewDate: "",
          content: "",
          reviewImages: [],
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

  const { id } = useParams();

  useEffect(() => {
    axiosInstance
      .get(`/api/auth/water-places/${id}`)
      .then((res) => {
        console.log(res);
        setValley(res.data.data);
        setValleyReview(res.data.data.reviewRespDto);
      })
      .catch((err) => console.log(err));

    setValley({
      waterPlaceWeathers: [
        // 물놀이 장소 날씨 리스트(5일)
        {
          weatherDate: "2023-08-27", // 날짜
          climateIcon: "10d", // 날씨 아이콘 Id
          climateDescription: "실 비", // 날씨 상세설명
          lowestTemperature: 24.08, // 일일 최저 온도
          highestTemperature: 26.29, // 일일 최고 온도
          humidity: 85, // 습도
          windSpeed: 3.56, // 풍속
          rainPrecipitation: 0.24, // 강수량
          clouds: 73, // 흐림 정도
          dayFeelsLike: 25.28, // 주간 체감 온도
        },
        {
          weatherDate: "2023-08-28",
          climateIcon: "03d",
          climateDescription: "구름조금",
          lowestTemperature: 25.53,
          highestTemperature: 27.82,
          humidity: 80,
          windSpeed: 1.58,
          rainPrecipitation: 0.0,
          clouds: 37,
          dayFeelsLike: 26.38,
        },
        {
          weatherDate: "2023-08-29",
          climateIcon: "03d",
          climateDescription: "구름조금",
          lowestTemperature: 25.53,
          highestTemperature: 27.82,
          humidity: 80,
          windSpeed: 1.58,
          rainPrecipitation: 0.0,
          clouds: 37,
          dayFeelsLike: 26.38,
        },
        {
          weatherDate: "2023-08-30",
          climateIcon: "03d",
          climateDescription: "구름조금",
          lowestTemperature: 25.53,
          highestTemperature: 27.82,
          humidity: 80,
          windSpeed: 1.58,
          rainPrecipitation: 0.0,
          clouds: 37,
          dayFeelsLike: 26.38,
        },
        {
          weatherDate: "2023-08-31",
          climateIcon: "03d",
          climateDescription: "구름조금",
          lowestTemperature: 25.53,
          highestTemperature: 27.82,
          humidity: 80,
          windSpeed: 1.58,
          rainPrecipitation: 0.0,
          clouds: 37,
          dayFeelsLike: 26.38,
        },
      ],
      waterPlaceDetails: {
        waterPlaceImage: null, // 물놀이 장소 이미지 Url
        waterPlaceName: "명곡저수지 상류계곡", // 물놀이 장소 명칭
        latitude: 35.42507209841541, // 위도
        longitude: 129.19863150208593, // 경도
        managementType: "일반지역", // 관리유형(일반지역, 중점관리지역)
        detailAddress: "경상남도 양산시 서창동 명동 산20-1 명곡저수지 상류계곡", // 주소 + 세부지명(세부지명은 null, 즉 빈문자열일 수 있음)
        town: "서창동", // 읍면 (null이면 빈문자열)
        annualVisitors: 0.5, // 연평균 총 이용객 수(천명)
        safetyMeasures: "", // 안전조치 사항(null일 경우 빈문자열)
        waterPlaceSegment: 320, // 물놀이구간(m)
        dangerSegments: "", // 위험구역구간(null일 경우 빈문자열)
        dangerSignboardsNum: "", // 위험구역 설정 안내표지판(합계, null일 경우 빈문자열)
        deepestDepth: 6, // 수심(깊은곳)
        avgDepth: 3, // 평균 수심
        waterTemperature: 27.9, // 계곡 수온(°C)
        bod: 3.8, // BOD(mg/L)
        turbidity: 7.9, // 탁도(NTU)
        waterQualityReviews: {
          // 사용자가 평가한 계곡 수질
          깨끗해요: 3,
          괜찮아요: 1,
          더러워요: 2,
        },
      },
      rescueSupplies: {
        // 구조용품 현황
        lifeBoatNum: 5, // 인명구조함(null일 경우 -1)
        portableStandNum: 0, // 이동식거치대(null일 경우 -1)
        lifeJacketNum: 5, // 구명조끼(null일 경우 -1)
        lifeRingNum: 5, // 구명환(null일 경우 -1)
        rescueRopeNum: 5, // 구명로프(null일 경우 -1)
        rescueRodNum: 0, // 구조봉(null일 경우 -1)
      },
      accidents: {
        // 최근 5년간 사건사고 수
        totalDeathCnt: 200,
        totalDisappearanceCnt: 66,
        totalInjuryCnt: 4,
      },
      tripPlanToWaterPlace: {
        // 해당 계곡의 여행 계획자 수
        "2023-08-31": 0, // 날짜 : 인원수
        "2023-08-30": 0,
        "2023-08-02": 0,
        "2023-08-01": 9,
        "2023-08-07": 16,
      },
      reviewRespDto: {
        waterPlaceRating: 3.2, // 물놀이 장소 평점
        reviewCnt: 6, // 물놀이 장소 리뷰수
        ratingRatio: {
          // 평점 비율
          "1": 1, // 평점: 인원수
          "2": 1,
          "3": 1,
          "4": 2,
          "5": 1,
        },
        reviews: {
          // 해당 계곡의 리뷰(페이징)
          content: [
            // 리뷰 리스트 (5개)
            {
              reviewId: 6, // 리뷰 아이디
              memberProfileImg: null, // 작성자 프로필 이미지
              nickname: "member5", // 작성자 닉네임
              rating: 5, // 작성자가 작성한 평점
              createdReviewDate: "2023-08-16 14:43:23", // 작성 시간
              content: "content5", // 리뷰 작성 내용
              reviewImages: [
                // 리뷰 이미지 Url 리스트
                "storeFileUrl1",
                "storeFileUrl2",
                "storeFileUrl3",
                "storeFileUrl4",
                "storeFileUrl5",
                "storeFileUrl6",
              ],
            },
            {
              reviewId: 2,
              memberProfileImg: null,
              nickname: "member3",
              rating: 3,
              createdReviewDate: "2023-08-16 14:43:23",
              content: "content3",
              reviewImages: [],
            },
          ],
          pageable: {
            sort: {
              empty: false,
              unsorted: false,
              sorted: true,
            },
            offset: 0,
            pageNumber: 0,
            pageSize: 5,
            paged: true,
            unpaged: false,
          },
          last: false,
          totalPages: 10,
          totalElements: 6,
          first: true,
          sort: {
            empty: false,
            unsorted: false,
            sorted: true,
          },
          number: 0,
          size: 5,
          numberOfElements: 5,
          empty: false,
        },
      },
    });

    setValleyReview({
      waterPlaceRating: 3.4,
      reviewCnt: 6,
      ratingRatio: {
        "1": 1,
        "2": 1,
        "3": 1,
        "4": 2,
        "5": 1,
      },
      reviews: {
        content: [
          {
            reviewId: 6,
            memberProfileImg: null,
            nickname: "member5",
            rating: 5,
            createdReviewDate: "2023-08-16 15:22:16",
            content: "content5",
            reviewImages: [
              "https://file.thisisgame.com/upload/nboard/news/2015/07/24/20150724161608_6058.jpg",
              "https://m.m-peak.com/file_data/design0011/2020/09/14/76c5d7968683b6477ac009c3fc1bba66.png",
            ],
          },
          {
            reviewId: 1,
            memberProfileImg: null,
            nickname: "member2",
            rating: 2,
            createdReviewDate: "2023-08-16 15:22:16",
            content: "content2",
            reviewImages: [],
          },
        ],
        pageable: {
          sort: {
            empty: false,
            unsorted: false,
            sorted: true,
          },
          offset: 0,
          pageNumber: 0,
          pageSize: 5,
          paged: true,
          unpaged: false,
        },
        last: false,
        totalPages: 2,
        totalElements: 6,
        number: 0,
        first: true,
        sort: {
          empty: false,
          unsorted: false,
          sorted: true,
        },
        size: 5,
        numberOfElements: 5,
        empty: false,
      },
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
        <ValleySchedule tripPlanToWaterPlace={valley.tripPlanToWaterPlace} />
        <ValleyReview
          reviewRespDto={valleyReview}
          setValleyReview={setValleyReview}
        />
      </div>
      <Footer />
    </div>
  );
};

export default ValleyPage;
