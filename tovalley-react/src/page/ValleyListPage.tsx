import React, { useEffect, useState } from "react";
import Header from "../component/header/Header";
import styles from "../css/valleyList/ValleyList.module.css";
import Footer from "../component/footer/Footer";
import { BiSearchAlt2 } from "react-icons/bi";
import axios from "axios";
import PagingBtn from "../component/common/PagingBtn";

const localhost = "http://localhost:8081";

const ValleyListPage = () => {
  const [list, setList] = useState({
    content: [
      {
        waterPlaceId: 1,
        waterPlaceName: "대천천계곡",
        waterPlaceAddr: "부산광역시 북구 화명동 2102번지",
        rating: 4.7,
        reviewNum: 195,
        category: "하천",
      },

      {
        waterPlaceId: 40,
        waterPlaceName: "대천천계곡",
        waterPlaceAddr: "부산광역시 북구 화명동 2102번지",
        rating: 4.7,
        reviewNum: 195,
        category: "계곡",
      },
    ],
    pageable: {
      sort: {
        unsorted: false,
        sorted: true,
        empty: false,
      },
      pageSize: 0,
      pageNumber: 0,
      offset: 0,
      paged: true,
      unpaged: false,
    },
    last: false,
    totalPages: 13,
    totalElements: 50,
    first: false,
    numberOfElements: 4,
    size: 4,
    number: 0,
    sort: {
      unsorted: false,
      sorted: true,
      empty: false,
    },
    empty: false,
  });

  useEffect(() => {
    const config = {
      params: {
        province: "경상북도",
        city: "김천시",
        sort: "rating,desc",
        page: 0,
        size: 3,
      },
    };

    axios
      .get(`${localhost}/api/valleys/list`, config)
      .then((res) => {
        console.log(res);
      })
      .catch((err) => console.log(err));
  }, []);

  const category = [
    {
      name: "전국",
      region: [],
    },
    {
      name: "울산광역시",
      region: ["울주군"],
    },
    {
      name: "대전광역시",
      region: ["대덕구", "중구", "동구", "서구"],
    },
    {
      name: "광주광역시",
      region: ["광산구"],
    },
    {
      name: "전라남도",
      region: [
        "곡성군",
        "장성군",
        "영암군",
        "화순군",
        "장흥군",
        "담양군",
        "해남군",
        "구례군",
        "순천시",
        "광양시",
        "보성군",
        "강진군",
      ],
    },
    {
      name: "부산광역시",
      region: ["해운대구", "금정구", "북구", "기장군"],
    },
    {
      name: "경기도",
      region: ["연천군", "남양주시", "양평군", "가평군", "포천시", "남양주"],
    },
    {
      name: "전라북도",
      region: [
        "진안군",
        "고창군",
        "정읍시",
        "임실군",
        "김제시",
        "장수군",
        "완주군",
        "남원시",
        "순창군",
        "무주군",
      ],
    },
    {
      name: "경상남도",
      region: [
        "사천시",
        "김해시",
        "양산시",
        "거창군",
        "합천군",
        "창원시진해구",
        "함안군",
        "의령군",
        "창녕군",
        "마산합포구",
        "밀양시",
        "마산회원구",
        "남해군",
        "하동군",
        "고성군",
        "함양군",
        "산청군",
      ],
    },
    {
      name: "서울특별시",
      region: ["관악구"],
    },
    {
      name: "충청남도",
      region: [
        "청양군",
        "천안",
        "서산시",
        "공주시",
        "천안시",
        "논산시",
        "금산군",
      ],
    },
    {
      name: "충청북도",
      region: [
        "청주시 상당구",
        "청주시 서원구",
        "충주시",
        "제천시",
        "영동군",
        "진천군",
        "괴산군",
        "옥천군",
        "보은군",
        "음성군",
        "단양군",
      ],
    },
    {
      name: "제주특별자치도",
      region: ["제주시", "서귀포시"],
    },
    {
      name: "강원도",
      region: [
        "정선군",
        "춘천시",
        "평창군",
        "인제군",
        "양구군",
        "양양군",
        "삼척시",
        "원주시",
        "정성군",
        "강릉시",
        "화천군",
        "철원군",
        "동해시",
        "홍천군",
        "횡성군",
        "영월군",
        "고성군",
      ],
    },
    {
      name: "경상북도",
      region: [
        "영주시",
        "영천시",
        "영덕군",
        "문경시",
        "청송군",
        "봉화군",
        "김천시",
        "울진군",
        "고령군",
        "성주군",
        "포항시 북구",
        "상주시",
        "포항시",
        "청도군",
        "안동시",
        "포항시 남구",
        "의성군",
        "칠곡군",
        "경주시",
        "예천군",
        "봉화",
        "군위군",
        "영양군",
      ],
    },
    {
      name: "세종특별자치시",
      region: [],
    },
  ];

  const [click, setClick] = useState("전국");
  const [regionClick, setRegionClick] = useState("");
  const [sort, setSort] = useState("평점");

  return (
    <div>
      <Header />
      <div className={styles.body}>
        <div className={styles.category}>
          {category.map((area) => {
            return (
              <div>
                <div
                  className={styles.categoryList}
                  style={
                    click === area.name ? { backgroundColor: "#F5F5F5" } : {}
                  }
                >
                  <span onClick={() => setClick(`${area.name}`)}>
                    {area.name}
                  </span>
                </div>
                {click === area.name && (
                  <div className={styles.regionList}>
                    {area.region.map((region) => {
                      return (
                        <span
                          onClick={() => setRegionClick(`${region}`)}
                          style={
                            regionClick === region
                              ? { backgroundColor: "#E9E9E9" }
                              : {}
                          }
                        >
                          {region}
                        </span>
                      );
                    })}
                  </div>
                )}
              </div>
            );
          })}
        </div>
        <div className={styles.valleyContainer}>
          <div className={styles.top}>
            <div className={styles.sort}>
              <span
                onClick={() => setSort("평점")}
                className={sort === "평점" ? styles.clickedSort : ""}
              >
                평점
              </span>
              <span
                onClick={() => setSort("리뷰")}
                className={sort === "리뷰" ? styles.clickedSort : ""}
              >
                리뷰
              </span>
            </div>
            <div className={styles.search}>
              <input placeholder="계곡을 검색해보세요" />
              <span>
                <BiSearchAlt2 size="22px" color="#838383" />
              </span>
            </div>
          </div>
          <div className={styles.valleyList}>
            {list.content.map((item) => {
              return (
                <div className={styles.valleyItem}>
                  <img
                    src={process.env.PUBLIC_URL + "/img/계곡test이미지.png"}
                    alt="계곡 이미지"
                    width="140px"
                  />
                  <div className={styles.valleyInfo}>
                    <span className={styles.valleyName}>
                      {item.waterPlaceName}
                    </span>
                    <span className={styles.valleyRegion}>
                      {item.waterPlaceAddr}
                    </span>
                    <span>하천</span>
                    <span className={styles.valleyRating}>
                      <span>{item.rating}</span>
                      <span>/5</span>
                    </span>
                    <span className={styles.valleyReview}>
                      리뷰 {item.reviewNum}개
                    </span>
                  </div>
                </div>
              );
            })}
            {list.content.length % 3 === 1 ? (
              <>
                <div className={styles.valleyBlank} />
                <div className={styles.valleyBlank} />
              </>
            ) : list.content.length % 3 === 2 ? (
              <div className={styles.valleyBlank} />
            ) : (
              <></>
            )}
          </div>
        </div>
      </div>
      <PagingBtn totalPages={list.totalPages} />
      <Footer />
    </div>
  );
};

export default ValleyListPage;
