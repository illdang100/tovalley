import { Route, Routes } from "react-router-dom";
import "./App.css";
import MainPage from "./page/MainPage";
import ValleyListPage from "./page/ValleyListPage";
import ValleyPage from "./page/ValleyPage";
import LoginPage from "./page/LoginPage";
import SignupPage from "./page/SignupPage";
import MyPage from "./page/MyPage";
import SafetyGuidePage from "./page/SafetyGuidePage";
import SocialLoginException from "./page/SocialLoginException";
import LostItemListPage from "./page/LostItemListPage";
import LostItemPostPage from "./page/LostItemPostPage";
import LostItemWritePage from "./page/LostItemWritePage";
import LostItemUpdatePage from "./page/LostItemUpdatePage";
import Alarm from "./component/common/Alarm";
import { useEffect } from "react";
import { useDispatch, useSelector } from "react-redux";
import { RootState } from "./store/store";
import AlarmList from "./component/common/AlarmList";
import { setNotificationView } from "./store/notification/notificationViewSlice";
import Chat from "./component/header/Chat";

function App() {
  const notification = useSelector(
    (state: RootState) => state.notification.value
  );
  const chatView = useSelector((state: RootState) => state.view.value);
  const dispatch = useDispatch();

  useEffect(() => {
    if (chatView) dispatch(setNotificationView(false));
  }, [chatView]);

  return (
    <div>
      {notification && notification.notificationType === "CHAT" && <Alarm />}
      <AlarmList />
      <Chat />
      <Routes>
        <Route path="/" element={<MainPage />} />
        <Route path="/login" element={<LoginPage />} />
        <Route path="/signup" element={<SignupPage />} />
        <Route path="/valleylist" element={<ValleyListPage />} />
        <Route path="/valley/:id" element={<ValleyPage />} />
        <Route path="/mypage" element={<MyPage />} />
        <Route path="/safety-guide" element={<SafetyGuidePage />} />
        <Route path="/lost-item" element={<LostItemListPage />} />
        <Route path="/lost-item/:category/:id" element={<LostItemPostPage />} />
        <Route
          path="/lost-item/:category/:id/update"
          element={<LostItemUpdatePage />}
        />
        <Route path="/lost-item/write" element={<LostItemWritePage />} />
        <Route
          path="/social-login-exception"
          element={<SocialLoginException />}
        />
      </Routes>
    </div>
  );
}

export default App;
