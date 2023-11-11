import React from "react";
import Header from "../component/header/Header";
import Footer from "../component/footer/Footer";
import styled from "styled-components";
import { PiWarningCircleBold } from "react-icons/pi";

const Container = styled.div`
  display: flex;
  flex-direction: column;
  justify-content: center;
  align-items: center;
  min-height: 70vh;
  width: 420px;

  img {
    margin-bottom: 1em;
  }

  span {
    &:nth-child(3) {
      font-weight: bold;
      display: block;
      margin-top: 0.3em;
    }
  }

  div {
    &:nth-child(4) {
      border: 1px solid #b8b8b8;
      margin-top: 1em;
      padding: 0.8em;
      width: 100%;
      box-sizing: border-box;
      border-radius: 3px;

      span {
        color: #555555;
        font-size: 0.9rem;
        display: block;

        &:first-child {
          margin-bottom: 0.2em;
        }
      }
    }

    &:last-child {
      margin-top: 1.5em;
      width: 100%;

      span {
        display: block;

        &:first-child {
          color: #393939;
          font-size: 0.9rem;
        }
        &: last-child {
          background-color: #66a5fc;
          border-radius: 3px;
          padding: 0.8em;
          color: white;
          font-weight: bold;
          text-align: center;
          cursor: pointer;
          margin-top: 0.3em;
        }
      }
    }
  }
`;

const SocialLoginException = () => {
  return (
    <div>
      <Header />
      <div style={{ display: "flex", justifyContent: "center" }}>
        <Container>
          <img
            src={process.env.PUBLIC_URL + "/img/투계곡-logo.png"}
            alt="tovalley logo"
            width="190px"
          />
          <span>
            <PiWarningCircleBold color="#66A5FC" size="50px" />
          </span>
          <span>간편 로그인으로 로그인할 수 없습니다.</span>
          <div>
            <span>현재 tovalley는 간편 로그인 개발 중 상태입니다.</span>
            <span>일반 로그인/회원가입을 이용하세요.</span>
          </div>
          <div>
            <span>자세한 사항은 tovalley 관리자에게 문의 바랍니다.</span>
            <span onClick={() => window.location.replace("/login")}>확인</span>
          </div>
        </Container>
      </div>
      <Footer />
    </div>
  );
};

export default SocialLoginException;
