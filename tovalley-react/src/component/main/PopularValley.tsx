import React, { useState } from "react";
import styles from "../../css/main/PopularValley.module.css";

const valleyList = [
  {
    name: "금오계곡",
    region: "경상북도 구미시",
    rating: 4.3,
    review: 214,
  },
  {
    name: "구미계곡",
    region: "경상북도 구미시",
    rating: 4.2,
    review: 197,
  },
  {
    name: "우왕계곡",
    region: "대구광역시",
    rating: 4.1,
    review: 100,
  },
  {
    name: "김천계곡",
    region: "경상북도 김천시",
    rating: 3.8,
    review: 195,
  },
];

const PopularValley = () => {
  const [clicked, setClicked] = useState("평점");

  return (
    <div className={styles.popularValley}>
      <h4>전국 인기 계곡</h4>
      <div className={styles.category}>
        <span
          onClick={() => {
            setClicked("평점");
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
          }}
          className={
            clicked === "리뷰" ? styles.clickedCategory : styles.categoryBtn
          }
        >
          리뷰
        </span>
      </div>
      <div className={styles.popularList}>
        {valleyList.map((item) => {
          return (
            <div className={styles.popularItem}>
              <img
                src={process.env.PUBLIC_URL + "/img/계곡test이미지.png"}
                alt="계곡 이미지"
                width="100%"
                height="60%"
              />
              <div className={styles.valleyInfo}>
                <div className={styles.valleyTitle}>
                  <span>{item.name}</span>
                  <span>{item.region}</span>
                </div>
                <div className={styles.valleyReview}>
                  <span>{`${item.rating}/5`}</span>
                  <span>{`리뷰 ${item.review}개`}</span>
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
