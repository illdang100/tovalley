import { createSlice } from "@reduxjs/toolkit";

const chatIdSlice = createSlice({
  name: "chatId",
  initialState: { value: null },
  reducers: {
    setId(state, action) {
      state.value = action.payload;
    },
  },
});

export default chatIdSlice;
export const { setId } = chatIdSlice.actions;
