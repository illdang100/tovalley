import React, { useEffect, useState } from "react";
import styles from "../../css/header/Header.module.css";
import { useLocation, useNavigate } from "react-router-dom";
import { Cookies } from "react-cookie";
import axiosInstance from "../../axios_interceptor";
import { RxHamburgerMenu } from "react-icons/rx";
import { BiUser } from "react-icons/bi";
import { FiLogOut } from "react-icons/fi";

const cookies = new Cookies();

const Header = () => {
  const navigation = useNavigate();
  const location = useLocation();
  const [login, setLogin] = useState(false);
  const [navClick, setNavClick] = useState(false);

  useEffect(() => {
    const loginStatus = cookies.get("ISLOGIN");
    console.log("loginStatus : ", loginStatus);

    if (loginStatus === true) {
      setLogin(true);
    } else {
      setLogin(false);
    }
  }, [login]);

  const handleLogout = () => {
    axiosInstance
      .delete(`/api/logout`)
      .then((res) => {
        console.log(res);
        if (res.status === 200) {
          const loginStatus = cookies.get("ISLOGIN");
          console.log("loginStatus : ", loginStatus);
          !loginStatus && setLogin(false);
          navigation("/");
        }
      })
      .catch((err) => console.log(err));
  };

  return (
    <div className={styles.header}>
      <div className={styles.headerWrapper}>
        <div className={styles.headertop}>
          <div className={styles.hamburger}>
            <span onClick={() => setNavClick(!navClick)}>
              <RxHamburgerMenu size="25px" color="#787878" />
            </span>
          </div>
          <div
            className={styles.logo}
            onClick={() => {
              navigation("/");
            }}
          >
            <img
              src={process.env.PUBLIC_URL + "/img/투계곡-logo.png"}
              alt="tovalley logo"
              width="120px"
            />
          </div>
          {login ? (
            <div className={styles.login}>
              <span onClick={() => navigation("/mypage")}>마이페이지</span>
              <span onClick={handleLogout}>로그아웃</span>
              <span
                className={styles.myPageIcon}
                onClick={() => navigation("/mypage")}
              >
                <BiUser size="22px" color="#787878" />
              </span>
              <span className={styles.logOutIcon} onClick={handleLogout}>
                <FiLogOut size="22px" color="#787878" />
              </span>
            </div>
          ) : (
            <div className={styles.signup}>
              <span
                onClick={() => {
                  navigation("/signup");
                }}
              >
                회원가입
              </span>
              <span
                onClick={() => {
                  navigation("/login");
                }}
              >
                로그인
              </span>
            </div>
          )}
        </div>
        <div className={styles.nav}>
          <span
            onClick={() => {
              navigation("/valleylist");
            }}
            className={
              location.pathname === "/valleylist"
                ? styles.navMenuClicked
                : styles.navMenu
            }
          >
            전국 계곡
          </span>
          <span
            onClick={() => {
              navigation("/safety-guide");
            }}
            className={
              location.pathname === "/safety-guide"
                ? styles.navMenuClicked
                : styles.navMenu
            }
          >
            안전 가이드
          </span>
        </div>
      </div>
      <div
        className={styles.mobileNav}
        style={
          navClick
            ? { transform: "translateY(5px)", transition: "all 0.3s" }
            : { transform: "translateY(-35px)", transition: "all 0.3s" }
        }
      >
        <span
          onClick={() => {
            navigation("/valleylist");
          }}
          className={
            location.pathname === "/valleylist"
              ? styles.mobileNavMenuClicked
              : styles.mobileNavMenu
          }
        >
          전국 계곡
        </span>
        <span
          onClick={() => {
            navigation("/safety-guide");
          }}
          className={
            location.pathname === "/safety-guide"
              ? styles.mobileNavMenuClicked
              : styles.mobileNavMenu
          }
        >
          안전 가이드
        </span>
      </div>
    </div>
  );
};

export default Header;
