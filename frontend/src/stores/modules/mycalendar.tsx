import { createSlice } from '@reduxjs/toolkit';

type myCalendarInitialState = {
  myCalendar: boolean;
  currentTime: boolean;
};

const initialState: myCalendarInitialState = {
  myCalendar: false,
  currentTime: false,
};

const myCalendarSlice = createSlice({
  name: 'myCalendar',
  initialState,
  reducers: {
    setMyCalendar: (state) => {
      state.myCalendar = true;
    },
    toCurrentTime: (state, action) => {
      state.currentTime = action.payload;
    },
  },
});

const { reducer } = myCalendarSlice;
export const myCalendarSelector = (state: any) => state;
export const { setMyCalendar, toCurrentTime } = myCalendarSlice.actions;
export default reducer;
