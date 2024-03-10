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
import { Client } from "@stomp/stompjs";

const Chat = () => {
  const chatView = useSelector((state: RootState) => state.view.value);
  const [appear, setAppear] = useState("");
  const [chatRoomList, setChatRoomList] = useState<ChatRoomItem[]>([]);
  const dispatch = useDispatch();
  const clientSelector = useSelector((state: RootState) => state.client.value);
  const chatRoomId = useSelector((state: RootState) => state.chatRoomId.value);
  const chatId = useSelector((state: RootState) => state.chatId.value);
  const notification = useSelector(
    (state: RootState) => state.notification.value
  );
  const [client, setClient] = useState<Client | null>(clientSelector);

  useEffect(() => {
    if (clientSelector) {
      setClient(clientSelector);
    }
  }, [clientSelector]);

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
    if (clientSelector && chatView) {
      console.log("clientSelector 있음");
      console.log(clientSelector);
      axiosInstance
        .get("/api/auth/chatroom") // 채팅방 목록
        .then((res) => {
          console.log(res);
          res.data !== "" && setChatRoomList(res.data.data);
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

  const outChatting = () => {};

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
          {!chatRoomId && <h1>illdang100</h1>}
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
