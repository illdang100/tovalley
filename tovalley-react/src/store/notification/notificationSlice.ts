import { createSlice } from "@reduxjs/toolkit";
import { NotificationType } from "../../typings/db";

interface MyNotification {
  value: NotificationType | null;
}

const initialState: MyNotification = { value: null };

const notificationSlice = createSlice({
  name: "notification",
  initialState,
  reducers: {
    setNotification(state, action) {
      state.value = action.payload;
    },
  },
});

export default notificationSlice;
export const { setNotification } = notificationSlice.actions;
