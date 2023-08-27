import React, { FC } from "react";
import styles from "../../css/valley/ValleyReview.module.css";
import {
  MdOutlineStar,
  MdOutlineStarOutline,
  MdOutlineChatBubble,
} from "react-icons/md";
import { FaUserCircle } from "react-icons/fa";

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
}

const ValleyReview: FC<Props> = ({ reviewRespDto }) => {
  return (
    <div className={styles.valleyReview}>
      <span>계곡 평점 및 리뷰</span>
      <div className={styles.reviewStatistics}>
        <div className={styles.totalRating}>
          <span>총 평점</span>
          <div className={styles.ratingStar}>
            <span>
              <MdOutlineStar size="30px" color="#66A5FC" />
            </span>
            <span>
              <MdOutlineStar size="30px" color="#66A5FC" />
            </span>
            <span>
              <MdOutlineStar size="30px" color="#66A5FC" />
            </span>
            <span>
              <MdOutlineStar size="30px" color="#66A5FC" />
            </span>
            <span>
              <MdOutlineStarOutline size="30px" color="#66A5FC" />
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
            <MdOutlineChatBubble size="70px" color="#999999" />
          </span>
          <span>{reviewRespDto.reviewCnt}개</span>
        </div>
        <div className={styles.totalRatio}>
          <span>평점 비율</span>
          <div>
            <div className={styles.ratioItem}>
              <span>5점</span>
              <div>
                <div>
                  <></>
                </div>
              </div>
              <span>100</span>
            </div>
            <div className={styles.ratioItem}>
              <span>4점</span>
              <div>
                <div>
                  <></>
                </div>
              </div>
              <span>60</span>
            </div>
            <div className={styles.ratioItem}>
              <span>3점</span>
              <div>
                <div>
                  <></>
                </div>
              </div>
              <span>10</span>
            </div>
            <div className={styles.ratioItem}>
              <span>2점</span>
              <div>
                <div>
                  <></>
                </div>
              </div>
              <span>15</span>
            </div>
            <div className={styles.ratioItem}>
              <span>1점</span>
              <div>
                <div>
                  <></>
                </div>
              </div>
              <span>30</span>
            </div>
          </div>
        </div>
      </div>
      <div className={styles.reviewList}>
        <div className={styles.reviewSort}>
          <div>
            <span>최신순</span>
          </div>
          <div>
            <span>평점 높은 순</span>
          </div>
          <div>
            <span>평점 낮은 순</span>
          </div>
        </div>
        <div className={styles.reviewContainer}>
          {reviewRespDto.reviews.content.map((item) => {
            return (
              <div className={styles.reviewItem}>
                <div className={styles.valleyImg}>
                  <img
                    src={process.env.PUBLIC_URL + "/img/계곡test이미지.png"}
                    alt="계곡 이미지"
                    width="140px"
                  />
                </div>
                <div className={styles.reviewDetail}>
                  <div className={styles.reviewInfo}>
                    <div className={styles.userInfo}>
                      <span>
                        <FaUserCircle size="30px" color="#B7B7B7" />
                      </span>
                      <span>{item.nickname}</span>
                    </div>
                    <span>{item.createdReviewDate}</span>
                  </div>
                  <div className={styles.reviewItemRating}>
                    {[0, 0, 0, 0, 0].map(() => (
                      <span>
                        <MdOutlineStar size="20px" color="#66A5FC" />
                      </span>
                    ))}
                    <span>{item.rating}</span>
                  </div>
                  <span>{item.content}</span>
                </div>
              </div>
            );
          })}
        </div>
      </div>
    </div>
  );
};

export default ValleyReview;
