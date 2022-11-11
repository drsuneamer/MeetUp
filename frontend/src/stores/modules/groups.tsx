import { createAsyncThunk, createSlice } from '@reduxjs/toolkit';
import { RootState } from '../ConfigStore';
import { axiosInstance } from '../../components/auth/axiosConfig';

const initialState = {
  loading: false,
  groups: [{ id: 0, name: '', leader: false }],
};

export const fetchGroupList = createAsyncThunk('groups', async () => {
  try {
    const res = await axiosInstance.get('/group').then((res) => {
      console.log(res);
      return res.data;
    });
    return res;
  } catch (err) {
    console.log(err);
  }
});

const GroupSlice = createSlice({
  name: 'groups',
  initialState,
  reducers: {},
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
  },
});

const { reducer } = GroupSlice;
export const groupSelector = (state: RootState) => state.groups;
export default reducer;
