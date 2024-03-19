import React, { useEffect, useState } from "react";
import styles from "../../css/header/Header.module.css";
import { useLocation, useNavigate } from "react-router-dom";
import { Cookies } from "react-cookie";
import axiosInstance from "../../axios_interceptor";
import { RxHamburgerMenu } from "react-icons/rx";
import { BiUser } from "react-icons/bi";
import { FiLogOut } from "react-icons/fi";
import { FaRegBell } from "react-icons/fa";
import { IoChatbubblesSharp } from "react-icons/io5";
import SockJS from "sockjs-client";
import { Client } from "@stomp/stompjs";
import { useDispatch, useSelector } from "react-redux";
import { RootState } from "../../store/store";
import { newClient } from "../../store/client/clientSlice";
import { view } from "../../store/chat/chatViewSlice";
import { setNotification } from "../../store/notification/notificationSlice";
import { setNotificationView } from "../../store/notification/notificationViewSlice";
import { enterChatRoom } from "../../store/chat/chatRoomIdSlice";

const cookies = new Cookies();
const localhost = process.env.REACT_APP_HOST;

const Header = () => {
  const navigation = useNavigate();
  const location = useLocation();
  const [login, setLogin] = useState(false);
  const [navClick, setNavClick] = useState(false);
  const dispatch = useDispatch();
  const client = useSelector((state: RootState) => state.client.value);
  const chatView = useSelector((state: RootState) => state.view.value);
  const notificationView = useSelector(
    (state: RootState) => state.notificationView.value
  );

  useEffect(() => {
    const loginStatus = cookies.get("ISLOGIN");
    if (loginStatus === true) {
      setLogin(true);
      if (!client) {
        console.log("connectSocket");
        connectSocket();
      } // 웹 소켓이 연결되어 있다면 연결 요청 x
    } else {
      setLogin(false);
    }
  }, [login, client]);

  useEffect(() => {
    if (notificationView) dispatch(view(false));
  }, [notificationView]);

  const connectSocket = () => {
    const socket = new SockJS(`${localhost}/stomp/chat`); // 서버와 웹소켓 연결

    const stompClient = new Client({
      webSocketFactory: () => socket,
      debug: (str) => {
        console.log(str);
      },
      reconnectDelay: 1000,
      // 웹 소켓이 끊어졌을 때 얼마나 빨리 연결을 시도할 지 설정.
      // recconectDelay에 설정된 대기 시간이 지난 후 다시 연결을 자동으로 시도한다.
    });

    stompClient.activate();
    // 웹소켓 연결 활성화
    // 활성화가 성공하면 onConnect가 실행 됨

    stompClient.onConnect = () => {
      console.log("connected!!");
      dispatch(newClient(stompClient));

      const getMemberId = async () => {
        try {
          const res = await axiosInstance.get("/api/auth/members/me");
          console.log(res);
          stompClient.subscribe(
            `/sub/notification/${res.data.data}`, // 알림 토픽 구독
            (notify) => {
              console.log(JSON.parse(notify.body));
              dispatch(setNotification(JSON.parse(notify.body)));
            }
          );
        } catch (err) {
          console.log(err);
        }
      };

      getMemberId();
    };
  };

  const handleLogout = () => {
    dispatch(view(false));
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
              <div className={styles.alarm}>
                <div
                  onClick={() => {
                    dispatch(setNotificationView(!notificationView));
                    dispatch(enterChatRoom(null));
                  }}
                >
                  <FaRegBell />
                </div>
                <span>•</span>
              </div>
              <div
                className={styles.chatIcon}
                onClick={() => {
                  dispatch(view(!chatView));
                  dispatch(enterChatRoom(null));
                }}
              >
                <IoChatbubblesSharp />
              </div>
              <span
                className={styles.myPage}
                onClick={() => {
                  dispatch(view(false));
                  navigation("/mypage");
                }}
              >
                마이페이지
              </span>
              <span className={styles.logout} onClick={handleLogout}>
                로그아웃
              </span>
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
              dispatch(view(false));
              dispatch(enterChatRoom(null));
              navigation("/valleylist");
            }}
            className={
              location.pathname === "/valleylist"
                ? styles.navMenuClicked
                : styles.navMenu
            }
          >
            전국 물놀이 장소
          </span>
          <span
            onClick={() => {
              dispatch(view(false));
              dispatch(enterChatRoom(null));
              navigation("/lost-item");
            }}
            className={
              location.pathname === "/lost-item"
                ? styles.navMenuClicked
                : styles.navMenu
            }
          >
            분실물 찾기
          </span>
          <span
            onClick={() => {
              dispatch(view(false));
              dispatch(enterChatRoom(null));
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
