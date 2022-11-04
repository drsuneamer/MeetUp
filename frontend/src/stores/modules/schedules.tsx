import { createAsyncThunk, createSlice } from '@reduxjs/toolkit';
import { tSchedule, tScheduleDetail, tMettingDetail } from '../../types/events';
import { axiosInstance } from '../../components/auth/axiosConfig';
import { RootState } from '../ConfigStore';
import axios from 'axios';

type scheduleInitialState = {
  loading: boolean;
  scheduleId: string;
  schedules: {
    meetingFromMe: Array<tSchedule>;
    meetingToMe: Array<tSchedule>;
    scheduleResponseList: Array<tSchedule>;
  };
  scheduleModal: {
    addSchedule: Array<tScheduleDetail>; // 일정 등록 디테일
    // addMeeting: Array<tMettingDetail>; // 미팅 등록 디테일
  };
};

const initialState: scheduleInitialState = {
  loading: false,
  scheduleId: '',
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
  scheduleModal: {
    addSchedule: [
      {
        id: '',
        start: '',
        end: '',
        title: '',
        content: '',
        userId: '',
        userName: '',
      },
    ],
    // addMeeting: [
    //   {
    //     id: '',
    //     start: '',
    //     end: '',
    //     title: '',
    //     content: '',
    //     userId: '',
    //     userName: '',
    //     userWebex: '',
    //     meetupId: '',
    //     meetupName: '',
    //     meetupColor: '',
    //     meetupAdminUserId: '',
    //     meetupAdminUserName: '',
    //     meetupAdminUserWebex: '',
    //   },
    // ]
  },
};

export const fetchSchedule = createAsyncThunk('schedule/fetch', async (thunkAPI: any) => {
  try {
    const res = await axiosInstance.get(`/schedule?targetId=${thunkAPI[0]}&date=${thunkAPI[1]} 00:00:00`).then((res) => {
      // console.log('my schedule fetched: ', res.data);
      return res.data;
    });
    return res;
  } catch (err) {
    console.log(err);
  }
});

export const addSchedule = createAsyncThunk('schedule/fetchAddSchedule', async (thunkAPI: any) => {
  console.log(thunkAPI);
  let scheduleId = '';
  try {
    const res = await axiosInstance.post('/schedule', thunkAPI).then((res) => {
      console.log('안녕!!');
      scheduleId = res.data;
      console.log('schedule data created: ', res);
      console.log(scheduleId);
      return scheduleId;
    });
    return scheduleId;
  } catch (err) {
    console.log(err);
  }
});

export const addMeeting = createAsyncThunk('schedule/fetchAddMeeting', async (thunkAPI: any) => {
  console.log(thunkAPI);
  try {
    const res = await axiosInstance.post('/meeting ', thunkAPI).then((res) => {
      console.log('meeting data created: ', res);
      return res.data;
    });
    return res.data;
  } catch (err) {
    console.log(err);
  }
});

export const fetchScheduleDetail = createAsyncThunk('schedule/fetchSechedule', async (thunkAPI: any) => {
  console.log(thunkAPI);
  try {
    const res = await axiosInstance.get(`/schedule/${thunkAPI}`).then((res) => {
      console.log('my schedule detail fetched: ', res.data);
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
    // POST
    [addSchedule.pending.toString()]: (state) => {
      state.loading = false;
    },
    [addSchedule.fulfilled.toString()]: (state, action) => {
      state.loading = true;
      state.scheduleId = action.payload;
      console.log('확인:', state.scheduleId);
      // console.log(action.payload.id)
    },
    [addSchedule.rejected.toString()]: (state) => {
      state.loading = false;
    },
    // POST
    [addMeeting.pending.toString()]: (state) => {
      state.loading = false;
    },
    [addMeeting.fulfilled.toString()]: (state, action) => {
      state.loading = true;
    },
    [addMeeting.rejected.toString()]: (state) => {
      state.loading = false;
    },
    // GET
    // [fetchScheduleDetail.pending.toString()]: (state) => {
    //   state.loading = false;
    // },
    // [fetchScheduleDetail.fulfilled.toString()]: (state, action) => {
    //   state.loading = true;
    // },
    // [fetchScheduleDetail.rejected.toString()]: (state) => {
    //   state.loading = false;
    // },
    // GET
    [fetchSchedule.pending.toString()]: (state) => {
      state.loading = false;
    },
    [fetchSchedule.fulfilled.toString()]: (state, action) => {
      state.loading = true;
      state.schedules = action.payload;
      // console.log(state.schedules);
    },
    [fetchSchedule.rejected.toString()]: (state) => {
      state.loading = false;
    },
  },
});

const { reducer } = scheduleSlice;
export const scheduleSelector = (state: RootState) => state.schedules;
export const myScheduleSelector = (state: RootState) => state.schedules.schedules.scheduleResponseList;
export const meetingToMeSelector = (state: RootState) => state.schedules.schedules.meetingToMe;
export const meetingFromMeSelector = (state: RootState) => state.schedules.schedules.meetingFromMe;
export const scheduleIdSelector = (state: RootState) => state.schedules.scheduleId;
// const { reducer } = ScheduleModalSlice;

export default reducer;
