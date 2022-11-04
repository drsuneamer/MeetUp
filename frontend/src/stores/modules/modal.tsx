import { createSlice } from '@reduxjs/toolkit';

type ModalInitialState = {
  eventModalIsOpen: boolean;
  detailModalIsOpen: boolean;
  webexModalIsOpen: boolean;
};

const initialState: ModalInitialState = {
  eventModalIsOpen: false,
  detailModalIsOpen: false,
  webexModalIsOpen: false,
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
  },
});

const { reducer } = modalSlice;
export const ModalSelector = (state: any) => state.modal;
export const { setEventModalOpen, setDetailModalOpen, setWebexModalOpen } = modalSlice.actions;
export default reducer;
