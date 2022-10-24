import { createSlice } from '@reduxjs/toolkit';

type ModalInitialState = {
  eventModalIsOpen: boolean;
};

const initialState: ModalInitialState = {
  eventModalIsOpen: false,
};

const modalSlice = createSlice({
  name: 'modal',
  initialState,
  reducers: {
    setEventModalOpen: state => {
      state.eventModalIsOpen = !state.eventModalIsOpen;
    },
  },
});

const { reducer } = modalSlice;
export const { setEventModalOpen } = modalSlice.actions;
export default reducer;