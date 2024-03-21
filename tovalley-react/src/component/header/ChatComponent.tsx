import React, { useCallback, useEffect, useRef, useState } from "react";
import styles from "../../css/header/ChatComponent.module.css";
import axiosInstance from "../../axios_interceptor";
import { MessageListType, MessageType } from "../../typings/db";
import { useDispatch, useSelector } from "react-redux";
import { RootState } from "../../store/store";
import { DateTime } from "luxon";
import { setSubscription } from "../../store/chat/subscriptionSlice";
import { MdImage } from "react-icons/md";
import { useSaveImg } from "../../composables/imgController";
import { IoIosArrowDown } from "react-icons/io";

const ChatComponent = () => {
  const [messageList, setMessageList] = useState<MessageListType>();
  const [newMessageList, setNewMessageList] = useState<MessageListType>();
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
  const { uploadImg, imgFiles, saveImgFile, handleDeleteImage } = useSaveImg();
  const [newMessageView, setNewMessageView] = useState(false);

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
    target.current?.scrollIntoView();
  }, [newMessageList]);

  useEffect(() => {
    if (message?.senderId === messageList?.data.memberId) {
      messageEndRef.current?.scrollIntoView();
      setNewMessageView(false);
    }

    if (reqMsg && message?.senderId !== messageList?.data.memberId)
      setNewMessageView(true);

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
          cursor: id,
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
    setNewMessageList(res.data);
    if (res.data.data.chatMessages.last) {
      setIsPageEnd(true);
    }
  };

  useEffect(() => {
    if (newMessageList && messageList)
      setMessageList({
        data: {
          memberId: newMessageList.data.memberId,
          chatRoomId: newMessageList.data.chatRoomId,
          chatMessages: {
            content: [
              ...newMessageList.data.chatMessages.content,
              ...messageList.data.chatMessages.content,
            ],
            pageable: {
              sort: {
                empty: newMessageList.data.chatMessages.pageable.sort.empty,
                sorted: newMessageList.data.chatMessages.pageable.sort.sorted,
                unsorted:
                  newMessageList.data.chatMessages.pageable.sort.unsorted,
              },
              offset: newMessageList.data.chatMessages.pageable.offset,
              pageNumber: newMessageList.data.chatMessages.pageable.pageNumber,
              pageSize: newMessageList.data.chatMessages.pageable.pageSize,
              paged: newMessageList.data.chatMessages.pageable.paged,
              unpaged: newMessageList.data.chatMessages.pageable.unpaged,
            },
            first: newMessageList.data.chatMessages.first,
            last: newMessageList.data.chatMessages.last,
            size: newMessageList.data.chatMessages.size,
            number: newMessageList.data.chatMessages.number,
            sort: {
              empty: newMessageList.data.chatMessages.sort.empty,
              sorted: newMessageList.data.chatMessages.sort.sorted,
              unsorted: newMessageList.data.chatMessages.sort.unsorted,
            },
            numberOfElements: newMessageList.data.chatMessages.numberOfElements,
            empty: newMessageList.data.chatMessages.empty,
          },
        },
      });
  }, [newMessageList]);

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

  const handleEndObserver = useCallback(
    async (
      [entry]: IntersectionObserverEntry[],
      observer: IntersectionObserver
    ) => {
      if (entry.isIntersecting) {
        observer.unobserve(entry.target);
        setReqMsg(false);
        setNewMessageView(false);
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

  useEffect(() => {
    if (!messageEndRef.current) return;

    const option = {
      root: null,
      rootMargin: "0px",
      threshold: 0,
    };

    const observer = new IntersectionObserver(handleEndObserver, option);

    messageEndRef.current && observer.observe(messageEndRef.current);

    return () => observer && observer.disconnect();
  }, [handleEndObserver]);

  const sendMessage = () => {
    if (clientSelector?.connected && content !== "") {
      const chatMessage = {
        chatRoomId: chatRoomId,
        content,
      };
      clientSelector.publish({
        destination: "/pub/chat/message",
        body: JSON.stringify(chatMessage),
      }); // 메시지 전송
      setContent("");
    } else if (content === "") {
      console.error("채팅 메시지가 비었습니다.");
    } else {
      console.error("웹소켓 연결이 활성화되지 않았습니다.");
    }
  };

  const sendImageMessage = () => {
    if (clientSelector?.connected) {
      const formData = new FormData();
      formData.append("image", imgFiles[0]);

      axiosInstance
        .post("/api/auth/chat/images/upload", formData)
        .then((res) => {
          console.log(res);
          const chatMessage = {
            chatRoomId: chatRoomId,
            content: "",
            chatType: "IMAGE",
            imageName: res.data.data.storeFileName,
            imageUrl: res.data.data.storeFileUrl,
          };
          clientSelector.publish({
            destination: "/pub/chat/message",
            body: JSON.stringify(chatMessage),
          }); // 메시지 전송
          setContent("");
          handleDeleteImage(0);
        });
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

  const confirmNewMessage = () => {
    setNewMessageView(false);
    messageEndRef.current?.scrollIntoView();
  };

  useEffect(() => {
    setNewMessageView(false);
  }, [uploadImg]);

  return (
    <div className={styles.chatComponent}>
      <div className={styles.messageList}>
        <div>
          {messageList?.data.chatMessages.content.map((message, index) => {
            const date = new Date(message.createdAt);
            return (
              <div key={message.chatMessageId} className={styles.speechBubble}>
                {message.myMsg ? (
                  <div style={{ justifyContent: "right" }}>
                    <div style={{ textAlign: "right" }}>
                      {message.readCount !== 0 && !chatRoomId && (
                        <span className={styles.readCount}>
                          {message.readCount}
                        </span>
                      )}
                      <span className={styles.sendTime}>{`${convertDate(
                        date.getHours()
                      )}:${convertDate(date.getMinutes())}`}</span>
                    </div>
                    <div className={styles.mySpeechBubble}>
                      {message.chatType === "IMAGE" ? (
                        <img
                          className={styles.chatImage}
                          src={message.imageUrl ? message.imageUrl : ""}
                          alt="chatImage"
                        />
                      ) : (
                        message.content
                      )}
                    </div>
                  </div>
                ) : (
                  <div style={{ justifyContent: "left" }}>
                    <div className={styles.opponentSpeechBubble}>
                      {message.chatType === "IMAGE" ? (
                        <img
                          className={styles.chatImage}
                          src={message.imageUrl ? message.imageUrl : ""}
                          alt="chatImage"
                        />
                      ) : (
                        message.content
                      )}
                    </div>
                    <div>
                      {message.readCount !== 0 && !chatRoomId && (
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
                {!isPageEnd && index === 0 && (
                  <div
                    ref={target}
                    style={{ width: "100%", height: "0.1px" }}
                  />
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
                  <div className={styles.mySpeechBubble}>
                    {el?.chatType === "IMAGE" ? (
                      <img
                        className={styles.chatImage}
                        src={el.imageUrl ? el.imageUrl : ""}
                        alt="chatImage"
                      />
                    ) : (
                      el?.content
                    )}
                  </div>
                </div>
              ) : (
                <div style={{ justifyContent: "left" }}>
                  <div className={styles.opponentSpeechBubble}>
                    {el?.chatType === "IMAGE" ? (
                      <img
                        className={styles.chatImage}
                        src={el.imageUrl ? el.imageUrl : ""}
                        alt="chatImage"
                      />
                    ) : (
                      el?.content
                    )}
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
        <div ref={messageEndRef} style={{ width: "100%", height: "10px" }} />
      </div>
      {newMessageView && (
        <div className={styles.newMessageContainer}>
          <div className={styles.newMessage} onClick={confirmNewMessage}>
            <p>
              {message?.content === ""
                ? "사진을 보냈습니다."
                : message?.content}
            </p>
            <span>
              <IoIosArrowDown />
            </span>
          </div>
        </div>
      )}
      <div className={styles.sendMessage}>
        <div>
          {uploadImg.length !== 0 && (
            <div className={styles.previewImg}>
              {uploadImg.map((image, id) => (
                <div key={id} className={styles.imageContainer}>
                  <div>
                    <img src={`${image}`} alt={`${image}-${id}`} />
                  </div>
                </div>
              ))}
            </div>
          )}
        </div>
        <div style={{ backgroundColor: "white" }}>
          <input
            type="text"
            placeholder="메시지 보내기"
            value={content}
            onChange={(e) => setContent(e.target.value)}
            readOnly={imgFiles.length !== 0}
            maxLength={200}
          />
          <span className={styles.imageIcon}>
            <label htmlFor="input-file">
              <input
                className={styles.imgInput}
                type="file"
                accept="image/*"
                id="input-file"
                multiple
                onChange={saveImgFile}
              />
              <MdImage />
            </label>
          </span>
          <button
            onClick={() => {
              if (imgFiles.length !== 0) {
                sendImageMessage();
              } else sendMessage();
            }}
          >
            전송
          </button>
        </div>
      </div>
    </div>
  );
};

export default ChatComponent;
