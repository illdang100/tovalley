import React, { FC, useEffect, useState } from "react";
import { IoCloseOutline } from "react-icons/io5";
import styles from "../../css/valley/DetailReviewimg.module.css";

interface Props {
  detailReview: {
    view: boolean;
    images: string[];
  };
  setDetailReview: React.Dispatch<
    React.SetStateAction<{
      view: boolean;
      images: string[];
    }>
  >;
}

const DetailReviewImg: FC<Props> = ({ detailReview, setDetailReview }) => {
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

  const [imgUrl, setImgUrl] = useState(detailReview.images[0]);

  return (
    <div className={styles.imgContainer}>
      <div className={styles.imgBox}>
        <span
          className={styles.close}
          onClick={() => setDetailReview({ ...detailReview, view: false })}
        >
          <IoCloseOutline color="#FFFFFF" size="40px" />
        </span>
        <div className={styles.reviewImg}>
          <img src={imgUrl} alt="리뷰 이미지" width="60%" height="380px" />
          <div className={styles.imgList}>
            {detailReview.images.map((item, index) => {
              return (
                <img
                  key={index}
                  src={item}
                  alt="리뷰 이미지"
                  width="80px"
                  height="80px"
                  onClick={() => setImgUrl(item)}
                />
              );
            })}
          </div>
        </div>
      </div>
    </div>
  );
};
export default DetailReviewImg;
