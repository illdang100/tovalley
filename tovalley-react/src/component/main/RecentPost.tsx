import React, { FC } from "react";
import styles from "../../css/main/RecentPost.module.css";
import { RecentPostType, RecentReviewType } from "../../typings/db";
import { elapsedTime } from "../../composables/elapsedTime";
import RatingStar from "../common/RatingStar";
import { useNavigate } from "react-router-dom";

const RecentPost: FC<{
  recentLostPost?: RecentPostType[];
  recentReviewPost?: RecentReviewType[];
}> = ({ recentLostPost, recentReviewPost }) => {
  const navigation = useNavigate();

  return (
    <div className={styles.recentPost}>
      <h1>{recentLostPost ? "최근 분실물 게시글" : "최근 리뷰"}</h1>
      <div className={styles.recentPostList}>
        {recentLostPost
          ? recentLostPost.map((post) => {
              return (
                <div
                  key={post.lostFoundBoardId}
                  className={styles.recentPostItem}
                  onClick={() => {
                    navigation(
                      `/lost-item/${post.lostFoundBoardCategory}/${post.lostFoundBoardId}`
                    );
                  }}
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
                    <span>{elapsedTime(post.lostFoundBoardCreatedAt)}</span>
                  </div>
                  <p>{post.lostFoundBoardContent}</p>
                </div>
              );
            })
          : recentReviewPost?.map((post) => {
              return (
                <div
                  key={post.reviewId}
                  className={styles.recentPostItem}
                  onClick={() => {
                    navigation(`/valley/${post.waterPlaceId}`);
                  }}
                >
                  <div className={styles.header}>
                    <div className={styles.title}>
                      <h4>{post.reviewContent}</h4>
                    </div>
                    <span>{elapsedTime(post.reviewCreatedAt)}</span>
                  </div>
                  <RatingStar rating={3} size="15px" />
                  <p>{post.reviewContent}</p>
                </div>
              );
            })}
      </div>
    </div>
  );
};

export default RecentPost;
