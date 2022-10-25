import { createAsyncThunk, createSlice } from '@reduxjs/toolkit';
import { tChannel } from '../../types/channels';
import axios from 'axios';

interface channelState extends tChannel {
    loading: boolean;
}
const initialState: channelState = {
  id: 0,
  name: '',
  color: '',
  loading: false,
};

export const fetchChannelList = createAsyncThunk('channel/fetch', async (thunkAPI) => {
    try {
      const res = await axios({
        url: '/api/channel',
        method: 'get',
        headers: {
            Authorization: `Bearer `
        }
      }).then((res:Array<tChannel>) => {
        console.log('channel data fetched: ', res)
      })
      return res;
    } catch (err) {
      return thunkAPI
    }
  });
  

const channelSlice = createSlice({
  name: 'channel',
  initialState,
  reducers: {},
  extraReducers: {
    [fetchChannelList.pending.toString()]: (state) => {
        state.loading = false;
        console.log('fetching pending')
    },
    [fetchChannelList.fulfilled.toString()]: (state, action) => {
        state.loading = true;
        console.log(action.payload)
        console.log('fetching fulfilled')
    },
    [fetchChannelList.rejected.toString()]: (state) => {
        state.loading = false;
        console.log('fetching rejected')
    },
  }
});

const { reducer } = channelSlice;
export const channelSelector = (state: channelState) => state;
export default reducer;