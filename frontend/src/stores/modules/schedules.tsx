import { createAsyncThunk, createSlice } from '@reduxjs/toolkit';
import { tSchedule, tScheduleDetail } from '../../types/events';
import { axiosInstance } from '../../components/auth/axiosConfig';
import { RootState } from '../ConfigStore';

type scheduleInitialState = {
  loading: boolean;
  schedules: {
    meetingFromMe: Array<tSchedule>;
    meetingToMe: Array<tSchedule>;
    scheduleResponseList: Array<tSchedule>;
  };
  scheduleModal: {
    scheduleDetail: tScheduleDetail; // 일정 등록 디테일
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
        open: false, 
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
        open: false,
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
        open: false,
      },
    ],
  },
  scheduleModal: {
    scheduleDetail: {
      id: 0,
      start: '',
      end: '',
      title: '',
      content: '',
      userId: '',
      userName: '',
      managerId: '',
      managerName: '',
      diffWebex: '',
      myWebex: '',
      meetupId: 0,
      open: false,
    },
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
  await axiosInstance
    .post('/schedule', thunkAPI)
    .then((res) => {
      return res.data;
    })
    .catch((err) => {
      if (err.response.status === 409) {
        alert('일정이 중복되었습니다.');
      }
    });
});

export const addMeeting = createAsyncThunk('schedule/fetchAddMeeting', async (thunkAPI: any) => {
  await axiosInstance
    .post('/meeting ', thunkAPI)
    .then((res) => {
      // console.log('meeting data created: ', res);
      return res.data;
    })
    .catch((err) => console.log(err));
});

export const fetchScheduleDetail = createAsyncThunk('schedule/fetchSechedule', async (thunkAPI: any) => {
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

export const editMeetingDetail = createAsyncThunk('schedule/editMeetingDetail', async (thunkAPI: any) => {
  try {
    const res = await axiosInstance.patch('/meeting', thunkAPI).then((res) => {
      // console.log('my meeting detail edited: ', res.data);
      return res.data;
    });
    return res;
  } catch (err) {
    console.log(err);
  }
});

export const editScheduleDetail = createAsyncThunk('schedule/editScheduleDetail', async (thunkAPI: any) => {
  try {
    const res = await axiosInstance.patch('/schedule', thunkAPI).then((res) => {
      // console.log('my meeting detail edited: ', res.data);
      return res.data;
    });
    return res;
  } catch (err) {
    console.log(err);
  }
});

// export const editScheduleDetail = createAsyncThunk('schedule/editScheduleDetail', async (thunkAPI: any) => {
//   const res = await axiosInstance
//     .post('/schedule', thunkAPI)
//     .then((res) => {
//       return res.data
//     })
//     .catch((err) => {
//       if (err.response.status === 409) {
//         alert('')
//       }
//     })
// });

export const deleteMeetingDetail = createAsyncThunk('schedule/deleteMeetingDetail', async (thunkAPI: number) => {
  const meetingId = thunkAPI;
  // console.log(meetingId);
  try {
    const res = await axiosInstance.delete(`/meeting/${meetingId}`).then((res) => {
      // console.log('deleted?', res);
      return meetingId;
    });
    return meetingId;
  } catch (err) {
    console.log(err);
  }
});

export const deleteScheduleDetail = createAsyncThunk('schedule/deleteScheduleDetail', async (thunkAPI: number) => {
  const scheduleId = thunkAPI;
  // console.log(scheduleId);
  try {
    const res = await axiosInstance.delete(`/schedule/${scheduleId}`).then((res) => {
      // console.log('deleted schedule?', res);
      return scheduleId;
    });
    return scheduleId;
  } catch (err) {
    console.log(err);
  }
});

const scheduleSlice = createSlice({
  name: 'schedule',
  initialState,
  reducers: {},
  extraReducers: {
    // POST: 내 스케쥴 생성하기
    [addSchedule.pending.toString()]: (state) => {
      state.loading = false;
    },
    [addSchedule.fulfilled.toString()]: (state, action) => {
      state.loading = true;
    },
    [addSchedule.rejected.toString()]: (state) => {
      state.loading = false;
    },

    // POST: 미팅 생성하기
    [addMeeting.pending.toString()]: (state) => {
      state.loading = false;
    },
    [addMeeting.fulfilled.toString()]: (state, action) => {
      state.loading = true;
    },
    [addMeeting.rejected.toString()]: (state) => {
      state.loading = false;
    },

    // GET: 내 스케쥴 디테일 가져오기(모달)
    [fetchScheduleDetail.pending.toString()]: (state) => {
      state.loading = false;
    },
    [fetchScheduleDetail.fulfilled.toString()]: (state, action) => {
      state.loading = true;
      state.scheduleModal.scheduleDetail = action.payload;
    },
    [fetchScheduleDetail.rejected.toString()]: (state) => {
      state.loading = false;
    },

    // GET: 내 스케쥴 전체 가져오기(달력)
    [fetchSchedule.pending.toString()]: (state) => {
      state.loading = false;
    },
    [fetchSchedule.fulfilled.toString()]: (state, action) => {
      state.loading = true;
      state.schedules = action.payload;
    },
    [fetchSchedule.rejected.toString()]: (state) => {
      state.loading = false;
    },

    // PATCH: 미팅 수정하기
    [editMeetingDetail.pending.toString()]: (state) => {
      state.loading = false;
    },
    [editMeetingDetail.fulfilled.toString()]: (state, action) => {
      state.loading = true;
    },
    [editMeetingDetail.rejected.toString()]: (state) => {
      state.loading = false;
    },

    // DELETE: 미팅 삭제하기
    [deleteMeetingDetail.pending.toString()]: (state) => {
      state.loading = false;
    },
    [deleteMeetingDetail.fulfilled.toString()]: (state) => {
      state.loading = false;
      state.scheduleModal.scheduleDetail = initialState.scheduleModal.scheduleDetail;
      window.location.reload();
    },
    [deleteMeetingDetail.rejected.toString()]: (state) => {
      state.loading = false;
    },

    // DELETE: 스케줄 삭제하기
    [deleteScheduleDetail.pending.toString()]: (state) => {
      state.loading = false;
    },
    [deleteScheduleDetail.fulfilled.toString()]: (state) => {
      state.loading = false;
      state.scheduleModal.scheduleDetail = initialState.scheduleModal.scheduleDetail;
      window.location.reload();
    },
    [deleteScheduleDetail.rejected.toString()]: (state) => {
      state.loading = false;
    },
  },
});

const { reducer } = scheduleSlice;
export const scheduleSelector = (state: RootState) => state.schedules;
export const myScheduleSelector = (state: RootState) => state.schedules.schedules.scheduleResponseList;
export const meetingToMeSelector = (state: RootState) => state.schedules.schedules.meetingToMe;
export const meetingFromMeSelector = (state: RootState) => state.schedules.schedules.meetingFromMe;
export const detailSelector = (state: RootState) => state.scheduleModal;

export default reducer;
