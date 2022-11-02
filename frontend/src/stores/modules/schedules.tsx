import { createAsyncThunk, createSlice } from '@reduxjs/toolkit';
import { tSchedule } from '../../types/events';
import { axiosInstance } from '../../components/auth/axiosConfig';
import { RootState } from '../ConfigStore';
import axios from 'axios';

type scheduleInitialState = {
  loading: boolean;
  schedules: {
    meetingFromMe: Array<tSchedule>;
    meetingToMe: Array<tSchedule>;
    scheduleResponseList: Array<tSchedule>;
  };
};

const initialState: scheduleInitialState = {
  loading: false,
  schedules: {
    meetingFromMe: [
      {
        id: '',
        start: '',
        end: '',
        title: '',
        content: '',
        userId: '',
        userName: '',
        meetupName: '',
        meetupColor: '',
      },
    ],
    meetingToMe: [
      {
        id: '',
        start: '',
        end: '',
        title: '',
        content: '',
        userId: '',
        userName: '',
        meetupName: '',
        meetupColor: '',
      },
    ],
    scheduleResponseList: [
      {
        id: '',
        start: '',
        end: '',
        title: '',
        content: '',
        userId: '',
        userName: '',
        meetupName: '',
        meetupColor: '',
      },
    ],
  },
};

export const fetchSchedule = createAsyncThunk('schedule/fetch', async (thunkAPI: any) => {
  try {
    const res = await axiosInstance.get(`/schedule?targetId=${thunkAPI[0]}&date=${thunkAPI[1]} 00:00:00`).then((res) => {
      console.log('my schedule fetched: ', res.data);
      return res.data;
    });
    return res;
  } catch (err) {
    console.log(err);
  }
});


export const addSchedule = createAsyncThunk('schedule/fetchAddSchedule', async(thunkAPI:any) => {
  try {
    const res = await axiosInstance.post('/schedule',thunkAPI).then((res) => {
      console.log('schedule data fetched: ', res.data);
      return res.data;
    });
    return res;
  } catch(err) {
    console.log(err)
  }
  // try {
  //   console.log(thunkAPI);
  //   const accessToken = window.localStorage.getItem('accessToken');
  //   const res = await axios({
  //     url: `${process.env.REACT_APP_BASE_URL}/api/schedule`,
  //     method: 'post',
  //     headers: {
  //       Authorization: `Bearer ${accessToken}`,
  //     },
  //     data: thunkAPI,
  //   }).then((res) => {
  //     console.log('successfully registered schedule');
  //     return res;
  //   });
  //   return res;
  // } catch (err) {
  //   return console.log(err);
  // }
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
      state.schedules = action.payload;
      console.log(state.schedules)
    },
    [fetchSchedule.rejected.toString()]: (state) => {
      state.loading = false;
    },
  },
});


const ScheduleModalSlice = createSlice({
  name: 'scheduleModal',
  initialState,
  reducers: {},
  extraReducers: {
    // POST
    [addSchedule.pending.toString()]: (state) => {
      state.loading = false;
    },
    [addSchedule.fulfilled.toString()]: (state, action) => {
      state.loading = true;
      state.schedules = action.payload;
    },
    [addSchedule.rejected.toString()]: (state) => {
      state.loading = false;
    },
  },
})

const { reducer } = scheduleSlice;
export const scheduleSelector = (state:RootState) => state.schedules
export const myScheduleSelector = (state: RootState) => state.schedules.schedules.scheduleResponseList;
export const meetingToMeSelector = (state: RootState) => state.schedules.schedules.meetingToMe;
export const meetingFromMeSelector = (state: RootState) => state.schedules.schedules.meetingFromMe;

// const { reducer } = ScheduleModalSlice;

export default reducer;