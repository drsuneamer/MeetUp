import { createAsyncThunk, createSlice } from '@reduxjs/toolkit';
import { axiosInstance } from '../../components/auth/axiosConfig';

// 화면 위에 표시될 각 모달의 온오프 상태를 관리합니다.

type ModalInitialState = {
  eventModalIsOpen: boolean;
  detailModalIsOpen: boolean;
  editModalIsOpen: boolean;
  webexModalIsOpen: boolean; // 웹엑스 로고 누르면 링크로 이동하는 모달 확인
  modalType: string;
  memberListModalIsOpen: boolean; // 밋업 관리하기의 밋업 클릭 시 멤버 리스트 확인
  deleteModalIsOpen: boolean; // 로그아웃 / 삭제 확인 모달
  deleteModalType: string;
};

const initialState: ModalInitialState = {
  eventModalIsOpen: false,
  detailModalIsOpen: false,
  editModalIsOpen: false,
  webexModalIsOpen: false,
  modalType: '',
  memberListModalIsOpen: false,
  deleteModalIsOpen: false,
  deleteModalType: '',
};

export const logout = createAsyncThunk('logout', async () => {
  try {
    const res = await axiosInstance.get('/meetup/calendar').then((res) => {
      return res.status;
    });
    return res;
  } catch (err) {
    console.log(err);
  }
});

const modalSlice = createSlice({
  name: 'modal',
  initialState,
  reducers: {
    setEventModalOpen: (state) => {
      state.eventModalIsOpen = !state.eventModalIsOpen;
    },
    setDetailModalOpen: (state, action) => {
      state.detailModalIsOpen = !state.detailModalIsOpen;
      state.modalType = action.payload;
    },
    setEditModalOpen: (state) => {
      state.editModalIsOpen = !state.editModalIsOpen;
    },
    setWebexModalOpen: (state) => {
      state.webexModalIsOpen = !state.webexModalIsOpen;
    },
    setMemberListModalOpen: (state) => {
      state.memberListModalIsOpen = !state.memberListModalIsOpen;
    },
    setDeleteModalOpen: (state, action) => {
      state.deleteModalIsOpen = !state.deleteModalIsOpen;
      state.deleteModalType = action.payload;
    },
  },
  extraReducers: {
    [logout.pending.toString()]: (state) => {},
    [logout.fulfilled.toString()]: (state, action) => {
      if (action.payload === 200) {
        window.localStorage.clear();

        window.location.reload();
      }
    },
    [logout.rejected.toString()]: (state) => {},
  },
});

const { reducer } = modalSlice;
export const ModalSelector = (state: any) => state.modal;
export const { setEventModalOpen, setDetailModalOpen, setEditModalOpen, setWebexModalOpen, setMemberListModalOpen, setDeleteModalOpen } =
  modalSlice.actions;
export default reducer;
