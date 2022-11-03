import { createSlice } from '@reduxjs/toolkit';


type myCalendarInitialState = {
  myCalendar: boolean;
};

const initialState: myCalendarInitialState = {
    myCalendar: false,
};

const myCalendarSlice = createSlice({
  name: 'myCalendar',
  initialState,
  reducers: {
    setMyCalendar: state => {
          state.myCalendar = true;
    },
  },
});

const { reducer } = myCalendarSlice;
export const myCalendarSelector = (state:any) => state.myCalendar;
export const { setMyCalendar } = myCalendarSlice.actions;
export default reducer;