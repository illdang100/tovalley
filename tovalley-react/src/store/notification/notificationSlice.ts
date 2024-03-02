import { createSlice } from "@reduxjs/toolkit";

const notificationSlice = createSlice({
  name: "notification",
  initialState: {
    value: {
      chatRoomId: 1,
      recipientId: "test",
      senderNick: 2,
      createdDate: "2024-02-23T01:24:20.913397",
      content: "hihi",
      notificationType: "CHAT",
    },
  },
  reducers: {
    setNotification(state, action) {
      state.value = action.payload;
    },
  },
});

export default notificationSlice;
export const { setNotification } = notificationSlice.actions;
