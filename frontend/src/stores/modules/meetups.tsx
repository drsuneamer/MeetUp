import { createAsyncThunk, createSlice } from '@reduxjs/toolkit';
import { tMeetup } from '../../types/channels';
import { RootState } from '../ConfigStore';
import { axiosInstance } from '../../components/auth/axiosConfig';

// 여기서 밋업이란 캘린더를 말함
type calendarInitialState = {
  loading: boolean;
  calendars: Array<tMeetup>;
  nickname: string;
};

const initialState: calendarInitialState = {
  loading: false,
  nickname: '',
  calendars: [{ id: '', userName: '' }],
};

export const fetchCalendarList = createAsyncThunk('calendar', async () => {
  try {
    const res = await axiosInstance.get('/meetup/calendar').then((res) => {
      // console.log('calendar data fetched: ', res.data);
      return res.data;
    });
    return res;
  } catch (err) {
    console.log(err);
  }
});

const calendarSlice = createSlice({
  name: 'calendar',
  initialState,
  reducers: {
    setUserNickName(state, action) {
      state.nickname = action.payload;
      // console.log('nickname', state.nickname);
    },
  },
  extraReducers: {
    [fetchCalendarList.pending.toString()]: (state) => {
      state.loading = false;
    },
    [fetchCalendarList.fulfilled.toString()]: (state, action) => {
      state.loading = true;
      state.calendars = action.payload;
    },
    [fetchCalendarList.rejected.toString()]: (state) => {
      state.loading = false;
    },
  },
});

const { reducer } = calendarSlice;
export const { setUserNickName } = calendarSlice.actions;
export const calendarSelector = (state: RootState) => state.calendars;
export default reducer;
