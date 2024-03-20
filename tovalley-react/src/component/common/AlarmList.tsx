import React, { useCallback, useEffect, useRef, useState } from "react";
import axiosInstance from "../../axios_interceptor";
import { AlarmListType } from "../../typings/db";
import styles from "../../css/common/AlarmList.module.css";
import { MdClose } from "react-icons/md";
import { elapsedTime } from "../../composables/elapsedTime";
import { useDispatch, useSelector } from "react-redux";
import { RootState } from "../../store/store";
import { Cookies } from "react-cookie";
import { view } from "../../store/chat/chatViewSlice";
import { enterChatRoom } from "../../store/chat/chatRoomIdSlice";

const cookies = new Cookies();

const AlarmList = () => {
  const [alarmList, setAlarmList] = useState<AlarmListType[]>([]);
  const [newAlarmList, setNewAlarmList] = useState<AlarmListType[]>([]);
  const [slide, setSlide] = useState("");
  const [bgForeground, setBgForeground] = useState(false);
  const notificationView = useSelector(
    (state: RootState) => state.notificationView.value
  );
  const target = useRef<HTMLDivElement>(null);
  const [isPageEnd, setIsPageEnd] = useState<boolean>(false);
  const notification = useSelector(
    (state: RootState) => state.notification.value
  );
  const [loading, setLoading] = useState(false);
  const dispatch = useDispatch();

  useEffect(() => {
    if (notificationView) setSlide("start");
    else setSlide("end");
  }, [notificationView]);

  useEffect(() => {
    if (slide === "end") {
      const fadeTimer = setTimeout(() => {
        setBgForeground(true);
      }, 500);
      return () => {
        clearTimeout(fadeTimer);
      };
    } else {
      setBgForeground(false);
    }
  }, [slide]);

  const getAlarmList = useCallback(
    async (id?: number) => {
      const loginStatus = cookies.get("ISLOGIN");
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

      if (loginStatus && notificationView) {
        try {
          setLoading(true);
          const res = await axiosInstance.get(
            "/api/auth/notifications",
            config
          );
          console.log(res);
          setNewAlarmList(res.data.data.content);
          if (res.data.data.content.length < 5 || res.data.data.last) {
            setIsPageEnd(true);
          }
          setLoading(false);
        } catch (err) {
          console.log(err);
        }
      } else {
        setAlarmList([]);
      }
    },
    [notificationView]
  );

  useEffect(() => {
    getAlarmList();
  }, [notificationView, getAlarmList]);

  useEffect(() => {
    if (notificationView) setAlarmList([...alarmList, ...newAlarmList]);
  }, [newAlarmList]);

  useEffect(() => {
    if (
      notification &&
      notificationView &&
      !loading &&
      notification.notificationType === "CHAT"
    )
      setAlarmList([
        {
          chatNotificationId: alarmList[0].chatNotificationId + 1,
          chatRoomId: notification.chatRoomId,
          senderNick: notification.senderNick,
          createdDate: notification.createdDate,
          content: notification.content,
          hasRead: false,
        },
        ...alarmList,
      ]);
  }, [notification]);

  const handleObserver = useCallback(
    async (
      [entry]: IntersectionObserverEntry[],
      observer: IntersectionObserver
    ) => {
      if (entry.isIntersecting) {
        observer.unobserve(entry.target);
        if (alarmList.length > 0)
          await getAlarmList(
            alarmList[alarmList.length - 1].chatNotificationId
          );
      }
    },
    [getAlarmList, alarmList]
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

  const deleteAlarm = (id: number) => {
    console.log(alarmList);
    const deleteAlarmList = alarmList?.filter((alarm) => {
      return alarm.chatNotificationId !== id;
    });
    setAlarmList(deleteAlarmList);
    console.log(alarmList);
    axiosInstance.delete(`/api/auth/notifications/${id}`).then((res) => {
      console.log(res);
    });
  };

  const deleteAllAlarm = () => {
    setAlarmList([]);
    axiosInstance.delete("/api/auth/notifications").then((res) => {
      console.log(res);
    });
  };

  const startChat = (id: number) => {
    dispatch(view(true));
    dispatch(enterChatRoom(id));
  };

  return (
    <div
      className={`${styles.alarmListContainer} ${
        bgForeground ? "" : styles.zIndex
      } `}
    >
      <div
        className={`${styles.alarmListWrap} ${
          slide === "start"
            ? styles.startSlideAnimation
            : slide === "end"
            ? styles.endSlideAnimation
            : ""
        }`}
      >
        <span className={styles.allDelete} onClick={deleteAllAlarm}>
          모두 지우기
        </span>
        {alarmList?.map((alarm) => (
          <div
            key={alarm.chatNotificationId}
            className={styles.alarmComponent}
            onClick={() => startChat(alarm.chatRoomId)}
          >
            <span
              className={styles.closeBtn}
              onClick={() => deleteAlarm(alarm.chatNotificationId)}
            >
              <MdClose />
            </span>
            <h4 className={alarm.hasRead ? styles.hasRead : ""}>
              {alarm.senderNick}
            </h4>
            <p
              className={`${styles.alarmContent} ${
                alarm.hasRead ? styles.hasRead : ""
              }`}
            >
              {alarm.content === "" ? "사진을 보냈습니다." : alarm.content}
            </p>
            {alarm.createdDate && (
              <span className={styles.alarmTime}>
                {elapsedTime(alarm.createdDate)}
              </span>
            )}
          </div>
        ))}
        {!isPageEnd && (
          <div ref={target} style={{ width: "100%", height: "5px" }} />
        )}
      </div>
    </div>
  );
};

export default AlarmList;
