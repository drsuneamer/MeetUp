import { createSlice } from '@reduxjs/toolkit';

// 화면 위에 표시될 각 모달의 온오프 상태를 관리합니다.

type ModalInitialState = {
  eventModalIsOpen: boolean;
  detailModalIsOpen: boolean;
  editModalIsOpen: boolean;
  editModalType: string;
  webexModalIsOpen: boolean; // 웹엑스 로고 누르면 링크로 이동하는 모달 확인
  modalType: string;
  memberListModalIsOpen: boolean; // 밋업 관리하기의 밋업 클릭 시 멤버 리스트 확인
  deleteModalIsOpen: boolean; // 로그아웃 / 삭제 확인 모달
  deleteModalType: string[]; // 0번 인덱스에는 로그아웃/삭제 여부를, 1번 인덱스에는 어떤 것을 삭제하는지
  createGroupModalIsOpen: boolean; // 마이페이지 안의 그룹 생성 모달 (더하기버튼)
};

const initialState: ModalInitialState = {
  eventModalIsOpen: false,
  detailModalIsOpen: false,
  editModalIsOpen: false,
  editModalType: '',
  webexModalIsOpen: false,
  modalType: '',
  memberListModalIsOpen: false,
  deleteModalIsOpen: false,
  deleteModalType: [],
  createGroupModalIsOpen: false,
};

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
    setEditModalOpen: (state, action) => {
      state.editModalIsOpen = !state.editModalIsOpen;
      state.editModalType = action.payload;
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
    setCreateGroupModalOpen: (state) => {
      state.createGroupModalIsOpen = !state.createGroupModalIsOpen;
    },
  },
});

const { reducer } = modalSlice;
export const ModalSelector = (state: any) => state.modal;
export const {
  setEventModalOpen,
  setDetailModalOpen,
  setEditModalOpen,
  setWebexModalOpen,
  setMemberListModalOpen,
  setDeleteModalOpen,
  setCreateGroupModalOpen,
} = modalSlice.actions;
export default reducer;
