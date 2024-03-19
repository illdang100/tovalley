import React, { useEffect, useState } from "react";
import Header from "../component/header/Header";
import Footer from "../component/footer/Footer";
import styles from "../css/lostItem/LostItemPost.module.css";
import { AiOutlineComment } from "react-icons/ai";
import { FaRegComment } from "react-icons/fa";
import { MdLocationPin } from "react-icons/md";
import { LostPost, LostPostComment } from "../typings/db";
import { useNavigate, useParams } from "react-router-dom";
import axiosInstance from "../axios_interceptor";
import { elapsedTime } from "../composables/elapsedTime";
import CustomModal from "../component/common/CustomModal";
import { FaCheck } from "react-icons/fa6";
import { useDispatch } from "react-redux";
import { view } from "../store/chat/chatViewSlice";
import { enterChatRoom } from "../store/chat/chatRoomIdSlice";
import { Cookies } from "react-cookie";

const LostItemPostPage = () => {
  const [lostPost, setLostPost] = useState<LostPost>();
  const { category, id } = useParams();
  const [commentList, setCommentList] = useState<LostPostComment[]>();
  const [comment, setComment] = useState("");
  const [deleteModal, setDeleteModal] = useState(false);
  const [resolveCheck, setResolveCheck] = useState(false);
  const [commentDeleteModal, setCommentDeleteModal] = useState(false);
  const navigate = useNavigate();
  const dispatch = useDispatch();
  const cookies = new Cookies();

  useEffect(() => {
    axiosInstance
      .get(`/api/lostItem/${id}`)
      .then((res) => {
        console.log(res);
        setLostPost({ data: res.data.data });
        setCommentList(res.data.data.comments);
        setResolveCheck(res.data.data.isResolved);
      })
      .catch((err) => console.log(err));
  }, [id]);

  const resolveStatus = () => {
    axiosInstance
      .patch(
        `/api/auth/lostItem/${id}`,
        {},
        {
          params: {
            isResolved: !resolveCheck,
          },
        }
      )
      .then((res) => {
        console.log(res);
        setResolveCheck(!resolveCheck);
      })
      .catch((err) => console.log(err));
  };

  const deletePost = () => {
    axiosInstance
      .delete(`/api/auth/lostItem/${id}`)
      .then((res) => {
        console.log(res);
        window.location.replace("/lost-item");
      })
      .catch((err) => console.log(err));
  };

  const closeDeleteModal = () => {
    setDeleteModal(false);
    setCommentDeleteModal(false);
  };

  const addComment = () => {
    const date = new Date();
    axiosInstance
      .post(`/api/auth/lostItem/${id}/comment`, {
        commentContent: comment,
      })
      .then((res) => {
        console.log(res);
        if (commentList && commentList?.length > 0) {
          setCommentList([
            ...commentList,
            {
              commentId: commentList[commentList?.length - 1].commentId + 1,
              commentAuthor: lostPost ? lostPost.data.author : "",
              commentContent: comment,
              commentCreateAt: `${date}`,
              commentByUser: true,
              commentAuthorProfile: lostPost
                ? lostPost.data.boardAuthorProfile
                : "",
            },
          ]);
        } else {
          setCommentList([
            {
              commentId: 1,
              commentAuthor: lostPost ? lostPost.data.author : "",
              commentContent: comment,
              commentCreateAt: `${date}`,
              commentByUser: true,
              commentAuthorProfile: lostPost
                ? lostPost.data.boardAuthorProfile
                : "",
            },
          ]);
        }
        // const newCommentList = commentList?.concat([res.data.data]);
        setComment("");
      })
      .catch((err) => console.log(err));
  };

  const deleteComment = (item: LostPostComment) => {
    axiosInstance
      .delete(`/api/auth/lostItem/${id}/comment/${item.commentId}`)
      .then((res) => {
        console.log(res);

        const newCommentList = commentList?.filter((comment) => {
          return comment !== item;
        });
        setCommentList(newCommentList);
      })
      .catch((err) => console.log(err));
  };

  const toUpdatePage = () => {
    navigate(`/lost-item/${category}/${id}/update`);
  };

  const newChatRoom = (nickname: string) => {
    axiosInstance
      .post("/api/auth/chatroom", {
        // 채팅방 생성 or 기존채팅방 id 요청
        recipientNick: nickname,
      })
      .then((res) => {
        console.log(res);
        dispatch(enterChatRoom(res.data.data.chatRoomId));
        dispatch(view(true));
      });
  };

  return (
    <div>
      <Header />
      <div className={styles.body}>
        <div className={styles.lostItemPost}>
          <div className={styles.locationInfo}>
            <MdLocationPin color="#66A5FC" size="28px" />
            <h4>{lostPost?.data.waterPlaceName}</h4>
            <span>{lostPost?.data.waterPlaceAddress}</span>
          </div>
          <div className={styles.postTop}>
            <div className={styles.authorInfo}>
              <div className={styles.profileImage}>
                <img
                  src={
                    lostPost?.data.boardAuthorProfile
                      ? lostPost?.data.boardAuthorProfile
                      : process.env.PUBLIC_URL + "/img/user-profile.png"
                  }
                  alt="author-profile"
                />
              </div>
              <div className={styles.authorName}>
                <h4>{lostPost?.data.author}</h4>
                <span>
                  {lostPost ? elapsedTime(lostPost.data.postCreateAt) : ""}
                </span>
              </div>
            </div>
            {!lostPost?.data.isMyBoard && (
              <div
                className={styles.chatBtn}
                onClick={() => lostPost && newChatRoom(lostPost.data.author)}
              >
                <AiOutlineComment size="22px" />
                <span>채팅하기</span>
              </div>
            )}
          </div>
          <h1>{lostPost?.data.title}</h1>
          <p>{lostPost?.data.content}</p>
          <div className={styles.imageList}>
            {lostPost?.data.postImages.map((img) => {
              return (
                <div key={img} className={styles.imageContainer}>
                  <img src={img} alt="post-img" />
                </div>
              );
            })}
          </div>
          <div className={styles.postBottom}>
            <div className={styles.comment}>
              <FaRegComment />
              {/* <span>{lostPost?.data.commentCnt}</span> */}
              <span>{commentList?.length}</span>
            </div>
            {lostPost?.data.isMyBoard && (
              <div className={styles.modifyBtn}>
                <div className={styles.solvedStatus}>
                  <div
                    className={resolveCheck ? styles.checked : styles.checkBox}
                    onClick={() => {
                      resolveStatus();
                    }}
                  >
                    <FaCheck />
                  </div>
                  <span>해결 완료</span>
                </div>
                <div className={styles.deleteBtn}>
                  <div>
                    <span onClick={() => setDeleteModal(true)}>삭제</span>
                  </div>
                  <div>
                    <span onClick={() => toUpdatePage()}>수정</span>
                  </div>
                </div>
              </div>
            )}
          </div>
        </div>
        {cookies.get("ISLOGIN") && (
          <div className={styles.postComment}>
            <div className={styles.commentInput}>
              <input
                placeholder="댓글을 입력하세요."
                value={comment}
                onChange={(e) => setComment(e.target.value)}
              />
            </div>
            <div className={styles.uploadBtn} onClick={addComment}>
              등록
            </div>
          </div>
        )}
        <div className={styles.commentList}>
          {commentList &&
            commentList.length > 0 &&
            commentList?.map((item) => (
              <div key={item.commentAuthor} className={styles.commentItem}>
                <div className={styles.commentTop}>
                  <div className={styles.commentInfo}>
                    <div className={styles.commentProfileImage}>
                      <img
                        src={
                          item.commentAuthorProfile
                            ? item.commentAuthorProfile
                            : process.env.PUBLIC_URL + "/img/user-profile.png"
                        }
                        alt="author-profile"
                      />
                    </div>
                    <span className={styles.commentWriterName}>
                      {item.commentAuthor}
                    </span>
                    <span>{elapsedTime(item.commentCreateAt)}</span>
                  </div>
                  {item.commentByUser ? (
                    <div
                      className={styles.deleteComment}
                      onClick={() => {
                        setCommentDeleteModal(true);
                      }}
                    >
                      삭제
                    </div>
                  ) : (
                    <div
                      className={styles.commentChatBtn}
                      onClick={() => newChatRoom(item.commentAuthor)}
                    >
                      <AiOutlineComment size="25px" />
                      <span>채팅</span>
                    </div>
                  )}
                  {commentDeleteModal && (
                    <CustomModal
                      content="댓글을 삭제하시겠습니까?"
                      customFunc={() => deleteComment(item)}
                      handleModal={closeDeleteModal}
                    />
                  )}
                </div>
                <p>{item.commentContent}</p>
              </div>
            ))}
        </div>
        {deleteModal && (
          <CustomModal
            content="정말 삭제하시겠습니까?"
            customFunc={deletePost}
            handleModal={closeDeleteModal}
          />
        )}
      </div>
      <Footer />
    </div>
  );
};

export default LostItemPostPage;
