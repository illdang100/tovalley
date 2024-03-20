import React, { useEffect, useState } from "react";
import styles from "../../css/common/Alarm.module.css";
import { elapsedTime } from "../../composables/elapsedTime";
import { useDispatch, useSelector } from "react-redux";
import { RootState } from "../../store/store";
import { setNotification } from "../../store/notification/notificationSlice";
import { view } from "../../store/chat/chatViewSlice";
import { enterChatRoom } from "../../store/chat/chatRoomIdSlice";

const Alarm = () => {
  const notification = useSelector(
    (state: RootState) => state.notification.value
  );
  const notificationView = useSelector(
    (state: RootState) => state.notificationView.value
  );
  const chatView = useSelector((state: RootState) => state.view.value);
  const [fade, setFade] = useState(true);
  const dispatch = useDispatch();

  useEffect(() => {
    console.log(notification);
    const fadeTimer = setTimeout(() => {
      setFade(false);
    }, 2700);
    const timer = setTimeout(() => {
      dispatch(setNotification(null));
    }, 3000);
    return () => {
      clearTimeout(fadeTimer);
      clearTimeout(timer);
    };
  }, []);

  const startChat = () => {
    dispatch(view(true));
    dispatch(enterChatRoom(notification?.chatRoomId));
  };

  return (
    <div className={styles.alarmWrap}>
      {/* {!chatView || !chatRoomId ? (
        <span
          className={styles.closeBtn}
          onClick={() => {
            dispatch(setNotification(null));
          }}
        >
          <MdClose />
        </span>
      ) : (
        ""
      )} */}
      <div
        className={`${styles.alarmComponent} ${
          fade ? styles.fadein : styles.fadeout
        }`}
        style={notificationView || chatView ? { display: "none" } : {}}
        onClick={startChat}
      >
        <h4>{notification?.senderNick}</h4>
        <p className={styles.alarmContent}>
          {notification?.content === ""
            ? "사진을 보냈습니다."
            : notification?.content}
        </p>
        {notification?.createdDate && (
          <span className={styles.alarmTime}>
            {elapsedTime(notification.createdDate)}
          </span>
        )}
      </div>
    </div>
  );
};

export default Alarm;
