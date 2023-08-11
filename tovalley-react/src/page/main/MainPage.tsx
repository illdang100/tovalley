import styles from "../../css/main/MainPage.module.css";
import Header from "../../component/header/Header";
import Weather from "../../component/main/Weather";
import Report from "../../component/main/Report";
import Accident from "../../component/main/Accident/Accident";
import PopularValley from "../../component/main/PopularValley";
import Footer from "../../component/footer/Footer";
import React from "react";

const MainPage = () => {
  return (
    <div>
      <Header />
      <div className={styles.body}>
        <div className={styles.top}>
          <Weather />
          <Report />
          <Accident />
        </div>
        <div>
          <PopularValley />
        </div>
      </div>
      <Footer />
    </div>
  );
};

export default MainPage;
