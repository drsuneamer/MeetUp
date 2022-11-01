import { createAsyncThunk, createSlice } from '@reduxjs/toolkit';
import { tSchedule } from '../../types/events';
import { axiosInstance } from '../../components/auth/axiosConfig';
import { RootState } from '../ConfigStore';

type scheduleInitialState = {
  loading: boolean;
  meetingFromMe: Array<tSchedule>;
};

const initialState: scheduleInitialState = {
  loading: false,
  meetingFromMe: [{
    id : '',
    start : '',
    end : '',
    title : '',
    content : '',
    userId : '', 
    userName: '', 
    meetupName: '',
    meetupColor: ''
  }]
};

export const fetchSchedule = createAsyncThunk('schedule/me', async (thunkAPI:any) => {
  try {
    const res = await axiosInstance.get(`/schedule?targetId=${thunkAPI[0]}&date=${thunkAPI[1]}`).then((res) => {
      console.log('my schedule fetched: ', res.data);
      return res.data;
    });
    return res;
  } catch (err) {
    console.log(err);
  }
});

const scheduleSlice = createSlice({
  name: 'schedule',
  initialState,
  reducers: {},
  extraReducers: {
    [fetchSchedule.pending.toString()]: (state) => {
      state.loading = false;
    },
    [fetchSchedule.fulfilled.toString()]: (state, action) => {
      state.loading = true;
      console.log(action.payload)
    },
    [fetchSchedule.rejected.toString()]: (state) => {
      state.loading = false;
      console.log('rejected');
    },
  },
});

const { reducer } = scheduleSlice;
export const scheduleSelector = (state: RootState) => state.channels;
export default reducer;