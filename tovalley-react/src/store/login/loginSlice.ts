import { createSlice } from "@reduxjs/toolkit";

const notificationSlice = createSlice({
  name: "login",
  initialState: { value: false },
  reducers: {
    setLoginState(state, action) {
      state.value = action.payload;
    },
  },
});

export default notificationSlice;
export const { setLoginState } = notificationSlice.actions;
