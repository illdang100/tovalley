import { configureStore } from "@reduxjs/toolkit";
import clientSlice from "./client/clientSlice";
import chatViewSlice from "./chat/chatViewSlice";
import chatRoomIdSlice from "./chat/chatRoomIdSlice";
import chatIdSlice from "./chat/chatIdSlice";
import notificationSlice from "./notification/notificationSlice";

const store = configureStore({
  reducer: {
    client: clientSlice.reducer,
    view: chatViewSlice.reducer,
    chatRoomId: chatRoomIdSlice.reducer,
    chatId: chatIdSlice.reducer,
    notification: notificationSlice.reducer,
  },
  middleware: (getDefaultMiddleware) =>
    getDefaultMiddleware({ serializableCheck: false }),
  // 기본 값이 true지만 배포할때 코드를 숨기기 위해서 false로 변환하기 쉽게 설정에 넣어놨다.
  devTools: true,
});

export default store;
export type RootState = ReturnType<typeof store.getState>;
export type AppDispatch = typeof store.dispatch;
