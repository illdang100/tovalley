import React, { useEffect, useState } from "react";
import Header from "../component/header/Header";
import styles from "../css/valleyList/ValleyList.module.css";
import Footer from "../component/footer/Footer";
import { BiSearchAlt2 } from "react-icons/bi";
import axios from "axios";
import { useNavigate } from "react-router-dom";

const localhost = "http://localhost:8081";

type List = {
  content: {
    waterPlaceId: number;
    waterPlaceName: string;
    waterPlaceAddr: string;
    waterPlaceRating: number | string;
    waterPlaceReviewCnt: number | string;
    managementType: string;
    waterPlaceCategory: string;
  }[];
  pageable: {
    sort: {
      unsorted: boolean;
      sorted: boolean;
      empty: boolean;
    };
    pageSize: number;
    pageNumber: number;
    offset: number;
    paged: boolean;
    unpaged: boolean;
  };
  last: boolean;
  totalPages: number;
  totalElements: number;
  first: boolean;
  numberOfElements: number;
  size: number;
  number: number;
  sort: {
    unsorted: boolean;
    sorted: boolean;
    empty: boolean;
  };
  empty: boolean;
};
const ValleyListPage = () => {
  const [click, setClick] = useState({ category: "전국", detail: false });
  const [regionClick, setRegionClick] = useState({ ko: "", en: "" });
  const [sort, setSort] = useState("평점");
  const [search, setSearch] = useState("");
  const [searchValley, setSearchValley] = useState(false);
  const navigation = useNavigate();

  const [page, setPage] = useState(1);
  const [currPage, setCurrPage] = useState(page);

  let firstNum = currPage - (currPage % 5) + 1;
  let lastNum = currPage - (currPage % 5) + 5;

  const [list, setList] = useState<List>({
    content: [
      {
        waterPlaceId: 1,
        waterPlaceName: "대천천계곡",
        waterPlaceAddr: "부산광역시 북구 화명동 2102번지",
        waterPlaceRating: "",
        waterPlaceReviewCnt: "",
        waterPlaceCategory: "",
        managementType: "일반지역",
      },
      {
        waterPlaceId: 40,
        waterPlaceName: "대천천계곡",
        waterPlaceAddr: "부산광역시 북구 화명동 2102번지",
        waterPlaceRating: 4.7,
        waterPlaceReviewCnt: 195,
        waterPlaceCategory: "계곡",
        managementType: "일반지역",
      },
      {
        waterPlaceId: 40,
        waterPlaceName: "대천천계곡",
        waterPlaceAddr: "부산광역시 북구 화명동 2102번지",
        waterPlaceRating: 4.7,
        waterPlaceReviewCnt: 195,
        waterPlaceCategory: "계곡",
        managementType: "일반지역",
      },
      {
        waterPlaceId: 40,
        waterPlaceName: "대천천계곡",
        waterPlaceAddr: "부산광역시 북구 화명동 2102번지",
        waterPlaceRating: 4.7,
        waterPlaceReviewCnt: 195,
        waterPlaceCategory: "계곡",
        managementType: "일반지역",
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
    sort === "평점"
      ? getValleyList(click.category, regionClick.en, "rating", search)
      : getValleyList(click.category, regionClick.en, "review", search);
  }, [regionClick, sort, page, searchValley]);

  const getValleyList = (
    province: string,
    city: string,
    sort: string,
    search: string
  ) => {
    console.log(province, city);
    const config =
      search === ""
        ? city === ""
          ? {
              params: {
                province: province,
                sortCond: sort,
                page: page - 1,
              },
            }
          : {
              params: {
                province: province,
                city: city,
                sortCond: sort,
                page: page - 1,
              },
            }
        : city === ""
        ? {
            params: {
              province: province,
              searchWord: search,
              sortCond: sort,
              page: page - 1,
            },
          }
        : {
            params: {
              province: province,
              city: city,
              searchWord: search,
              sortCond: sort,
              page: page - 1,
            },
          };

    console.log(config);
    axios
      .get(`${localhost}/api/water-place/list`, config)
      .then((res) => {
        console.log(res);
        setList(res.data.data);
      })
      .catch((err) => console.log(err));
  };

  const category = [
    {
      name: "울산광역시",
      region: [
        { ko: "전체", en: "" },
        { ko: "울주군", en: "ULJU_GUN" },
      ],
    },
    {
      name: "대전광역시",
      region: [
        { ko: "전체", en: "" },
        {
          ko: "대덕구",
          en: "DAEDEOK_GU",
        },
        {
          ko: "중구",
          en: "JUNG_GU",
        },
        { ko: "동구", en: "JUNG_GU" },
        { ko: "서구", en: "SEO_GU" },
      ],
    },
    {
      name: "광주광역시",
      region: [
        { ko: "전체", en: "" },
        { ko: "광산구", en: "GWANGSAN_GU" },
      ],
    },
    {
      name: "전라남도",
      region: [
        { ko: "전체", en: "" },
        { ko: "곡성군", en: "GOKSEONG_GUN" },
        { ko: "장성군", en: "JANGSEONG_GUN" },
        { ko: "영암군", en: "YEONGAM_GUN" },
        { ko: "화순군", en: "HWASUN_GUN" },
        { ko: "장흥군", en: "JANGHEUNG_GUN" },
        { ko: "담양군", en: "DAMYANG_GUN" },
        { ko: "해남군", en: "HAENAM_GUN" },
        { ko: "구례군", en: "GURYE_GUN" },
        { ko: "순천시", en: "SUNCHEON_SI" },
        { ko: "광양시", en: "GWANGYANG_SI" },
        { ko: "보성군", en: "BOSEONG_GUN" },
        { ko: "강진군", en: "GANGJIN_GUN" },
      ],
    },
    {
      name: "부산광역시",
      region: [
        { ko: "전체", en: "" },
        { ko: "해운대구", en: "HAEUNDAE_GU" },
        { ko: "금정구", en: "GEUMJEONG_GU" },
        { ko: "북구", en: "BUK_GU" },
        { ko: "기장군", en: "GIJANG_GUN" },
      ],
    },
    {
      name: "경기도",
      region: [
        { ko: "전체", en: "" },
        { ko: "연천군", en: "YEONCHEON_GUN" },
        { ko: "남양주시", en: "NAMYANGJU" },
        { ko: "양평군", en: "YANGPYEONG_GUN" },
        { ko: "가평군", en: "GAPYEONG_GUN" },
        { ko: "포천시", en: "POCHEON_SI" },
      ],
    },
    {
      name: "전라북도",
      region: [
        { ko: "전체", en: "" },
        { ko: "진안군", en: "JINAN_GUN" },
        { ko: "고창군", en: "GOCHANG_GUN" },
        { ko: "정읍시", en: "JEONGEUP_SI" },
        { ko: "임실군", en: "IMSIL_GUN" },
        { ko: "김제시", en: "GIMJE_SI" },
        { ko: "장수군", en: "JANGSU_GUN" },
        { ko: "완주군", en: "WANJU_GUN" },
        { ko: "남원시", en: "NAMWON_SI" },
        { ko: "순창군", en: "SUNCHANG_GUN" },
        { ko: "무주군", en: "MUJU_GUN" },
      ],
    },
    {
      name: "경상남도",
      region: [
        { ko: "전체", en: "" },
        { ko: "사천시", en: "SACHEON_SI" },
        { ko: "김해시", en: "GIMHAE_SI" },
        { ko: "양산시", en: "YANGSAN_SI" },
        { ko: "거창군", en: "GEOCHANG_GUN" },
        { ko: "합천군", en: "HAPCHEON_GUN" },
        { ko: "창원시", en: "CHANGWON_SI" },
        { ko: "진해구", en: "JINHAE_GU" },
        { ko: "함안군", en: "HAMAN_GUN" },
        { ko: "의령군", en: "UIRYEONG_GUN" },
        { ko: "창녕군", en: "CHANGNYEONG_GUN" },
        { ko: "마산합포구", en: "MASAN_HAPPO_GU" },
        { ko: "밀양시", en: "MIRYANG_SI" },
        { ko: "마산회원구", en: "MASAN_HOEWON_GU" },
        { ko: "남해군", en: "NAMHAE_GUN" },
        { ko: "하동군", en: "HADONG_GUN" },
        { ko: "고성군", en: "GOSEONG_GUN" },
        { ko: "함양군", en: "HAMYANG_GUN" },
        { ko: "산청군", en: "SANCHEONG_GUN" },
      ],
    },
    {
      name: "서울특별시",
      region: [
        { ko: "전체", en: "" },
        { ko: "관악구", en: "GWANAK_GU" },
      ],
    },
    {
      name: "충청남도",
      region: [
        { ko: "전체", en: "" },
        { ko: "청양군", en: "CHEONGYANG_GUN" },
        { ko: "천안시", en: "CHUNAN_SI" },
        { ko: "서산시", en: "SEOSAN_SI" },
        { ko: "공주시", en: "GONGJU_SI" },
        { ko: "논산시", en: "NONSAN_SI" },
        { ko: "금산군", en: "GEUMSAN_GUN" },
      ],
    },
    {
      name: "충청북도",
      region: [
        { ko: "전체", en: "" },
        { ko: "청주시", en: "CHEONGJU_SI" },
        { ko: "상당구", en: "SANGDANG_GU" },
        { ko: "충주시", en: "CHUNGJU_SI" },
        { ko: "제천시", en: "JECHON_SI" },
        { ko: "영동군", en: "YEONGDONG_GUN" },
        { ko: "진천군", en: "JINCHEON_GUN" },
        { ko: "괴산군", en: "GOESAN_GUN" },
        { ko: "옥천군", en: "OKCHEON_GUN" },
        { ko: "보은군", en: "BOEUN_GUN" },
        { ko: "음성군", en: "EUMSEONG_GUN" },
        { ko: "단양군", en: "DANYANG_GUN" },
      ],
    },
    {
      name: "제주특별자치도",
      region: [
        { ko: "전체", en: "" },
        { ko: "제주시", en: "JEJU_SI" },
        { ko: "서귀포시", en: "SEOGWIPO_SI" },
      ],
    },
    {
      name: "강원도",
      region: [
        { ko: "전체", en: "" },
        { ko: "정선군", en: "JEONGSEON_GUN" },
        { ko: "춘천시", en: "CHUNCHEON_SI" },
        { ko: "평창군", en: "PYEONGCHANG_GUN" },
        { ko: "인제군", en: "INJE_GUN" },
        { ko: "양구군", en: "YANGGU_GUN" },
        { ko: "양양군", en: "YANGYANG_GUN" },
        { ko: "삼척시", en: "SAMCHEOK_SI" },
        { ko: "원주시", en: "WONJU_SI" },
        { ko: "강릉시", en: "GANGNEUNG_SI" },
        { ko: "화천군", en: "HWACHEON_GUN" },
        { ko: "철원군", en: "CHEOLWON_GUN" },
        { ko: "동해시", en: "DONGHAE_SI" },
        { ko: "홍천군", en: "HONGCHEON_GUN" },
        { ko: "횡성군", en: "HOENGSEONG_GUN" },
        { ko: "영월군", en: "YEONGWOL_GUN" },
      ],
    },
    {
      name: "경상북도",
      region: [
        { ko: "전체", en: "" },
        { ko: "영주시", en: "YEONGJU_SI" },
        { ko: "영천시", en: "YEONGCHEON_SI" },
        { ko: "영덕군", en: "YEONGDEOK_GUN" },
        { ko: "문경시", en: "MUNGYEONG_SI" },
        { ko: "청송군", en: "CHEONGSONG_GUN" },
        { ko: "봉화군", en: "BONGHWA_GUN" },
        { ko: "김천시", en: "GIMCHEON_SI" },
        { ko: "울진군", en: "ULJIN_GUN" },
        { ko: "고령군", en: "GORYEONG_GUN" },
        { ko: "성주군", en: "SEONGJU_GUN" },
        { ko: "포항시", en: "POHANG_SI" },
        { ko: "상주시", en: "SANGJU_SI" },
        { ko: "청도군", en: "CHEONGDO_GUN" },
        { ko: "안동시", en: "ANDONG_SI" },
        { ko: "의성군", en: "UISEONG_GUN" },
        { ko: "칠곡군", en: "CHILGOK_GUN" },
        { ko: "경주시", en: "GYEONGJU_SI" },
        { ko: "예천군", en: "YECHON_GUN" },
        { ko: "군위군", en: "GUNWI_GUN" },
        { ko: "영양군", en: "YEONGYANG_GUN" },
      ],
    },
    {
      name: "세종특별자치시",
      region: [
        { ko: "전체", en: "" },
        { ko: "세종시", en: "SEJONG_SI" },
      ],
    },
  ];

  return (
    <div>
      <Header />
      <div className={styles.body}>
        <div className={styles.category}>
          <div
            className={styles.categoryList}
            style={
              click.category === "전국" ? { backgroundColor: "#F5F5F5" } : {}
            }
          >
            <span
              onClick={() => {
                setClick({
                  category: "전국",
                  detail: false,
                });
                setRegionClick({ ko: "", en: "" });
              }}
            >
              전국
            </span>
          </div>
          {category.map((area) => {
            return (
              <div>
                <div
                  className={styles.categoryList}
                  style={
                    click.category === area.name
                      ? { backgroundColor: "#F5F5F5" }
                      : {}
                  }
                >
                  <span
                    onClick={() => {
                      setClick({
                        category: `${area.name}`,
                        detail:
                          area.name === click.category ? !click.detail : true,
                      });
                      setRegionClick(area.region[0]);
                    }}
                  >
                    {area.name}
                  </span>
                </div>
                {click.category === area.name && click.detail && (
                  <div className={styles.regionList}>
                    {area.region.map((region) => {
                      return (
                        <span
                          onClick={() => {
                            setRegionClick(region);
                          }}
                          style={
                            regionClick.ko === region.ko
                              ? { backgroundColor: "#E9E9E9" }
                              : {}
                          }
                        >
                          {region.ko}
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
              <input
                value={search}
                placeholder="계곡을 검색해보세요"
                onChange={(e) => {
                  setSearch(e.target.value);
                }}
                onKeyDown={(e) => {
                  if (e.key === "Enter") {
                    setSearchValley((prev) => !prev);
                  }
                }}
              />
              <span onClick={() => setSearchValley((prev) => !prev)}>
                <BiSearchAlt2 size="22px" color="#838383" />
              </span>
            </div>
          </div>
          <div className={styles.valleyList}>
            {list.content.map((item) => {
              return (
                <div
                  className={styles.valleyItem}
                  onClick={() => navigation(`/valley/${item.waterPlaceId}`)}
                >
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
                    <div className={styles.valleyType}>
                      {item.waterPlaceCategory !== "" && (
                        <span className={styles.valleyCategory}>
                          {item.waterPlaceCategory}
                        </span>
                      )}
                      {item.managementType !== "" && (
                        <span className={styles.valleyCategory}>
                          {item.managementType}
                        </span>
                      )}
                    </div>
                    <div className={styles.reviewContainer}>
                      <span className={styles.valleyRating}>
                        <span>
                          {item.waterPlaceRating === ""
                            ? 0
                            : item.waterPlaceRating}
                        </span>
                        <span>/5</span>
                      </span>
                      <span className={styles.valleyReview}>
                        리뷰{" "}
                        {item.waterPlaceReviewCnt === ""
                          ? 0
                          : item.waterPlaceReviewCnt}
                        개
                      </span>
                    </div>
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
          <div className={styles.paging}>
            <button
              onClick={() => {
                setPage(page - 1);
                setCurrPage(page - 2);
              }}
              disabled={page === 1}
            >
              &lt;
            </button>
            <button
              onClick={() => setPage(firstNum)}
              aria-current={page === firstNum ? "page" : undefined}
            >
              {firstNum}
            </button>
            {Array(4)
              .fill(0)
              .map((_, i) => {
                if (firstNum + 1 + i > list.totalPages) {
                  return null;
                } else {
                  if (i <= 2) {
                    return (
                      <button
                        key={i + 1}
                        onClick={() => {
                          setPage(firstNum + 1 + i);
                        }}
                        aria-current={
                          page === firstNum + 1 + i ? "page" : undefined
                        }
                      >
                        {firstNum + 1 + i}
                      </button>
                    );
                  } else if (i >= 3) {
                    return (
                      <button
                        key={i + 1}
                        onClick={() => setPage(lastNum)}
                        aria-current={page === lastNum ? "page" : undefined}
                      >
                        {lastNum}
                      </button>
                    );
                  }
                }
              })}
            <button
              onClick={() => {
                setPage(page + 1);
                setCurrPage(page);
              }}
              disabled={page === list.totalPages}
            >
              &gt;
            </button>
          </div>
        </div>
      </div>

      <Footer />
    </div>
  );
};

export default ValleyListPage;
