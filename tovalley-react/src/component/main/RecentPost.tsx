import React, { FC } from "react";
import styles from "../../css/main/RecentPost.module.css";
import { RecentPostType, RecentReviewType } from "../../typings/db";

const RecentPost: FC<{
  recentLostPost?: RecentPostType[];
  recentReviewPost?: RecentReviewType[];
}> = ({ recentLostPost, recentReviewPost }) => {
  return (
    <div className={styles.recentPost}>
      <h1>{recentLostPost ? "최근 분실물 게시글" : "최근 리뷰"}</h1>
      <div className={styles.recentPostList}>
        {recentLostPost
          ? recentLostPost.map((post, index) => {
              return (
                <div
                  key={post.lostFoundBoardId}
                  className={styles.recentPostItem}
                >
                  <div className={styles.header}>
                    <div className={styles.title}>
                      <span
                        className={
                          post.lostFoundBoardCategory === "LOST"
                            ? styles.lost
                            : styles.found
                        }
                      >
                        {post.lostFoundBoardCategory === "LOST"
                          ? "물건 찾아요"
                          : "주인 찾아요"}
                      </span>
                      <h4>{post.lostFoundBoardTitle}</h4>
                    </div>
                    <span>
                      {index === 0
                        ? "방금전"
                        : index === 1
                        ? "10분전"
                        : index === 2
                        ? "14분전"
                        : "20분전"}
                    </span>
                  </div>
                  <p>{post.lostFoundBoardContent}</p>
                </div>
              );
            })
          : recentReviewPost?.map((post, index) => {
              return (
                <div key={post.reviewId} className={styles.recentPostItem}>
                  <div className={styles.header}>
                    <div className={styles.title}>
                      <h4>{post.reviewTitle}</h4>
                    </div>
                    <span>
                      {index === 0
                        ? "방금전"
                        : index === 1
                        ? "5분전"
                        : index === 2
                        ? "20분전"
                        : "25분전"}
                    </span>
                  </div>
                  <p>{post.reviewContent}</p>
                </div>
              );
            })}
      </div>
    </div>
  );
};

export default RecentPost;
