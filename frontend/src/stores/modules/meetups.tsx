import { createAsyncThunk, createSlice } from '@reduxjs/toolkit';
import { tMeetup } from '../../types/channels';
import axios from 'axios';

interface meetupState extends tMeetup {
  loading: boolean;
}
const initialState: meetupState = {
  manager_id: 0,
  name: '',
  loading: false,
};

export const fetchMeetupList = createAsyncThunk('meetup/fetch', async (thunkAPI) => {
  try {
    const res = await axios({
      url: '/api/meetup',
      method: 'get',
      headers: {
        Authorization: `Bearer `,
      },
    }).then((res) => {
      console.log('meetup data fetched: ', res);
    });
    return res;
  } catch (err) {
    return thunkAPI;
  }
});

const meetupSlice = createSlice({
  name: 'meetup',
  initialState,
  reducers: {},
  extraReducers: {
    [fetchMeetupList.pending.toString()]: (state) => {
      state.loading = false;
      console.log('fetching pending');
    },
    [fetchMeetupList.fulfilled.toString()]: (state, action) => {
      state.loading = true;
      console.log(action.payload);
      console.log('fetching fulfilled');
    },
    [fetchMeetupList.rejected.toString()]: (state) => {
      state.loading = false;
      console.log('fetching rejected');
    },
  },
});

const { reducer } = meetupSlice;
export const meetupSelector = (state: meetupState) => state;
export default reducer;
