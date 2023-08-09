import React from "react";
import styles from "../../css/main/Report.module.css";
import { HiSun } from "react-icons/hi";

const report = [
  {
    reportMenu: "폭염특보",
    presentation: "2023.07.28 16:00",
    effectuation: "2023.07.28 16:00 이후",
    region:
      "인천, 남양주, 양양평지, 정선평지, 인제평지, 강원북부산지, 대전, 대구, 구미",
  },
  {
    reportMenu: "폭염특보",
    presentation: "2023.07.30 16:00",
    effectuation: "2023.07.30 16:00 이후",
    region:
      "인천, 남양주, 양양평지, 정선평지, 인제평지, 강원북부산지, 대전, 대구, 구미",
  },
];

const Report = () => {
  return (
    <div className={styles.report}>
      <div className={styles.menu}>
        <span>폭염</span>
        <span>호우</span>
        <span>태풍</span>
      </div>
      <div className={styles.reportList}>
        {report.map((item) => {
          return (
            <div className={styles.reportItem}>
              <div className={styles.reportTitle}>
                <span>
                  <HiSun size="20px" />
                </span>
                <span>{item.reportMenu}</span>
              </div>
              <div className={styles.reportContent}>
                <div className={styles.presentation}>
                  <span>발표</span>
                  <span>{item.presentation}</span>
                </div>
                <div className={styles.presentation}>
                  <span>발효</span>
                  <span>{item.effectuation}</span>
                </div>
                <div className={styles.region}>
                  <span>{item.region}</span>
                </div>
              </div>
            </div>
          );
        })}
      </div>
    </div>
  );
};

export default Report;
