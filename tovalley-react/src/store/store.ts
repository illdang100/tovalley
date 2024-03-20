import { combineReducers, configureStore } from "@reduxjs/toolkit";
import clientSlice from "./client/clientSlice";
import chatViewSlice from "./chat/chatViewSlice";
import chatRoomIdSlice from "./chat/chatRoomIdSlice";
import chatIdSlice from "./chat/chatIdSlice";
import notificationSlice from "./notification/notificationSlice";
import notificationViewSlice from "./notification/notificationViewSlice";
import loginSlice from "./login/loginSlice";
import { persistReducer } from "redux-persist";
import storageSession from "redux-persist/lib/storage/session";
import subscriptionSlice from "./chat/subscriptionSlice";
import chatRoomNameSlice from "./chat/chatRoomNameSlice";

// const store = configureStore({
//   reducer: {
//     client: clientSlice.reducer,
//     view: chatViewSlice.reducer,
//     chatRoomId: chatRoomIdSlice.reducer,
//     chatId: chatIdSlice.reducer,
//     notification: notificationSlice.reducer,
//   },
//   middleware: (getDefaultMiddleware) =>
//     getDefaultMiddleware({ serializableCheck: false }),
//   // 기본 값이 true지만 배포할때 코드를 숨기기 위해서 false로 변환하기 쉽게 설정에 넣어놨다.
//   devTools: true,
// });

const rootReducer = combineReducers({
  client: clientSlice.reducer,
  view: chatViewSlice.reducer,
  chatRoomId: chatRoomIdSlice.reducer,
  chatId: chatIdSlice.reducer,
  notification: notificationSlice.reducer,
  login: loginSlice.reducer,
  notificationView: notificationViewSlice.reducer,
  subscription: subscriptionSlice.reducer,
  chatRoomName: chatRoomNameSlice.reducer,
});

const persistConfig = {
  key: "login",
  storage: storageSession,
  whitelist: ["login"],
};

const persistedReducer = persistReducer(persistConfig, rootReducer);

const store = configureStore({
  reducer: persistedReducer,
  middleware: (getDefaultMiddleware) =>
    getDefaultMiddleware({ serializableCheck: false }),
  // 기본 값이 true지만 배포할때 코드를 숨기기 위해서 false로 변환하기 쉽게 설정에 넣어놨다.
  devTools: true,
});

export default store;
export type RootState = ReturnType<typeof store.getState>;
export type AppDispatch = typeof store.dispatch;
