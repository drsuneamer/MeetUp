import { createAsyncThunk, createSlice } from '@reduxjs/toolkit';
import { tMember } from '../../types/members';
import { axiosInstance } from '../../components/auth/axiosConfig';
import { RootState } from '../ConfigStore';

type memberInitialState = {
  loading: boolean;
  members: Array<tMember>;
};

const initialState: memberInitialState = {
  loading: false,
  members: [{ id: '', nickname: '' }],
};

export const fetchMemberList = createAsyncThunk('members/fetchMemberList', async (channelId: number) => {
  try {
    const res = await axiosInstance.get(`/meetup/users/${channelId}`).then((res) => {
      return res;
    });
    return res.data;
  } catch (err) {
    console.log(err);
  }
});

const memberSlice = createSlice({
  name: 'member',
  initialState,
  reducers: {},
  extraReducers: {
    [fetchMemberList.pending.toString()]: (state) => {
      state.loading = false;
    },
    [fetchMemberList.fulfilled.toString()]: (state, action) => {
      state.loading = true;
      state.members = action.payload.userInfoDtoList;
    },
    [fetchMemberList.rejected.toString()]: (state) => {
      state.loading = false;
    },
  },
});

const { reducer } = memberSlice;
export const memberSelector = (state: RootState) => state.members;
export default reducer;
