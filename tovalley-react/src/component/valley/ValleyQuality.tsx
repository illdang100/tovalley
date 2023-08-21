import React from "react";
import styles from "../../css/valley/ValleyQuality.module.css";

const ValleyQuality = () => {
  return (
    <div className={styles.valleyQuality}>
      <span>계곡 수질</span>
      <div className={styles.qualityDetail}>
        <div className={styles.qualityGauge}>
          <span>수질정도</span>
          <div className={styles.gaugeBar}></div>
        </div>
        <div className={styles.valleyWater}>
          <span></span>
        </div>
      </div>
    </div>
  );
};

export default ValleyQuality;
