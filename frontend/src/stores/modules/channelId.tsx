import { createSlice } from '@reduxjs/toolkit';

/* 
  밋업 목록의 톱니바퀴를 눌렀을 때
  그 선택한 밋업의 id값을
  edit-channel/{id} 페이지로 보내기 위해 사용합니다.
*/

const initialStateValue = {};
export const channelIdSlice = createSlice({
  name: 'channelId',
  initialState: {
    value: initialStateValue,
  },
  reducers: {
    update: (state, action) => {
      state.value = action.payload;
    },
  },
});

export const { update } = channelIdSlice.actions;
export default channelIdSlice.reducer;
