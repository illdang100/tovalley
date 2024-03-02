import React, { useEffect, useState } from "react";
import { Client } from "@stomp/stompjs";
import styles from "../../css/header/ChatComponent.module.css";
import axiosInstance from "../../axios_interceptor";
import { MessageListType, MessageType } from "../../typings/db";
import { useDispatch, useSelector } from "react-redux";
import { RootState } from "../../store/store";
import { setId } from "../../store/chat/chatIdSlice";

const ChatComponent = () => {
  const [client, setClient] = useState<Client | null>();
  const [messageList, setMessageList] = useState<MessageListType>();
  const [message, setMessage] = useState<MessageType[]>([]);
  const [content, setContent] = useState(""); // 보낼 메시지
  const clientSelector = useSelector((state: RootState) => state.client.value);
  const chatRoomId = useSelector((state: RootState) => state.chatRoomId.value);
  const dispatch = useDispatch();

  useEffect(() => {
    setClient(clientSelector);
    const requestMessageList = async () => {
      const response = await axiosInstance.get(
        `/api/auth/chat/messages/${chatRoomId}` // 채팅 메시지 목록 조회
      );
      setMessageList(response.data.chatMessages.data.content.chatMessages);
      if (client?.connected) {
        const subscription = client.subscribe(
          `/sub/chat/room/${chatRoomId}`,
          (msg) => {
            // 특정 채팅방 구독
            console.log(JSON.parse(msg.body));
            setMessage(message.concat([JSON.parse(msg.body)])); // 상대방으로부터 메시지를 수신 받을 때마다 함수 실행
          }
        );
        dispatch(setId(subscription.id));
      }
    };

    //requestMessageList();

    return () => {
      if (client?.connected) {
        client.deactivate(); // 컴포넌트 unmount 시 웹소켓 연결 비활성화
      }
    };
  }, [chatRoomId]);

  const sendMessage = () => {
    if (client?.connected) {
      const chatMessage = {
        chatRoomId: chatRoomId,
        content,
      };
      client.publish({
        destination: "/pub/chat/message",
        body: JSON.stringify(chatMessage),
      }); // 메시지 전송
    } else {
      console.error("웹소켓 연결이 활성화되지 않았습니다.");
    }
  };

  return (
    <div className={styles.chatComponent}>
      <div>
        <div className={styles.messageList}>
          {messageList?.data.chatMessages.content.map((message) => {
            const date = new Date(message.createdAt);
            return (
              <div className={styles.speechBubble}>
                {message.myMsg ? (
                  <div style={{ justifyContent: "right" }}>
                    <div style={{ textAlign: "right" }}>
                      <span className={styles.readCount}>
                        {message.readCount}
                      </span>
                      <span
                        className={styles.sendTime}
                      >{`${date.getHours()}:${date.getMinutes()}`}</span>
                    </div>
                    <div className={styles.mySpeechBubble}>
                      {message.content}
                    </div>
                  </div>
                ) : (
                  <div style={{ justifyContent: "left" }}>
                    <div className={styles.opponentSpeechBubble}>
                      {message.content}
                    </div>
                    <div>
                      <span className={styles.readCount}>
                        {message.readCount}
                      </span>
                      <span
                        className={styles.sendTime}
                      >{`${date.getHours()}:${date.getMinutes()}`}</span>
                    </div>
                  </div>
                )}
              </div>
            );
          })}
        </div>
        {message.map((el) => {
          const date = new Date(el.createdAt);
          return (
            <div className={styles.speechBubble}>
              {el?.senderId === messageList?.data.memberId ? (
                <div style={{ justifyContent: "right" }}>
                  <div style={{ textAlign: "right" }}>
                    <span className={styles.readCount}>{el?.readCount}</span>
                    <span
                      className={styles.sendTime}
                    >{`${date.getHours()}:${date.getMinutes()}`}</span>
                  </div>
                  <div className={styles.mySpeechBubble}>{el?.content}</div>
                </div>
              ) : (
                <div style={{ justifyContent: "left" }}>
                  <div className={styles.opponentSpeechBubble}>
                    {el?.content}
                  </div>
                  <div>
                    <span className={styles.readCount}>{el?.readCount}</span>
                    <span
                      className={styles.sendTime}
                    >{`${date.getHours()}:${date.getMinutes()}`}</span>
                  </div>
                </div>
              )}
            </div>
          );
        })}
      </div>
      <div className={styles.sendMessage}>
        <input
          type="text"
          placeholder="메시지 보내기"
          value={content}
          onChange={(e) => setContent(e.target.value)}
        />
        <button onClick={sendMessage}>전송</button>
      </div>
    </div>
  );
};

export default ChatComponent;
