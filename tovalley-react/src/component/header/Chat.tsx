import React, { useEffect, useState } from "react";
import styles from "../../css/header/Chat.module.css";
import { MdLogout } from "react-icons/md";
import ChatComponent from "./ChatComponent";
import { MdArrowBackIos } from "react-icons/md";
import axiosInstance from "../../axios_interceptor";
import { ChatRoomItem, NotificationType } from "../../typings/db";
import { elapsedTime } from "../../composables/elapsedTime";
import { RootState } from "../../store/store";
import { useDispatch, useSelector } from "react-redux";
import { enterChatRoom } from "../../store/chat/chatRoomIdSlice";
import { view } from "../../store/chat/chatViewSlice";
import { Client } from "@stomp/stompjs";

const Chat = () => {
  const chatView = useSelector((state: RootState) => state.view.value);
  const [appear, setAppear] = useState("");
  const [chatRoomList, setChatRoomList] = useState<ChatRoomItem[]>([]);
  const dispatch = useDispatch();
  const [client, setClient] = useState<Client | null>();
  const [notification, setNotification] = useState<NotificationType | null>();
  const clientSelector = useSelector((state: RootState) => state.client.value);
  const chatRoomId = useSelector((state: RootState) => state.chatRoomId.value);
  const chatId = useSelector((state: RootState) => state.chatId.value);
  const notificationSelector = useSelector(
    (state: RootState) => state.notification.value
  );

  useEffect(() => {
    if (clientSelector) {
      setClient(clientSelector);
    }
  }, [clientSelector]);

  useEffect(() => {
    setNotification(notificationSelector);
  }, [notificationSelector]);

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
    if (clientSelector) {
      console.log("clientSelector 있음");
      console.log(clientSelector);
      axiosInstance
        .get("/api/auth/chatroom") // 채팅방 목록
        .then((res) => {
          console.log(res);
          setChatRoomList(res.data.data.content);
        })
        .catch((err) => console.log(err));
    }
  }, [clientSelector]);

  const outChatting = () => {
    dispatch(view(false));
    if (client && chatId) {
      client.unsubscribe(chatId);
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
            <span onClick={() => dispatch(enterChatRoom(null))}>
              <MdArrowBackIos />
            </span>
          )}
          {!chatRoomId && <h1>illdang100</h1>}
          <span onClick={outChatting}>
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
