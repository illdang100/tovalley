import { Route, Routes } from "react-router-dom";
import "./App.css";
import MainPage from "./page/MainPage";
import ValleyListPage from "./page/ValleyListPage";
import ValleyPage from "./page/ValleyPage";
import LoginPage from "./page/LoginPage";
import SignupPage from "./page/SignupPage";
import MyPage from "./page/MyPage";
import SafetyGuidePage from "./page/SafetyGuidePage";

function App() {
  return (
    <div>
      <Routes>
        <Route path="/" element={<MainPage />} />
        <Route path="/login" element={<LoginPage />} />
        <Route path="/signup" element={<SignupPage />} />
        <Route path="/valleylist" element={<ValleyListPage />} />
        <Route path="/valley/:id" element={<ValleyPage />} />
        <Route path="/mypage" element={<MyPage />} />
        <Route path="/safety-guide" element={<SafetyGuidePage />} />
      </Routes>
    </div>
  );
}

export default App;
