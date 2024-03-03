import React, { FC, useEffect, useState } from "react";
import styles from "../../css/common/Alarm.module.css";
import { MdClose } from "react-icons/md";
import { NotificationType } from "../../typings/db";
import { elapsedTime } from "../../composables/elapsedTime";

const Alarm: FC<{
  alarm: NotificationType | null;
  setAlarm: React.Dispatch<React.SetStateAction<NotificationType | null>>;
}> = ({ alarm, setAlarm }) => {
  const [fade, setFade] = useState(true);
  useEffect(() => {
    const fadeTimer = setTimeout(() => {
      setFade(false);
    }, 2700);
    const timer = setTimeout(() => {
      setAlarm(null);
    }, 3000);
    return () => {
      clearTimeout(timer);
      clearTimeout(fadeTimer);
    };
  }, [alarm, setAlarm]);

  return (
    <div
      className={`${styles.alarmComponent} ${
        fade ? styles.fadein : styles.fadeout
      }`}
    >
      <span className={styles.closeBtn}>
        <MdClose />
      </span>
      <h4>{alarm?.senderNick}</h4>
      <p className={styles.alarmContent}>{alarm?.content}</p>
      {alarm?.createdDate && (
        <span className={styles.alarmTime}>
          {elapsedTime(alarm.createdDate)}
        </span>
      )}
    </div>
  );
};

export default Alarm;
