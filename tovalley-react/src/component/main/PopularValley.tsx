import React, { FC, useEffect, useState } from "react";
import styles from "../../css/main/PopularValley.module.css";
import axios from "axios";
import { useNavigate } from "react-router-dom";

interface Props {
  place: {
    waterPlaceId: number;
    waterPlaceName: string;
    waterPlaceImageUrl: string;
    location: string;
    rating: number;
    reviewCnt: number;
  }[];
  setPopularValley: React.Dispatch<
    React.SetStateAction<
      {
        waterPlaceId: number;
        waterPlaceName: string;
        waterPlaceImageUrl: string;
        location: string;
        rating: number;
        reviewCnt: number;
      }[]
    >
  >;
}
const localhost = process.env.REACT_APP_HOST;

const PopularValley: FC<Props> = ({ place, setPopularValley }) => {
  const [clicked, setClicked] = useState("평점");
  const navigation = useNavigate();
  const [num, setNum] = useState(0);
  const [carouselTransition, setCarouselTransition] = useState(
    "transform 500ms ease-in-out"
  );
  const [currList, setCurrList] = useState<
    {
      waterPlaceId: number;
      waterPlaceName: string;
      waterPlaceImageUrl: string;
      location: string;
      rating: number;
      reviewCnt: number;
    }[]
  >([place[place.length - 1], ...place, place[0]]);

  useEffect(() => {
    if (place.length !== 0) {
      setCurrList([
        place[place.length - 1],
        ...place,
        place[0],
        place[1],
        place[2],
        place[3],
      ]);
    }
  }, [place]);

  useEffect(() => {
    const timer = setInterval(() => {
      setNum((num) => num + 1);
      setCarouselTransition("transform 500ms ease-in-out");
    }, 2500);

    return () => {
      clearInterval(timer);
    };
  }, []);

  useEffect(() => {
    if (num === place.length) handleOriginSlide(0);
  }, [num]);

  function handleOriginSlide(index: number) {
    setTimeout(() => {
      setNum(index);
      setCarouselTransition("");
    }, 500);
  }

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
        {currList.map((item, index) => {
          return (
            <div
              key={index}
              className={styles.popularItem}
              onClick={() => {
                navigation(`/valley/${item.waterPlaceId}`);
              }}
              style={{
                transition: `${carouselTransition}`,
                transform: `translateX(-${num}00%)`,
              }}
            >
              <span>{index + 1 <= 8 ? index + 1 : (index + 1) % 8}</span>
              <div className={styles.valleyItemImg}>
                <img
                  src={
                    item.waterPlaceImageUrl === null ||
                    item.waterPlaceImageUrl === ""
                      ? process.env.PUBLIC_URL + "/img/default-image.png"
                      : item.waterPlaceImageUrl
                  }
                  alt="계곡 이미지"
                  width="100%"
                />
              </div>
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
