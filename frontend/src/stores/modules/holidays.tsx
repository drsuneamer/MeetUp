import { createSlice } from '@reduxjs/toolkit';
import { RootState } from '../../stores/ConfigStore';
import holidayList from '../../data/holidays.json';
import { HolidayDetail } from '../../types/events';

type HolidaysInitialState = {
  holidays: Array<HolidayDetail>;
};

const initialState: HolidaysInitialState = {
  holidays: [{ locdate: '', dateName: '' }],
};

const holidaySlice = createSlice({
  name: 'holidays',
  initialState,
  reducers: {
    fetchHolidays: (state) => {
      state.holidays = holidayList.data;
    },
  },
});

const { reducer } = holidaySlice;
export const { fetchHolidays } = holidaySlice.actions;
export const holidaySelector = (state: RootState) => state.holidays;
export default reducer;
