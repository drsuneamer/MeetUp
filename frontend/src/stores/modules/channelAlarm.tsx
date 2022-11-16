import { createAsyncThunk, createSlice } from '@reduxjs/toolkit';
import { tAlarm } from '../../types/channels';
import { axiosInstance } from '../../components/auth/axiosConfig';
import { RootState } from '../ConfigStore';

type channelAlarmInitialState = {
  loading: boolean;
  alarmChannels: Array<tAlarm>;
};

const initialState: channelAlarmInitialState = {
  loading: false,
  alarmChannels: [{ meetupId: 0, displayName: '' }],
};
export const fetchAlarmChannelList = createAsyncThunk('meeting', async (managerId: any) => {
  try {
    const res = await axiosInstance.get(`/meeting/channel/${managerId}`).then((res) => {
      console.log('alarm channel data fetched: ', res.data);
      return res.data;
    });
    return res;
  } catch (err) {
    console.log(err);
  }
});

const AlarmChannelSlice = createSlice({
  name: 'alarmChannel',
  initialState,
  reducers: {},
  extraReducers: {
    [fetchAlarmChannelList.pending.toString()]: (state) => {
      state.loading = false;
    },
    [fetchAlarmChannelList.fulfilled.toString()]: (state, action) => {
      state.loading = true;
      state.alarmChannels = action.payload;
    },
    [fetchAlarmChannelList.rejected.toString()]: (state) => {
      state.loading = false;
      // console.log('rejected');
    },
  },
});

const { reducer } = AlarmChannelSlice;
export const alarmChannelSelector = (state: RootState) => state.alarmChannels;
export default reducer;
