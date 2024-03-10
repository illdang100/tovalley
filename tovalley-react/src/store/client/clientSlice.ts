import { createSlice } from "@reduxjs/toolkit";
import { Client } from "@stomp/stompjs";

interface ClientType {
  value: Client | null;
}

const initialState: ClientType = { value: null };

const clientSlice = createSlice({
  name: "client",
  initialState,
  reducers: {
    newClient(state, action) {
      state.value = action.payload;
    },
  },
});

export default clientSlice;
export const { newClient } = clientSlice.actions;
