import { createAsyncThunk, createSlice } from '@reduxjs/toolkit';
import { RootState } from '../ConfigStore';
import { axiosInstance } from '../../components/auth/axiosConfig';

const initialState = {
  loading: false,
  groups: [{ id: 0, name: '', leader: false }],
  group: { id: 0, name: '' },
  groupMembers: [{ id: '', nickname: '' }],
};

export const fetchGroupList = createAsyncThunk('groups', async () => {
  try {
    const res = await axiosInstance.get('/group').then((res) => {
      return res.data;
    });
    return res;
  } catch (err) {
    console.log(err);
  }
});

export const fetchGroupMemberList = createAsyncThunk('groups/fetchMemberList', async (groupId: number) => {
  try {
    const res = await axiosInstance.get(`/group/${groupId}`).then((res) => {
      return res;
    });
    return res.data;
  } catch (err) {
    console.log(err);
  }
});

const groupSlice = createSlice({
  name: 'groups',
  initialState,
  reducers: {
    update: (state, action) => {
      state.group = action.payload;
    },
  },
  extraReducers: {
    [fetchGroupList.pending.toString()]: (state) => {
      state.loading = false;
    },
    [fetchGroupList.fulfilled.toString()]: (state, action) => {
      state.loading = true;
      state.groups = action.payload;
    },
    [fetchGroupList.rejected.toString()]: (state) => {
      state.loading = false;
    },
    [fetchGroupMemberList.pending.toString()]: (state) => {
      state.loading = false;
    },
    [fetchGroupMemberList.fulfilled.toString()]: (state, action) => {
      state.loading = true;
      state.groupMembers = action.payload;
    },
    [fetchGroupMemberList.rejected.toString()]: (state) => {
      state.loading = false;
    },
  },
});

const { reducer } = groupSlice;
export const { update } = groupSlice.actions;
export const groupMemberSelector = (state: RootState) => state.groupMembers;
export const groupSelector = (state: RootState) => state.group;
export const groupsSelector = (state: RootState) => state.groups;
export default reducer;
