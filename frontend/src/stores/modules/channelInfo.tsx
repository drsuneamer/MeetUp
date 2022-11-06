import { createSlice } from '@reduxjs/toolkit';

/* 
  밋업 목록의 톱니바퀴를 눌렀을 때
  그 선택한 밋업의 id값을
  edit-channel/{id} 페이지로 보내거나

  밋업 목록의 인덱스를 눌렀을 때
  선택한 밋업의 title값을
  MemberListModal로 보내기 위해 사용합니다.
*/

interface ChannelInfo {
  id?: number;
  title?: string;
}

const initialStateValue: ChannelInfo = {};
export const channelInfoSlice = createSlice({
  name: 'channelInfo',
  initialState: {
    value: initialStateValue,
  },
  reducers: {
    update: (state, action) => {
      state.value = action.payload;
      console.log(action.payload);
    },
  },
});

export const { update } = channelInfoSlice.actions;
export default channelInfoSlice.reducer;
