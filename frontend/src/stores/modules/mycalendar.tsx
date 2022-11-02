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
      console.log('안되냐')
      if ( window.location.href === `http://localhost:3000/calendar/${localStorage.getItem('id')}`) {
          state.myCalendar = true;
          console.log('url', state.myCalendar)
      }
    },
  },
});

const { reducer } = myCalendarSlice;
export const myCalendarSelector = (state:any) => state.myCalendar;
export const { setMyCalendar } = myCalendarSlice.actions;
export default reducer;