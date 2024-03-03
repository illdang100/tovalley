import { createSlice } from "@reduxjs/toolkit";

const clientSlice = createSlice({
  name: "client",
  initialState: { value: null },
  reducers: {
    newClient(state, action) {
      state.value = action.payload;
    },
  },
});

export default clientSlice;
export const { newClient } = clientSlice.actions;
