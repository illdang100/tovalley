import { createSlice } from "@reduxjs/toolkit";

const chatViewSlice = createSlice({
  name: "chatView",
  initialState: { value: false },
  reducers: {
    view(state, action) {
      state.value = action.payload;
    },
  },
});

export default chatViewSlice;
export const { view } = chatViewSlice.actions;
