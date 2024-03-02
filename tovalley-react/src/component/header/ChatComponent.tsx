import React, { FC, useEffect, useState } from "react";
import SockJS from "sockjs-client";
import { Client } from "@stomp/stompjs";
import axios from "axios";
import styles from "../../css/header/ChatComponent.module.css";

const ChatComponent: FC<{ recipientNick: string }> = ({ recipientNick }) => {
  const [client, setClient] = useState<Client>();
  const [roomId, setRoomId] = useState(null); // 서버에서 받아올 채팅방 ID
  const [message, setMessage] = useState(""); // 보낼 메시지

  useEffect(() => {
    const socket = new SockJS("http://localhost:8081/stomp/chat"); // 서버와 웹소켓 연결
    const stompClient = new Client({
      webSocketFactory: () => socket,
      debug: (str) => {
        console.log(str);
      },
      reconnectDelay: 5000,
    });

    const requestChatRoom = async () => {
      try {
        const response = await axios.post(
          "http://localhost:8081/api/auth/chatroom",
          {
            recipientNick: recipientNick,
          }
        );
        console.log(response);
        setRoomId(response.data.chatRoomId); // 채팅방 ID 설정

        stompClient.onConnect = () => {
          if (roomId) {
            stompClient.subscribe(`/sub/chat/room/${roomId}`, (message) => {
              // 특정 채팅방 구독
              console.log(message);
              //console.log(JSON.parse(message.body)); // 상대방으로부터 메시지를 수신 받을 때마다 함수 실행
            });
          }
        };

        stompClient.activate(); // 웹소켓 연결 활성화
        setClient(stompClient);
      } catch (error) {
        console.error(error);
      }
    };

    if (stompClient) {
      requestChatRoom();
    }

    return () => {
      if (client) {
        client.deactivate(); // 컴포넌트 unmount 시 웹소켓 연결 비활성화
      }
    };
  }, []);

  const sendMessage = () => {
    if (client) {
      const chatMessage = {
        roomId: roomId,
        message: message,
        // 필요한 다른 필드들
      };

      client.publish({
        destination: "/pub/chat/message",
        body: JSON.stringify(chatMessage),
      }); // 메시지 전송
    }
  };

  return (
    <div className={styles.chatComponent}>
      <div className={styles.speechBubble}>
        <div className={styles.bubbleComponent}>
          <span>15:12</span>
          <div>안녕하세요 혹시 어디서 발견하셨나요?</div>
        </div>
      </div>
      <div className={styles.sendMessage}>
        <input
          type="text"
          placeholder="메시지 보내기"
          value={message}
          onChange={(e) => setMessage(e.target.value)}
        />
        <button onClick={sendMessage}>전송</button>
      </div>
    </div>
  );
};

export default ChatComponent;
