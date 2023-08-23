import React from "react";
import Header from "../component/header/Header";
import Footer from "../component/footer/Footer";
import ValleyInfo from "../component/valley/valleyInfo/ValleyInfo";
import ValleyQuality from "../component/valley/ValleyQuality";
import ValleySchedule from "../component/valley/ValleySchedule";
import ValleyReview from "../component/valley/ValleyReview";

const ValleyPage = () => {
  return (
    <div>
      <Header />
      <ValleyInfo />
      <div style={{ padding: "0 10% 8em 10%" }}>
        <ValleyQuality />
        <ValleySchedule />
        <ValleyReview />
      </div>
      <Footer />
    </div>
  );
};

export default ValleyPage;
