import { createSlice } from "@reduxjs/toolkit";

const notificationSlice = createSlice({
  name: "notification",
  initialState: {
    value: null,
  },
  reducers: {
    setNotification(state, action) {
      state.value = action.payload;
    },
  },
});

export default notificationSlice;
export const { setNotification } = notificationSlice.actions;
