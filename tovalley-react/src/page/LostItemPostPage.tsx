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

const LostItemPostPage = () => {
  const [lostPost, setLostPost] = useState<LostPost>();
  const { category, id } = useParams();
  const [commentList, setCommentList] = useState<LostPostComment[]>();
  const [comment, setComment] = useState("");
  const [deleteModal, setDeleteModal] = useState(false);
  const [resolveCheck, setResolveCheck] = useState(false);
  const navigate = useNavigate();
  const dispatch = useDispatch();

  useEffect(() => {
    setLostPost({
      data: {
        title: "금오계곡에서 찾았어요",
        content: "아이폰15 입니다.",
        author: "illdang100",
        waterPlaceName: "금오계곡",
        waterPlaceAddress: "경상북도 구미시 옥계동",
        postCreateAt: "2024-01-19 21:33:10",
        postImages: [
          "/img/dummy/계곡이미지5.jpg",
          "/img/dummy/계곡이미지5.jpg",
          "/img/dummy/계곡이미지5.jpg",
        ],
        isResolved: false,
        isMyBoard: false,
        boardAuthorProfile: "",
        commentCnt: 3,
        comments: [
          {
            commentId: 1,
            commentAuthor: "행복한 어피치",
            commentContent: "혹시 흰색인가요?",
            commentCreatedAt: "2024-01-23 09:00:02",
            isMyComment: false, // 현재 유저가 작성한 댓글인지 확인
            commentAuthorProfile: "",
          },
        ],
      },
    });
    setCommentList([
      {
        commentId: 1,
        commentAuthor: "행복한 어피치",
        commentContent: "혹시 흰색인가요?",
        commentCreatedAt: "2024-01-23 09:00:02",
        isMyComment: true, // 현재 유저가 작성한 댓글인지 확인,
        commentAuthorProfile: "",
      },
    ]);

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
            status: resolveCheck,
          },
        }
      )
      .then((res) => {
        console.log(res);
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
  };

  const addComment = () => {
    axiosInstance
      .post(`/api/auth/lostItem/${id}/comment`, {
        commentContent: comment,
      })
      .then((res) => {
        console.log(res);
        const newCommentList = commentList?.concat([res.data.data]);
        setCommentList(newCommentList);
      })
      .catch((err) => console.log(err));
  };

  const deleteComment = (item: LostPostComment) => {
    const newCommentList = commentList?.filter((comment) => {
      return comment !== item;
    });
    setCommentList(newCommentList);
    axiosInstance
      .delete(`/api/auth/lostItem/${id}/comment/${item.commentId}`)
      .then((res) => console.log(res))
      .catch((err) => console.log(err));
  };

  const toUpdatePage = () => {
    navigate(`/lost-item/${category}/${id}/update`);
  };

  const newChatRoom = () => {
    axiosInstance
      .post("/api/auth/chatroom", {
        // 채팅방 생성 or 기존채팅방 id 요청
        // recipientNick: lostPost?.data.author,
        recipientNick: "test1",
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
            <MdLocationPin color="#66A5FC" size="22px" />
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
              <div className={styles.chatBtn} onClick={() => newChatRoom()}>
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
                <div className={styles.imageContainer}>
                  <img src={img} alt="post-img" />
                </div>
              );
            })}
          </div>
          <div className={styles.postBottom}>
            <div className={styles.comment}>
              <FaRegComment />
              <span>{lostPost?.data.commentCnt}</span>
            </div>
            {lostPost?.data.isMyBoard && (
              <div className={styles.modifyBtn}>
                <div className={styles.solvedStatus}>
                  <div
                    className={resolveCheck ? styles.checked : styles.checkBox}
                    onClick={() => {
                      setResolveCheck(!resolveCheck);
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
        <div className={styles.postComment}>
          <div className={styles.commentInput}>
            <div className={styles.commentUser}>
              <div className={styles.commentProfileImage}>
                <img
                  src={
                    lostPost?.data.boardAuthorProfile
                      ? lostPost?.data.boardAuthorProfile
                      : process.env.PUBLIC_URL + "/img/user-profile.png"
                  }
                  alt="author-profile"
                />
              </div>
              <span>{lostPost?.data.author}</span>
            </div>
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
        <div className={styles.commentList}>
          {commentList?.map((item) => (
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
                  <span>{elapsedTime(item.commentCreatedAt)}</span>
                </div>
                {item.isMyComment ? (
                  <div
                    className={styles.deleteComment}
                    onClick={() => deleteComment(item)}
                  >
                    삭제
                  </div>
                ) : (
                  <div className={styles.commentChatBtn}>
                    <AiOutlineComment size="25px" />
                    <span>채팅</span>
                  </div>
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
