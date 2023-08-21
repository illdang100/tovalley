import React from "react";
import styles from "../../../css/valley/ValleyInfo.module.css";
import { TbChartDonut4, TbJumpRope } from "react-icons/tb";
import { MdEmojiPeople, MdHomeRepairService } from "react-icons/md";
import { FaVest } from "react-icons/fa";
import { LuUtilityPole } from "react-icons/lu";

const ValleyInfo = () => {
  return (
    <div>
      <div className={styles.title}>
        <span>금오계곡</span>
      </div>
      <div className={styles.valleyInfo}>
        <div className={styles.valleyMap}>
          <div className={styles.valleyPlaceMenu}>
            <span>계곡위치</span>
            <span>병원</span>
            <span>약국</span>
          </div>
          <div className={styles.valleyPlace}>google Map</div>
        </div>
        <div className={styles.valleyDetail}>
          <div className={styles.valleyWeather}>
            <span>경상북도 구미시 날씨 ☁️</span>
            <div className={styles.weatherList}>
              <div className={styles.weatherItem}>
                <span>☀️</span>
                <div className={styles.temperature}>
                  <span>35</span>
                  <span> / </span>
                  <span>37</span>
                </div>
                <span>토</span>
                <span>오늘</span>
              </div>
              <div className={styles.weatherItem}>
                <span>☀️</span>
                <div className={styles.temperature}>
                  <span>35</span>
                  <span> / </span>
                  <span>37</span>
                </div>
                <span>토</span>
                <span>오늘</span>
              </div>
              <div className={styles.weatherItem}>
                <span>☀️</span>
                <div className={styles.temperature}>
                  <span>35</span>
                  <span> / </span>
                  <span>37</span>
                </div>
                <span>토</span>
                <span>오늘</span>
              </div>
              <div className={styles.weatherItem}>
                <span>☀️</span>
                <div className={styles.temperature}>
                  <span>35</span>
                  <span> / </span>
                  <span>37</span>
                </div>
                <span>토</span>
                <span>오늘</span>
              </div>
              <div className={styles.weatherItem}>
                <span>☀️</span>
                <div className={styles.temperature}>
                  <span>35</span>
                  <span> / </span>
                  <span>37</span>
                </div>
                <span>토</span>
                <span>오늘</span>
              </div>
            </div>
          </div>
          <div className={styles.valleyAccident}>
            <span>최근 5년간 사고 수</span>
            <div className={styles.graph}>
              <div className={styles.graphTitle}>
                <span>사망</span>
                <span>실종</span>
                <span>부상</span>
              </div>
              <div className={styles.graphContent}>
                <span>19</span>
                <span>27</span>
                <span>80</span>
              </div>
            </div>
          </div>
          <div className={styles.rescueSupplies}>
            <span>구조용품 현황</span>
            <div className={styles.rescueList}>
              <div className={styles.rescueItem}>
                <span>
                  <TbChartDonut4 size="40px" color="#66A5FC" />
                </span>
                <span>구명환</span>
                <span>10</span>
              </div>
              <div className={styles.rescueItem}>
                <span>
                  <TbJumpRope size="40px" color="#66A5FC" />
                </span>
                <span>구명로프</span>
                <span>10</span>
              </div>
              <div className={styles.rescueItem}>
                <span>
                  <MdEmojiPeople size="40px" color="#66A5FC" />
                </span>
                <span>인명구조함</span>
                <span>10</span>
              </div>
              <div className={styles.rescueItem}>
                <span>
                  <FaVest size="40px" color="#66A5FC" />
                </span>
                <span>구명조끼</span>
                <span>10</span>
              </div>
              <div className={styles.rescueItem}>
                <span>
                  <MdHomeRepairService size="40px" color="#66A5FC" />
                </span>
                <span>이동식 거치대</span>
                <span>10</span>
              </div>
              <div className={styles.rescueItem}>
                <span>
                  <LuUtilityPole size="40px" color="#66A5FC" />
                </span>
                <span>구명봉</span>
                <span>10</span>
              </div>
            </div>
          </div>
        </div>
      </div>
    </div>
  );
};

export default ValleyInfo;
