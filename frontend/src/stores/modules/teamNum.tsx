import { createSlice } from '@reduxjs/toolkit';

const initialStateValue = {};
export const teamNumSlice = createSlice({
  name: 'teamNum',
  initialState: {
    value: initialStateValue,
  },
  reducers: {
    update: (state, action) => {
      state.value = action.payload;
    },
  },
});

export const { update } = teamNumSlice.actions;
export default teamNumSlice.reducer;
