import React, { FC, useState } from "react";
import styles from "../../css/main/Report.module.css";
import { HiSun } from "react-icons/hi";

interface Props {
  alert: {
    weatherAlerts: {
      weatherAlertType: string;
      title: string;
      announcementTime: string;
      effectiveTime: string;
      content: string;
    }[];
    weatherPreAlerts: {
      announcementTime: string;
      title: string;
      weatherAlertType: string;
      contents: {
        content: string;
      }[];
    }[];
  };
}

const Report: FC<Props> = ({ alert }) => {
  const [report, setReport] = useState("특보");

  return (
    <div className={styles.report}>
      <div className={styles.menu}>
        <span
          style={
            report === "특보"
              ? {
                  backgroundColor: "#66A5FC",
                  color: "white",
                }
              : {}
          }
          onClick={() => setReport("특보")}
        >
          특보
        </span>
        <span
          style={
            report === "예비특보"
              ? {
                  backgroundColor: "#66A5FC",
                  color: "white",
                }
              : {}
          }
          onClick={() => setReport("예비특보")}
        >
          예비특보
        </span>
      </div>
      {report === "특보" ? (
        <div className={styles.reportList}>
          {alert.weatherAlerts.map((item) => {
            return (
              <div className={styles.reportItem}>
                <div className={styles.reportTitle}>
                  <span>
                    <HiSun size="20px" />
                  </span>
                  <span>{item.title}</span>
                </div>
                <div className={styles.reportContent}>
                  <div className={styles.presentation}>
                    <span>발표</span>
                    <span>{item.announcementTime}</span>
                  </div>
                  <div className={styles.presentation}>
                    <span>발효</span>
                    <span>{item.effectiveTime}</span>
                  </div>
                  <div className={styles.region}>
                    <span>{item.content}</span>
                  </div>
                </div>
              </div>
            );
          })}
        </div>
      ) : (
        <div className={styles.reportList}>
          {alert.weatherPreAlerts.map((item) => {
            return (
              <div className={styles.reportItem}>
                <div className={styles.reportTitle}>
                  <span>
                    <HiSun size="20px" />
                  </span>
                  <span>{item.title}</span>
                </div>
                <div className={styles.reportContent}>
                  <div className={styles.presentation}>
                    <span>발표</span>
                    <span>{item.announcementTime}</span>
                  </div>
                  {item.contents.map((content) => {
                    return (
                      <div className={styles.region}>
                        <span>{content.content}</span>
                      </div>
                    );
                  })}
                </div>
              </div>
            );
          })}
        </div>
      )}
    </div>
  );
};

export default Report;
