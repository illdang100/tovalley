import React, { useEffect } from "react";

declare global {
  interface Window {
    kakao: any;
  }
}

const KakaoMap = () => {
  useEffect(() => {
    const container = document.getElementById("map"); // 지도를 담을 영역의 DOM 레퍼런스
    const options = {
      // 지도를 생성할 때 필요한 기본 옵션
      center: new window.kakao.maps.LatLng(36.213350465, 127.996370894), //지도의 중심좌표
      draggable: false, // 드래그 확대, 축소 막음
      level: 13, //지도의 레벨(확대, 축소 정도)
    };

    const map = new window.kakao.maps.Map(container, options);
  }, []);

  return (
    <div
      id="map"
      style={{ width: "100%", height: "100%", borderRadius: "10px" }}
    />
  );
};

export default KakaoMap;
