import React, { FC, useEffect, useState } from "react";
import styles from "../../css/header/Chat.module.css";
import { MdLogout } from "react-icons/md";
import ChatComponent from "./ChatComponent";
import { MdArrowBackIos } from "react-icons/md";
import axiosInstance from "../../axios_interceptor";
import { ChatRoomItem } from "../../typings/db";

interface Props {
  outChatting: () => void;
  chatView: boolean;
}
const Chat: FC<Props> = ({ outChatting, chatView }) => {
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

  const [appear, setAppear] = useState("");
  const [enterChatRoom, setEnterChatRoom] = useState("");
  const [chatRoomList, setChatRoomList] = useState<ChatRoomItem[]>([]);

  useEffect(() => {
    console.log(chatView);
    if (chatView) {
      setAppear("start");
    } else {
      setAppear("end");
    }
  }, [chatView]);

  const getChatRoomList = () => {
    setChatRoomList([
      {
        chatRoomId: 1,
        chatRoomTitle: "누구누구와(과)의 채팅방입니다.",
        otherUserProfileImage: "프로필 이미지 URL",
        otherUserNick: "누구누구",
        createdChatRoomDate: "2024-02-15 05:05:04",
        lastMessageContent: "안녕하세요",
        lastMessageTime: "2024-02-15 05:05:04",
      },

      {
        chatRoomId: 2,
        chatRoomTitle: "호호와(과)의 채팅방입니다.",
        otherUserProfileImage: "프로필 이미지 URL",
        otherUserNick: "호호",
        createdChatRoomDate: "2024-02-15 05:05:04",
        lastMessageContent: "안녕하세요!!",
        lastMessageTime: "2024-02-15 05:06:04",
      },
    ]);
    // axiosInstance
    //   .get("/api/auth/chatroom")
    //   .then((res) => console.log(res))
    //   .catch((err) => console.log(err));
  };

  useEffect(() => {
    getChatRoomList();
  }, []);

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
          {enterChatRoom !== "" && (
            <span onClick={() => setEnterChatRoom("")}>
              <MdArrowBackIos />
            </span>
          )}
          <h1>illdang100</h1>
          <span onClick={outChatting}>
            <MdLogout />
          </span>
        </div>
        {enterChatRoom === "" ? (
          <>
            <h4>채팅목록 {chatRoomList.length}</h4>
            <div className={styles.chatList}>
              {chatRoomList.map((chatRoom) => (
                <div
                  key={chatRoom.chatRoomId}
                  className={styles.chatItem}
                  onClick={() => setEnterChatRoom(chatRoom.otherUserNick)}
                >
                  <div className={styles.chatTitle}>
                    <div className={styles.userInfo}>
                      <div className={styles.profileImg}>
                        <div
                          style={{
                            backgroundColor: "grey",
                            width: "50px",
                            height: "50px",
                          }}
                        />
                      </div>
                      <span className={styles.nickName}>
                        {chatRoom.otherUserNick}
                      </span>
                    </div>
                    <span>5분전</span>
                  </div>
                  <div className={styles.chatContent}>
                    <span className={styles.content}>
                      {chatRoom.lastMessageContent}
                    </span>
                    <div className={styles.count}>1</div>
                  </div>
                </div>
              ))}
            </div>
          </>
        ) : (
          <ChatComponent recipientNick={enterChatRoom} />
        )}
      </div>
    </div>
  );
};

export default Chat;
