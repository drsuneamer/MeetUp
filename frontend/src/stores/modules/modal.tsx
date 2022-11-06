import { createSlice } from '@reduxjs/toolkit';

type ModalInitialState = {
  eventModalIsOpen: boolean;
  detailModalIsOpen: boolean;
  webexModalIsOpen: boolean;
  memberListModalIsOpen: boolean;
};

const initialState: ModalInitialState = {
  eventModalIsOpen: false,
  detailModalIsOpen: false,
  webexModalIsOpen: false,
  memberListModalIsOpen: false,
};

const modalSlice = createSlice({
  name: 'modal',
  initialState,
  reducers: {
    setEventModalOpen: (state) => {
      state.eventModalIsOpen = !state.eventModalIsOpen;
    },
    setDetailModalOpen: (state) => {
      state.detailModalIsOpen = !state.detailModalIsOpen;
    },
    setWebexModalOpen: (state) => {
      state.webexModalIsOpen = !state.webexModalIsOpen;
    },
    setMemberListModalOpen: (state) => {
      state.memberListModalIsOpen = !state.memberListModalIsOpen;
    },
  },
});

const { reducer } = modalSlice;
export const ModalSelector = (state: any) => state.modal;
export const { setEventModalOpen, setDetailModalOpen, setWebexModalOpen, setMemberListModalOpen } = modalSlice.actions;
export default reducer;
