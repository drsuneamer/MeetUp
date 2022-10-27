import { createSlice} from '@reduxjs/toolkit';
import { RootState } from '../../stores/ConfigStore';
import holidayList from '../../data/holidays.json';

type HolidaysInitialState = {
  holidays: Array<Object>;
};

const initialState: HolidaysInitialState = {
  holidays: [{ locdate: '', dateName: ''}],
};

const holidaySlice = createSlice({
  name: 'holidays',
  initialState,
  reducers: {
    fetchHolidays: (state) => {
        state.holidays = holidayList
  }
}});

const { reducer } = holidaySlice;
export const { fetchHolidays } = holidaySlice.actions;
export const holidaySelector = (state:RootState) => state.holidays;
export default reducer;