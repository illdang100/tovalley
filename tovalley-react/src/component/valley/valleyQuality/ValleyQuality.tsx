import React, { FC, useState } from "react";
import styles from "../../../css/valley/ValleyQuality.module.css";
import ValleyQualityChart from "./ValleyQualityChart";
import { AiOutlineInfoCircle } from "react-icons/ai";

interface Props {
  waterPlaceDetails: {
    waterPlaceImage: string | null;
    waterPlaceName: string;
    latitude: string;
    longitude: string;
    managementType: string;
    detailAddress: string;
    town: string;
    annualVisitors: string;
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
  const [helpInfo, setHelpInfo] = useState(false);

  return (
    <div className={styles.valleyQuality}>
      <span>계곡 수질</span>
      <div className={styles.valleyQualityContent}>
        {helpInfo && <HelpInfo />}
        <div className={styles.qualityDetail}>
          <div className={styles.qualityGauge}>
            <div className={styles.gaugeTitle}>
              <span>수질정도</span>
              <span onClick={() => setHelpInfo(!helpInfo)}>
                <AiOutlineInfoCircle color="#7BA5F6" />
              </span>
            </div>
            <div className={styles.gaugeBarList}>
              <div className={styles.bodGauge}>
                <span>BOD(mg/L)</span>
                <div className={styles.bodGaugeDetail}>
                  <div className={styles.bodGaugeBar}>
                    {waterPlaceDetails.bod && (
                      <div
                        style={{ bottom: `${waterPlaceDetails.bod * 10}px` }}
                      />
                    )}
                  </div>
                  <span>10↑</span>
                  {waterPlaceDetails.bod > 0 && waterPlaceDetails.bod < 10 && (
                    <span
                      style={{
                        bottom: `${waterPlaceDetails.bod * 10 - 5}px`,
                      }}
                    >
                      {waterPlaceDetails.bod}
                    </span>
                  )}
                  <span>0</span>
                </div>
              </div>
              <div className={styles.ntuGauge}>
                <span>탁도(NTU)</span>
                <div className={styles.ntuGaugeDetail}>
                  <div className={styles.ntuGaugeBar}>
                    {waterPlaceDetails.turbidity && (
                      <div
                        style={{ bottom: `${waterPlaceDetails.turbidity}px` }}
                      />
                    )}
                  </div>
                  <span>101↑</span>
                  {waterPlaceDetails.turbidity > 0 &&
                    waterPlaceDetails.turbidity < 101 && (
                      <span
                        style={{
                          bottom: `${waterPlaceDetails.turbidity - 5}px`,
                        }}
                      >
                        {waterPlaceDetails.turbidity}
                      </span>
                    )}
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
          <h4>사용자 수질 평가</h4>
          <ValleyQualityChart
            waterQualityReviews={waterPlaceDetails.waterQualityReviews}
          />
        </div>
      </div>
    </div>
  );
};

const HelpInfo = () => {
  return (
    <div className={styles.helpInfo}>
      <div>
        <span>BOD(mg/L)</span>
        <span>
          <br />물 속의 유기 물질 분해 및 분해 과정에서 소비되는 산소 양을
          나타내는 지표입니다. <br /> BOD가 높다는 것은 그 물 속에 분해되기 쉬운
          유기물이 많음을 의미하므로 수질이 나쁘다는 것을 뜻 합니다. <br />
          수질 및 수생태계 환경기준에 따르면 BOD 3㎎/L 이하를 ‘좋은 물’이라 할
          수 있습니다.
        </span>
      </div>
      <div>
        <span>탁도(NTU)</span>
        <span>
          <br />물 속의 입자나 부유 물질로 인해 물이 얼마나 흐려지는지를
          나타내는 지표입니다. <br /> • 0-5 NTU : 매우 깨끗하고 맑은 물로,
          물체가 선명하게 보입니다. <br /> • 6-10 NTU : 물은 여전히 매우
          투명하지만 약간의 탁함이 느껴질 수 있습니다. <br /> • 11-20 NTU : 물이
          다소 탁하지만 일반적으로 명확한 물 상태입니다.
          <br />
          • 21-50 NTU : 물이 조금 탁해져 물체의 가시성이 낮아질 수 있습니다.
          <br />
          • 51-100 NTU : 물이 상당히 탁해져 물체의 가시성이 매우 낮아지며, 탁한
          느낌을 받을 수 있습니다. <br />• 101 NTU 이상 : 물이 매우 탁해져 거의
          모든 물체가 보이지 않을 정도로 가시성이 매우 낮습니다.
        </span>
      </div>
    </div>
  );
};

export default ValleyQuality;
