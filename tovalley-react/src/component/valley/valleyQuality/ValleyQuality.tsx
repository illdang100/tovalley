import React, { FC } from "react";
import styles from "../../../css/valley/ValleyQuality.module.css";
import ValleyQualityChart from "./ValleyQualityChart";
import { AiOutlineInfoCircle } from "react-icons/ai";

interface Props {
  waterPlaceDetails: {
    waterPlaceImage: string | null;
    waterPlaceName: string;
    latitude: number;
    longitude: number;
    managementType: string;
    detailAddress: string;
    town: string;
    annualVisitors: number;
    safetyMeasures: number | string;
    waterPlaceSegment: number;
    dangerSegments: number | string;
    dangerSignboardsNum: number | string;
    deepestDepth: number;
    avgDepth: number;
    waterTemperature: number;
    bod: number;
    turbidity: number;
    waterQualityReviews: {
      깨끗해요: number;
      괜찮아요: number;
      더러워요: number;
    };
  };
}

const ValleyQuality: FC<Props> = ({ waterPlaceDetails }) => {
  return (
    <div className={styles.valleyQuality}>
      <span>계곡 수질</span>
      <div className={styles.valleyQualityContent}>
        <div className={styles.qualityDetail}>
          <div className={styles.qualityGauge}>
            <div className={styles.gaugeTitle}>
              <span>수질정도</span>
              <span>
                <AiOutlineInfoCircle color="#7BA5F6" />
              </span>
            </div>
            <div className={styles.gaugeBarList}>
              <div className={styles.bodGauge}>
                <span>BOD(mg/L)</span>
                <div className={styles.bodGaugeDetail}>
                  <div className={styles.bodGaugeBar}></div>
                  <span>10</span>
                  <span>0</span>
                </div>
              </div>
              <div className={styles.ntuGauge}>
                <span>탁도(NTU)</span>
                <div className={styles.ntuGaugeDetail}>
                  <div className={styles.ntuGaugeBar}></div>
                  <span>101</span>
                  <span>0</span>
                </div>
              </div>
            </div>
          </div>
          <div className={styles.valleyWaterInfo}>
            <div className={styles.average}>
              <span>물놀이 구간 ({waterPlaceDetails.waterPlaceSegment}m)</span>
              <span>평균 수심</span>
            </div>
            <div className={styles.blank} />
            <div className={styles.danger}>
              <span>위험 구간 ({waterPlaceDetails.dangerSegments}m)</span>
              <span>가장 깊은 곳</span>
            </div>
          </div>
          <div className={styles.valleyWater}>
            <div className={styles.avgDepth}>
              <span>{waterPlaceDetails.avgDepth}m</span>
              <div></div>
            </div>
            <div className={styles.waterTemperature}>
              <span>수온</span>
              <div>
                <span>{waterPlaceDetails.waterTemperature}</span>
                <span>°C</span>
              </div>
            </div>
            <div className={styles.deepestDepth}>
              <span>{waterPlaceDetails.deepestDepth}m</span>
              <div></div>
            </div>
          </div>
        </div>
        <div className={styles.valleyQualityReview}>
          <ValleyQualityChart
            waterQualityReviews={waterPlaceDetails.waterQualityReviews}
          />
        </div>
      </div>
    </div>
  );
};

export default ValleyQuality;
