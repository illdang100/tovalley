import React, { FC, useEffect, useState } from "react";
import styles from "../../css/valley/ValleyReview.module.css";
import { MdOutlineChatBubble, MdImage } from "react-icons/md";
import { FaUserCircle } from "react-icons/fa";
import RatingStar from "../common/RatingStar";
import axiosInstance from "../../axios_interceptor";
import { useParams } from "react-router-dom";
import DetailReviewImg from "./DetailReviewImg";
import useDidMountEffect from "../../useDidMountEffect";

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

interface Props {
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
  setValleyReview: React.Dispatch<React.SetStateAction<valleyReview>>;
}

const ValleyReview: FC<Props> = ({ reviewRespDto, setValleyReview }) => {
  const [sort, setSort] = useState("최신순");
  const sortMenu = ["최신순", "평점 높은 순", "평점 낮은 순"];
  const [page, setPage] = useState(1);
  const [currPage, setCurrPage] = useState(page);
  const [detailReview, setDetailReview] = useState<{
    view: boolean;
    images: string[];
  }>({ view: false, images: [] });
  const [innerWidth, setInnerWidth] = useState(window.innerWidth);

  useEffect(() => {
    const resizeListener = () => {
      setInnerWidth(window.innerWidth);
    };
    window.addEventListener("resize", resizeListener);
  });

  let firstNum = currPage - (currPage % 5) + 1;
  let lastNum = currPage - (currPage % 5) + 5;

  const { id } = useParams();

  useDidMountEffect(() => {
    const config =
      sort === "최신순"
        ? {
            params: {
              sort: "createdDate,desc",
              page: page - 1,
              size: 5,
            },
          }
        : sort === "평점 높은 순"
        ? {
            params: {
              sort: "rating,desc",
              page: page - 1,
              size: 5,
            },
          }
        : {
            params: {
              sort: "rating,asc",
              page: page - 1,
              size: 5,
            },
          };

    console.log(config);

    axiosInstance
      .get(`/api/auth/water-places/${id}/reviews`, config)
      .then((res) => {
        console.log(res);
        setValleyReview(res.data.data);
      })
      .catch((err) => console.log(err));
  }, [sort, page]);

  return (
    <div className={styles.valleyReview}>
      <span>계곡 평점 및 리뷰</span>
      <div className={styles.reviewStatistics}>
        <div className={styles.totalRating}>
          <span>총 평점</span>
          <div className={styles.ratingStar}>
            <span>
              <RatingStar
                rating={reviewRespDto.waterPlaceRating}
                size={
                  innerWidth <= 730
                    ? "20px"
                    : innerWidth <= 880
                    ? "25px"
                    : "30px"
                }
              />
            </span>
          </div>
          <div className={styles.ratingNum}>
            <span>{reviewRespDto.waterPlaceRating}</span>
            <span> / </span>
            <span>5</span>
          </div>
        </div>
        <div className={styles.totalReview}>
          <span>전체 리뷰 수</span>
          <span>
            <MdOutlineChatBubble
              size={
                innerWidth <= 730 ? "40px" : innerWidth <= 880 ? "50px" : "70px"
              }
              color="#999999"
            />
          </span>
          <span>{reviewRespDto.reviewCnt}개</span>
        </div>
        <div className={styles.totalRatio}>
          <span>평점 비율</span>
          <div>
            <div className={styles.ratioItem}>
              <span>5점</span>
              <div>
                <div
                  style={
                    reviewRespDto.reviewCnt !== 0 ||
                    reviewRespDto.ratingRatio[5] !== 0
                      ? {
                          width: `calc(${reviewRespDto.ratingRatio[5]}/${reviewRespDto.reviewCnt}*100%)`,
                        }
                      : {
                          width: "0",
                        }
                  }
                >
                  <></>
                </div>
              </div>
              <span>{reviewRespDto.ratingRatio[5]}</span>
            </div>
            <div className={styles.ratioItem}>
              <span>4점</span>
              <div>
                <div
                  style={
                    reviewRespDto.reviewCnt !== 0 ||
                    reviewRespDto.ratingRatio[4] !== 0
                      ? {
                          width: `calc(${reviewRespDto.ratingRatio[4]}/${reviewRespDto.reviewCnt}*100%)`,
                        }
                      : {
                          width: "0",
                        }
                  }
                >
                  <></>
                </div>
              </div>
              <span>{reviewRespDto.ratingRatio[4]}</span>
            </div>
            <div className={styles.ratioItem}>
              <span>3점</span>
              <div>
                <div
                  style={
                    reviewRespDto.reviewCnt !== 0 ||
                    reviewRespDto.ratingRatio[3] !== 0
                      ? {
                          width: `calc(${reviewRespDto.ratingRatio[3]}/${reviewRespDto.reviewCnt}*100%)`,
                        }
                      : {
                          width: "0",
                        }
                  }
                >
                  <></>
                </div>
              </div>
              <span>{reviewRespDto.ratingRatio[3]}</span>
            </div>
            <div className={styles.ratioItem}>
              <span>2점</span>
              <div>
                <div
                  style={
                    reviewRespDto.reviewCnt !== 0 ||
                    reviewRespDto.ratingRatio[2] !== 0
                      ? {
                          width: `calc(${reviewRespDto.ratingRatio[2]}/${reviewRespDto.reviewCnt}*100%)`,
                        }
                      : {
                          width: "0",
                        }
                  }
                >
                  <></>
                </div>
              </div>
              <span>{reviewRespDto.ratingRatio[2]}</span>
            </div>
            <div className={styles.ratioItem}>
              <span>1점</span>
              <div>
                <div
                  style={
                    reviewRespDto.reviewCnt !== 0 ||
                    reviewRespDto.ratingRatio[1] !== 0
                      ? {
                          width: `calc(${reviewRespDto.ratingRatio[1]}/${reviewRespDto.reviewCnt}*100%)`,
                        }
                      : {
                          width: "0",
                        }
                  }
                >
                  <></>
                </div>
              </div>
              <span>{reviewRespDto.ratingRatio[1]}</span>
            </div>
          </div>
        </div>
      </div>
      <div className={styles.reviewList}>
        <div className={styles.reviewSort}>
          {sortMenu.map((item) => {
            return (
              <div key={item}>
                <span
                  onClick={() => setSort(item)}
                  style={
                    sort === item
                      ? { fontWeight: "bold", color: "#353535" }
                      : {}
                  }
                >
                  {item}
                </span>
              </div>
            );
          })}
        </div>
        <div className={styles.reviewContainer}>
          {reviewRespDto.reviews.content.map((item) => {
            return (
              <div key={item.reviewId} className={styles.reviewItem}>
                <div
                  className={styles.valleyImg}
                  onClick={() =>
                    item.reviewImages?.length !== 0 &&
                    setDetailReview({ view: true, images: item.reviewImages })
                  }
                  style={
                    item.reviewImages?.length === 0 ? {} : { cursor: "pointer" }
                  }
                >
                  <img
                    src={
                      item.reviewImages?.length === 0
                        ? process.env.PUBLIC_URL + "/img/default-image.png"
                        : `${item.reviewImages[0]}`
                    }
                    alt="계곡 이미지"
                  />
                  {item.reviewImages.length > 1 && (
                    <span>{item.reviewImages.length}</span>
                  )}
                </div>
                <div className={styles.reviewDetail}>
                  <div className={styles.reviewInfo}>
                    <div className={styles.userInfo}>
                      {item.memberProfileImg === null ? (
                        <span>
                          <FaUserCircle
                            size={innerWidth <= 730 ? "25px" : "30px"}
                            color="#B7B7B7"
                          />
                        </span>
                      ) : (
                        <div>
                          <img src={item.memberProfileImg} alt="profileImg" />
                        </div>
                      )}
                      <span>{item.nickname}</span>
                    </div>
                    <span>{item.createdReviewDate}</span>
                  </div>
                  <div className={styles.reviewItemRating}>
                    <span>
                      <RatingStar
                        rating={item.rating}
                        size={innerWidth <= 730 ? "16px" : "20px"}
                      />
                    </span>
                    <span>{item.rating}</span>
                    <span>{item.waterQuality}</span>
                    {item.reviewImages?.length !== 0 && (
                      <span
                        onClick={() =>
                          item.reviewImages?.length !== 0 &&
                          setDetailReview({
                            view: true,
                            images: item.reviewImages,
                          })
                        }
                      >
                        <MdImage
                          color="#696969"
                          size={innerWidth <= 730 ? "20px" : "28px"}
                        />
                      </span>
                    )}
                  </div>
                  <span>{item.content}</span>
                </div>
              </div>
            );
          })}
        </div>
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
            if (firstNum + 1 + i > reviewRespDto.reviews.totalPages) {
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
          disabled={page === reviewRespDto.reviews.totalPages}
        >
          &gt;
        </button>
      </div>
      {detailReview.view && (
        <DetailReviewImg
          detailReview={detailReview}
          setDetailReview={setDetailReview}
        />
      )}
    </div>
  );
};

export default ValleyReview;
