import React, { FC } from "react";
import styles from "../../css/lostItem/LostPost.module.css";

interface Props {
  title: string;
}

const LostPost: FC<Props> = ({ title }) => {
  return (
    <div
      className={styles.lostPostWrap}
      style={title === "분실물" ? { marginRight: "1.5em" } : {}}
    >
      <h1 className={styles.lostPostHeader}>
        {title === "분실물" ? "최근 분실물 게시글" : "최근 리뷰"}
      </h1>
      <div className={styles.lostPostContents}>
        <div className={styles.contentsTop}>
          <div className={styles.title}>
            {title === "분실물" && (
              <span className={styles.categoryFind}>찾아요</span>
            )}
            <h4>핸드폰을 잃어버렸어요</h4>
          </div>
          <span>5분전</span>
        </div>
        <p>금오계곡에서 아이폰15 보신 분 있으신가요 ㅠㅠ</p>
      </div>
    </div>
  );
};

export default LostPost;
