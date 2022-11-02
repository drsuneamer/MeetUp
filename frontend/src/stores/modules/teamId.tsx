import { createSlice } from '@reduxjs/toolkit';

/* 
create-channel 안의 드롭다운에서 팀(왼쪽)을 선택했을 때
그 id값이 채널(오른쪽)을 호출하는 컴포넌트로 넘어갈 수 있게 하기 위해 사용합니다
*/

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
