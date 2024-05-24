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
import { setChatRoomName } from "../../store/chat/chatRoomNameSlice";

const Chat = () => {
  const chatView = useSelector((state: RootState) => state.view.value);
  const [appear, setAppear] = useState("");
  const [chatRoomList, setChatRoomList] = useState<{
    memberName: String;
    chatRooms: ChatRoomItem[];
  }>();
  const dispatch = useDispatch();
  const clientSelector = useSelector((state: RootState) => state.client.value);
  const chatRoomId = useSelector((state: RootState) => state.chatRoomId.value);
  const notification = useSelector(
    (state: RootState) => state.notification.value
  );
  const subscription = useSelector(
    (state: RootState) => state.subscription.value
  );
  const chatRoomName = useSelector(
    (state: RootState) => state.chatRoomName.value
  );
  const [bgForeground, setBgForeground] = useState(false);

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
    if (appear === "end") {
      const fadeTimer = setTimeout(() => {
        setBgForeground(true);
      }, 500);
      return () => {
        clearTimeout(fadeTimer);
      };
    } else {
      setBgForeground(false);
    }
  }, [appear]);

  useEffect(() => {
    if (clientSelector && chatView && !chatRoomId) {
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
      chatRoomList?.chatRooms.forEach((chat, index) => {
        const chatLastTime = new Date(chat.lastMessageTime);
        const notificationTime = new Date(notification.createdDate);
        console.log(chatLastTime.getTime(), notificationTime.getTime());
        if (
          chat.chatRoomId === notification.chatRoomId &&
          chatLastTime.getTime() !== notificationTime.getTime()
        ) {
          chatRoomList?.chatRooms.splice(index, 1);
          chatRoomList?.chatRooms.splice(0, 0, {
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
          setChatRoomList({
            memberName: chatRoomList.memberName,
            chatRooms: [
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
              ...chatRoomList.chatRooms,
            ],
          });
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
    <div
      className={`${styles.chatContainer} ${
        bgForeground ? "" : styles.zIndex
      } `}
    >
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
          {!chatRoomId ? (
            <h1>{chatRoomList?.memberName}</h1>
          ) : (
            <h1>{chatRoomName}</h1>
          )}
          <span>
            <MdLogout />
          </span>
        </div>
        {!chatRoomId ? (
          <>
            <h4>채팅목록 {chatRoomList?.chatRooms.length}</h4>
            <div className={styles.chatList}>
              {chatRoomList?.chatRooms.map((chatRoom) => (
                <div
                  key={chatRoom.chatRoomId}
                  className={styles.chatItem}
                  onClick={() => {
                    dispatch(enterChatRoom(chatRoom.chatRoomId));
                    dispatch(setChatRoomName(chatRoom.otherUserNick));
                  }}
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
                        : "사진을 보냈습니다."}
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
