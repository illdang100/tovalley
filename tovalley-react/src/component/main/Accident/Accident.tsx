import styles from "../../../css/main/Accident.module.css";
import { MdNavigateNext, MdNavigateBefore } from "react-icons/md";
import AccidentChart from "./AccidentChart";
import React, { useRef, useState } from "react";

const Accident = () => {
  const region: string[] = [
    "전국",
    "서울",
    "경기",
    "강원",
    "전라",
    "경상",
    "부산",
    "제주",
    "충청",
    "대구",
  ];
  const [clicked, setClicked] = useState("전국");
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
                    setClicked(`${item}`);
                  }}
                  style={clicked === item ? { color: "#66A5FC" } : {}}
                >
                  {item}
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
        <AccidentChart />
      </div>
      <div className={styles.graph}>
        <div className={styles.graphTitle}>
          <span>사망</span>
          <span>실종</span>
          <span>부상</span>
        </div>
        <div className={styles.graphContent}>
          <span>19</span>
          <span>19</span>
          <span>19</span>
        </div>
      </div>
    </div>
  );
};

export default Accident;
