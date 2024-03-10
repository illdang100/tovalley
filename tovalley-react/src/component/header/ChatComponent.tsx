import React, { useCallback, useEffect, useRef, useState } from "react";
import styles from "../../css/header/ChatComponent.module.css";
import axiosInstance from "../../axios_interceptor";
import { MessageListType, MessageType } from "../../typings/db";
import { useDispatch, useSelector } from "react-redux";
import { RootState } from "../../store/store";
import { DateTime } from "luxon";
import { setSubscription } from "../../store/chat/subscriptionSlice";

const ChatComponent = () => {
  const [messageList, setMessageList] = useState<MessageListType>();
  const [message, setMessage] = useState<MessageType>();
  const [chatMessageList, setChatMessageList] = useState<MessageType[]>([]);
  const [content, setContent] = useState(""); // 보낼 메시지
  const clientSelector = useSelector((state: RootState) => state.client.value);
  const chatRoomId = useSelector((state: RootState) => state.chatRoomId.value);
  const notification = useSelector(
    (state: RootState) => state.notification.value
  );
  const [notificationDate, setNotificationDate] = useState<Date>();
  const target = useRef<HTMLDivElement>(null);
  const [isPageEnd, setIsPageEnd] = useState<boolean>(false);
  const messageEndRef = useRef<HTMLDivElement>(null);
  const [reqMsg, setReqMsg] = useState(false);
  const dispatch = useDispatch();

  useEffect(() => {
    const requestMessageList = async () => {
      const response = await axiosInstance.get(
        `/api/auth/chat/messages/${chatRoomId}` // 채팅 메시지 목록 조회
      );
      console.log(response);
      setMessageList(response.data);

      if (clientSelector?.connected) {
        console.log("채팅방 구독 시작!!");
        const subscribe = clientSelector.subscribe(
          `/sub/chat/room/${chatRoomId}`,
          (msg) => {
            // 특정 채팅방 구독
            console.log(JSON.parse(msg.body));
            setMessage(JSON.parse(msg.body));
          }
        );
        dispatch(setSubscription(subscribe));
      }
    };

    if (clientSelector) requestMessageList();

    return () => {
      // if (clientSelector?.connected && subscription) {
      //   console.log("구독해제!!");
      //   subscription.unsubscribe();
      //   //clientSelector.deactivate(); // 컴포넌트 unmount 시 웹소켓 연결 비활성화
      // }
    };
  }, []);

  useEffect(() => {
    if (!reqMsg) messageEndRef.current?.scrollIntoView();
  }, [messageList, message]);

  useEffect(() => {
    if (message) {
      setChatMessageList([...chatMessageList, message]);
    }
  }, [message]);

  const getMessageList = async (id?: string) => {
    setReqMsg(true);
    let config;

    if (id) {
      config = {
        params: {
          cursorId: id,
        },
      };
    } else {
      config = undefined;
    }

    const res = await axiosInstance.get(
      `/api/auth/chat/messages/${chatRoomId}`,
      config // 채팅 메시지 목록 조회
    );
    console.log(res);
    setMessageList(res.data);
    if (res.data.data.chatMessages.last) {
      setIsPageEnd(true);
    }
  };

  const handleObserver = useCallback(
    async (
      [entry]: IntersectionObserverEntry[],
      observer: IntersectionObserver
    ) => {
      if (entry.isIntersecting) {
        observer.unobserve(entry.target);
        if (messageList && messageList?.data.chatMessages.content.length > 0)
          await getMessageList(
            messageList.data.chatMessages.content[0].chatMessageId
          );
      }
    },
    [messageList]
  );

  useEffect(() => {
    if (!target.current) return;

    const option = {
      root: null,
      rootMargin: "0px",
      threshold: 0,
    };

    const observer = new IntersectionObserver(handleObserver, option);

    target.current && observer.observe(target.current);

    return () => observer && observer.disconnect();
  }, [handleObserver, isPageEnd]);

  const sendMessage = () => {
    if (clientSelector?.connected) {
      const chatMessage = {
        chatRoomId: chatRoomId,
        content,
      };
      clientSelector.publish({
        destination: "/pub/chat/message",
        body: JSON.stringify(chatMessage),
      }); // 메시지 전송
      setContent("");
    } else {
      console.error("웹소켓 연결이 활성화되지 않았습니다.");
    }
  };

  const convertDate = (date: number) => {
    if (date < 10) {
      return `0${date}`;
    } else return date;
  };

  useEffect(() => {
    if (notification) {
      const dateString = notification.createdDate;
      const luxonDateTime = DateTime.fromISO(dateString, {
        zone: "Asia/Seoul",
      });
      const date = luxonDateTime.toJSDate();
      setNotificationDate(date);
    }
  }, [notification]);

  return (
    <div className={styles.chatComponent}>
      <div className={styles.messageList}>
        {!isPageEnd && (
          <div ref={target} style={{ width: "100%", height: "5px" }} />
        )}
        <div>
          {messageList?.data.chatMessages.content.map((message) => {
            const date = new Date(message.createdAt);
            return (
              <div key={message.chatMessageId} className={styles.speechBubble}>
                {message.myMsg ? (
                  <div style={{ justifyContent: "right" }}>
                    <div style={{ textAlign: "right" }}>
                      {message.readCount !== 0 && (
                        <span className={styles.readCount}>
                          {message.readCount}
                        </span>
                      )}
                      <span className={styles.sendTime}>{`${convertDate(
                        date.getHours()
                      )}:${convertDate(date.getMinutes())}`}</span>
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
                      {message.readCount !== 0 && (
                        <span className={styles.readCount}>
                          {message.readCount}
                        </span>
                      )}
                      <span className={styles.sendTime}>{`${convertDate(
                        date.getHours()
                      )}:${convertDate(date.getMinutes())}`}</span>
                    </div>
                  </div>
                )}
              </div>
            );
          })}
        </div>
        {chatMessageList.map((el) => {
          const dateString = el.createdAt;
          const luxonDateTime = DateTime.fromISO(dateString, {
            zone: "Asia/Seoul",
          });
          const date = luxonDateTime.toJSDate();
          return (
            <div key={el.createdAt} className={styles.speechBubble}>
              {el?.senderId === messageList?.data.memberId ? (
                <div style={{ justifyContent: "right" }}>
                  <div style={{ textAlign: "right" }}>
                    {el?.readCount !== 0 && (
                      <span className={styles.readCount}>
                        {notificationDate &&
                        notification?.notificationType ===
                          "READ_COUNT_UPDATE" &&
                        notificationDate > date
                          ? ""
                          : el?.readCount}
                      </span>
                    )}
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
                    {el?.readCount !== 0 && (
                      <span className={styles.readCount}>{el?.readCount}</span>
                    )}
                    <span className={styles.sendTime}>{`${convertDate(
                      date.getHours()
                    )}:${convertDate(date.getMinutes())}`}</span>
                  </div>
                </div>
              )}
            </div>
          );
        })}
        <div ref={messageEndRef} style={{ width: "100%", height: "5px" }} />
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
