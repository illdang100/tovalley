import styles from "../../../css/main/Accident.module.css";
import { MdNavigateNext, MdNavigateBefore } from "react-icons/md";
import AccidentChart from "./AccidentChart";
import React, { FC, useRef, useState } from "react";
import axios from "axios";

const localhost = "http://13.125.136.237";

interface Props {
  accident: {
    accidentCountPerMonth: {
      month: number;
      deathCnt: number;
      disappearanceCnt: number;
      injuryCnt: number;
    }[];
    province: string;
    totalDeathCnt: number;
    totalDisappearanceCnt: number;
    totalInjuryCnt: number;
  };
  setRegionAccident: React.Dispatch<
    React.SetStateAction<{
      accidentCountPerMonth: {
        month: number;
        deathCnt: number;
        disappearanceCnt: number;
        injuryCnt: number;
      }[];
      province: string;
      totalDeathCnt: number;
      totalDisappearanceCnt: number;
      totalInjuryCnt: number;
    }>
  >;
}

const Accident: FC<Props> = ({ accident, setRegionAccident }) => {
  const region: { ko: string; en: string }[] = [
    {
      ko: "전국",
      en: "NATIONWIDE",
    },
    {
      ko: "서울",
      en: "SEOUL",
    },
    {
      ko: "경기",
      en: "GYEONGGI",
    },
    {
      ko: "세종",
      en: "SEJONG",
    },
    {
      ko: "강원",
      en: "GANGWON",
    },
    {
      ko: "충청",
      en: "CHUNGCHEONG",
    },
    {
      ko: "전라",
      en: "JEOLLA",
    },
    {
      ko: "광주",
      en: "GWANGJU",
    },
    {
      ko: "경상",
      en: "GYEONGSANG",
    },
    {
      ko: "부산",
      en: "BUSAN",
    },
    {
      ko: "울산",
      en: "ULSAN",
    },
    {
      ko: "제주",
      en: "JEJU",
    },
  ];
  const [clicked, setClicked] = useState(accident.province);
  const [next, setNext] = useState(false);

  const scroll = useRef<HTMLDivElement>(null);
  const scrollPrev = useRef<HTMLDivElement>(null);
  const onMove = () => {
    scroll.current?.scrollIntoView({ behavior: "smooth", block: "end" });
  };
  const onMovePrev = () => {
    scrollPrev.current?.scrollIntoView({ behavior: "smooth", block: "end" });
  };

  const handleNext = () => {
    setNext(true);
    onMove();
  };

  const handlePrev = () => {
    setNext(false);
    onMovePrev();
  };

  const getRegionAccident = (region: { ko: string; en: string }) => {
    const config = {
      params: {
        province: region.en,
      },
    };
    axios
      .get(`${localhost}/api/main-page/accidents`, config)
      .then((res) => {
        console.log(res);
        setRegionAccident(res.data.data);
      })
      .catch((err) => {
        console.log(err);
      });
  };

  return (
    <div className={styles.accident}>
      <h4>올해 사고 발생수</h4>
      <div className={styles.regionNav}>
        <div className={styles.regionMenu}>
          {region.map((item, index) => {
            return (
              <div>
                {index === 0 && (
                  <span ref={scrollPrev} className={styles.blank}>
                    ㅤ
                  </span>
                )}
                <span
                  onClick={() => {
                    setClicked(`${item.ko}`);
                    getRegionAccident(item);
                  }}
                  style={clicked === item.ko ? { color: "#66A5FC" } : {}}
                >
                  {item.ko}
                </span>
                {index === region.length - 1 && (
                  <span ref={scroll} className={styles.blank}>
                    ㅤ
                  </span>
                )}
              </div>
            );
          })}
          {next ? (
            <span className={styles.regionPrevBtn} onClick={handlePrev}>
              <MdNavigateBefore color="#66A5FC" size="30px" />
            </span>
          ) : (
            <span className={styles.regionNextBtn} onClick={handleNext}>
              <MdNavigateNext color="#66A5FC" size="30px" />
            </span>
          )}
        </div>
      </div>
      <div className={styles.lineGraph}>
        <AccidentChart accidentCnt={accident.accidentCountPerMonth} />
      </div>
      <div className={styles.graph}>
        <div className={styles.graphTitle}>
          <span>사망</span>
          <span>실종</span>
          <span>부상</span>
        </div>
        <div className={styles.graphContent}>
          <span>{accident.totalDeathCnt}</span>
          <span>{accident.totalDisappearanceCnt}</span>
          <span>{accident.totalInjuryCnt}</span>
        </div>
      </div>
    </div>
  );
};

export default Accident;
