import React, { useEffect, useState } from "react";
import styles from "../../css/header/Chat.module.css";
import { MdLogout } from "react-icons/md";
import ChatComponent from "./ChatComponent";
import { MdArrowBackIos } from "react-icons/md";
import axiosInstance from "../../axios_interceptor";
import { ChatRoomItem } from "../../typings/db";
import { elapsedTime } from "../../composables/elapsedTime";
import { RootState } from "../../store/store";
import { useDispatch, useSelector } from "react-redux";
import { enterChatRoom } from "../../store/chat/chatRoomIdSlice";

const Chat = () => {
  const chatView = useSelector((state: RootState) => state.view.value);
  const [appear, setAppear] = useState("");
  const [chatRoomList, setChatRoomList] = useState<ChatRoomItem[]>([]);
  const dispatch = useDispatch();
  const clientSelector = useSelector((state: RootState) => state.client.value);
  const chatRoomId = useSelector((state: RootState) => state.chatRoomId.value);
  const notification = useSelector(
    (state: RootState) => state.notification.value
  );
  const subscription = useSelector(
    (state: RootState) => state.subscription.value
  );

  useEffect(() => {
    if (chatView) {
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
    }
  }, [chatView]);

  useEffect(() => {
    if (chatView) {
      setAppear("start");
    } else {
      setAppear("end");
    }
  }, [chatView]);

  useEffect(() => {
    if (clientSelector && chatView && !chatRoomId) {
      console.log("clientSelector 있음");
      console.log(clientSelector);
      axiosInstance
        .get("/api/auth/chatroom") // 채팅방 목록
        .then((res) => {
          console.log(res);
          // res.data !== "" && setChatRoomList(res.data.data);
          setChatRoomList([
            {
              chatRoomId: 1,
              chatRoomTitle: "test1 와(과)의 채팅방입니다.",
              createdChatRoomDate: "2024-03-03 22:43:00",
              lastMessageContent: "제 핸드폰인 것 같아요.",
              lastMessageTime: "2024-03-12 12:51:25",
              otherUserNick: "illdang100",
              otherUserProfileImage: "/img/dummy/profile-img2.jpg",
              unReadMessageCount: 0,
            },
            {
              chatRoomId: 2,
              chatRoomTitle: "test1 와(과)의 채팅방입니다.",
              createdChatRoomDate: "2024-03-03 22:43:00",
              lastMessageContent: "감사합니다.",
              lastMessageTime: "2024-03-10 12:51:25",
              otherUserNick: "kim123",
              otherUserProfileImage: null,
              unReadMessageCount: 0,
            },
            {
              chatRoomId: 3,
              chatRoomTitle: "test1 와(과)의 채팅방입니다.",
              createdChatRoomDate: "2024-03-03 22:43:00",
              lastMessageContent: "네~",
              lastMessageTime: "2024-03-08 12:51:25",
              otherUserNick: "계곡탐험가",
              otherUserProfileImage: "/img/dummy/profile-img3.jpg",
              unReadMessageCount: 0,
            },
            {
              chatRoomId: 4,
              chatRoomTitle: "test1 와(과)의 채팅방입니다.",
              createdChatRoomDate: "2024-03-03 22:43:00",
              lastMessageContent: "좋은 하루 되세요",
              lastMessageTime: "2024-03-08 12:51:25",
              otherUserNick: "yunej27",
              otherUserProfileImage: "/img/dummy/profile-img4.jpg",
              unReadMessageCount: 0,
            },
          ]);
        })
        .catch((err) => console.log(err));
    }
  }, [clientSelector, chatView, chatRoomId]);

  useEffect(() => {
    if (notification && chatView && notification.notificationType === "CHAT") {
      chatRoomList.forEach((chat, index) => {
        const chatLastTime = new Date(chat.lastMessageTime);
        const notificationTime = new Date(notification.createdDate);
        console.log(chatLastTime.getTime(), notificationTime.getTime());
        if (
          chat.chatRoomId === notification.chatRoomId &&
          chatLastTime.getTime() !== notificationTime.getTime()
        ) {
          chatRoomList.splice(index, 1);
          chatRoomList.splice(0, 0, {
            chatRoomId: chat.chatRoomId,
            chatRoomTitle: chat.chatRoomTitle,
            otherUserProfileImage: chat.otherUserProfileImage,
            otherUserNick: chat.otherUserNick,
            createdChatRoomDate: chat.createdChatRoomDate,
            lastMessageContent: notification.content,
            unReadMessageCount: chat.unReadMessageCount + 1,
            lastMessageTime: notification.createdDate,
          });
        } else if (chat.chatRoomId !== notification.chatRoomId) {
          setChatRoomList([
            {
              chatRoomId: notification.chatRoomId,
              chatRoomTitle: "",
              otherUserProfileImage: null,
              otherUserNick: notification.senderNick,
              createdChatRoomDate: notification.createdDate,
              lastMessageContent: notification.content,
              unReadMessageCount: 1,
              lastMessageTime: notification.createdDate,
            },
            ...chatRoomList,
          ]);
        }
      });
    }
  }, [notification]);

  const outChatting = () => {
    if (clientSelector?.connected && subscription) {
      console.log("구독해제!!");
      clientSelector.unsubscribe(subscription.id);
    }
  };

  return (
    <div className={chatView ? styles.chatContainer : ""}>
      <div
        className={
          appear === "start"
            ? `${styles.chatWrap} ${styles.startSlideAnimation}`
            : appear === "end"
            ? `${styles.chatWrap} ${styles.endSlideAnimation}`
            : styles.chatWrap
        }
      >
        <div className={styles.header}>
          {chatRoomId && (
            <span
              onClick={() => {
                dispatch(enterChatRoom(null));
                outChatting();
              }}
            >
              <MdArrowBackIos />
            </span>
          )}
          {!chatRoomId ? <h1>tovalley</h1> : <h1>illdang100</h1>}
          <span>
            <MdLogout />
          </span>
        </div>
        {!chatRoomId ? (
          <>
            <h4>채팅목록 {chatRoomList.length}</h4>
            <div className={styles.chatList}>
              {chatRoomList.map((chatRoom) => (
                <div
                  key={chatRoom.chatRoomId}
                  className={styles.chatItem}
                  onClick={() => dispatch(enterChatRoom(chatRoom.chatRoomId))}
                >
                  <div className={styles.chatTitle}>
                    <div className={styles.userInfo}>
                      <div className={styles.profileImg}>
                        <img
                          src={
                            chatRoom.otherUserProfileImage
                              ? chatRoom.otherUserProfileImage
                              : process.env.PUBLIC_URL + "/img/user-profile.png"
                          }
                          alt="profile-img"
                        />
                      </div>
                      <span className={styles.nickName}>
                        {chatRoom.otherUserNick}
                      </span>
                    </div>
                    <span>
                      {chatRoom.lastMessageTime
                        ? elapsedTime(chatRoom.lastMessageTime)
                        : ""}
                    </span>
                  </div>
                  <div className={styles.chatContent}>
                    <span className={styles.content}>
                      {chatRoom.lastMessageContent
                        ? chatRoom.lastMessageContent
                        : "(대화 내용이 없습니다.)"}
                    </span>
                    {chatRoom.unReadMessageCount !== 0 && (
                      <div className={styles.count}>
                        {chatRoom.unReadMessageCount}
                      </div>
                    )}
                  </div>
                </div>
              ))}
            </div>
          </>
        ) : (
          <ChatComponent />
        )}
      </div>
    </div>
  );
};

export default Chat;
