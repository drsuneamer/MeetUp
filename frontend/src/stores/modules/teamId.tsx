import { createSlice } from '@reduxjs/toolkit';

const initialStateValue = {};
export const teamIdSlice = createSlice({
  name: 'teamId',
  initialState: {
    value: initialStateValue,
  },
  reducers: {
    update: (state, action) => {
      state.value = action.payload;
    },
  },
});

export const { update } = teamIdSlice.actions;
export default teamIdSlice.reducer;
