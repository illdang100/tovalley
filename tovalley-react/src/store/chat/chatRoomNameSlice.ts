import { createSlice } from "@reduxjs/toolkit";

const chatRoomNameSlice = createSlice({
  name: "chatRoomName",
  initialState: { value: null },
  reducers: {
    setChatRoomName(state, action) {
      state.value = action.payload;
    },
  },
});

export default chatRoomNameSlice;
export const { setChatRoomName } = chatRoomNameSlice.actions;
