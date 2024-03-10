import { createSlice } from "@reduxjs/toolkit";
import { StompSubscription } from "@stomp/stompjs";

interface SubscribeType {
  value: StompSubscription | null;
}

const initialState: SubscribeType = { value: null };

const subscriptionSlice = createSlice({
  name: "subscription",
  initialState,
  reducers: {
    setSubscription(state, action) {
      state.value = action.payload;
    },
  },
});

export default subscriptionSlice;
export const { setSubscription } = subscriptionSlice.actions;
