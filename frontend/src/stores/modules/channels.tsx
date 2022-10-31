import { createAsyncThunk, createSlice } from '@reduxjs/toolkit';
import { tChannel } from '../../types/channels';
import { axiosInstance } from '../../components/auth/axiosConfig';
import { RootState } from '../ConfigStore';

// interface channelState extends tChannel {
//   loading: boolean;
// }

type channelInitialState = {
  loading: boolean;
  channels: Array<tChannel>;
};

const initialState: channelInitialState = {
  loading: false,
  channels: [{ id: 0, title: '', color: '' }],
};

export const fetchChannelList = createAsyncThunk('meetup', async () => {
  try {
    const res = await axiosInstance.get('/meetup').then((res) => {
      // console.log('channel data fetched: ', res.data);
      return res.data;
    });
    return res;
  } catch (err) {
    console.log(err);
  }
});

const channelSlice = createSlice({
  name: 'channel',
  initialState,
  reducers: {},
  extraReducers: {
    [fetchChannelList.pending.toString()]: (state) => {
      state.loading = false;
    },
    [fetchChannelList.fulfilled.toString()]: (state, action) => {
      state.loading = true;
      state.channels = action.payload;
    },
    [fetchChannelList.rejected.toString()]: (state) => {
      state.loading = false;
      console.log('rejected');
    },
  },
});

const { reducer } = channelSlice;
export const channelSelector = (state: RootState) => state.channels;
export default reducer;
