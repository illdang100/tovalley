import React, { FC, useEffect, useRef, useState } from "react";
import styles from "../../css/main/PopularValley.module.css";
import axios from "axios";
import { useNavigate } from "react-router-dom";

interface Props {
  place: {
    waterPlaceId: number;
    waterPlaceName: string;
    waterPlaceImage: string;
    location: string;
    rating: number;
    reviewCnt: number;
  }[];
  setPopularValley: React.Dispatch<
    React.SetStateAction<
      {
        waterPlaceId: number;
        waterPlaceName: string;
        waterPlaceImage: string;
        location: string;
        rating: number;
        reviewCnt: number;
      }[]
    >
  >;
}

const localhost = "http://13.125.136.237";

const PopularValley: FC<Props> = ({ place, setPopularValley }) => {
  const [clicked, setClicked] = useState("평점");
  const navigation = useNavigate();

  const scroll = useRef<HTMLDivElement>(null);
  const scrollPrev = useRef<HTMLDivElement>(null);

  const onMove = () => {
    scroll.current?.scrollIntoView({ behavior: "smooth", block: "nearest" });
  };

  const onMovePrev = () => {
    scrollPrev.current?.scrollIntoView({
      behavior: "smooth",
      block: "nearest",
    });
  };

  // useEffect(() => {
  //   setInterval(function () {
  //     onMove();
  //   }, 6000);

  //   setInterval(function () {
  //     onMovePrev();
  //   }, 6000);
  // }, []);

  const getPopluarValley = (cond: string) => {
    const config = {
      params: {
        cond: cond,
      },
    };

    axios
      .get(`${localhost}/api/main-page/popular-water-places`, config)
      .then((res) => {
        console.log(res);
        setPopularValley(res.data.data);
      })
      .catch((err) => console.log(err));
  };

  return (
    <div className={styles.popularValley}>
      <h4>전국 인기 계곡</h4>
      <div className={styles.category}>
        <span
          onClick={() => {
            setClicked("평점");
            getPopluarValley("RATING");
          }}
          className={
            clicked === "평점" ? styles.clickedCategory : styles.categoryBtn
          }
        >
          평점
        </span>
        <span
          onClick={() => {
            setClicked("리뷰");
            getPopluarValley("REVIEW");
          }}
          className={
            clicked === "리뷰" ? styles.clickedCategory : styles.categoryBtn
          }
        >
          리뷰
        </span>
      </div>
      <div className={styles.popularList}>
        {place?.map((item, index) => {
          return (
            <div
              ref={
                index === 0
                  ? scrollPrev
                  : index === place.length - 1
                  ? scroll
                  : null
              }
              className={styles.popularItem}
              onClick={() => {
                navigation(`/valley/${item.waterPlaceId}`);
              }}
            >
              <span>{index + 1}</span>
              <img
                src={process.env.PUBLIC_URL + "/img/계곡test이미지.png"}
                alt="계곡 이미지"
                width="100%"
                height="60%"
              />
              <div className={styles.valleyInfo}>
                <div className={styles.valleyTitle}>
                  <span>{item.waterPlaceName}</span>
                  <span>{item.location}</span>
                </div>
                <div className={styles.valleyReview}>
                  <span>{`${item.rating}/5`}</span>
                  <span>{`리뷰 ${item.reviewCnt}개`}</span>
                </div>
              </div>
            </div>
          );
        })}
      </div>
    </div>
  );
};

export default PopularValley;
