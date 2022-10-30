import { createSlice, PayloadAction } from '@reduxjs/toolkit';
import {
  EventDetail,
  EventModalData,
  NewEvent,
  SelectedEvent,
} from '../../types/events';

type EventsInitialState = {
  events: { [key: string]: EventDetail[] };
  eventModalData: EventModalData | null;
};

const initialState: EventsInitialState = {
  events: {},
  eventModalData: null,
};

const eventsSlice = createSlice({
  name: 'events',
  initialState,
  reducers: {
    setEventModalData: (
      state,
      action: PayloadAction<EventModalData | null>,
    ) => {
      const data = action.payload;
      if (data !== null) {
        state.eventModalData = data;
      } else {
        state.eventModalData = null;
      }
    },
    addEvent: (state, action: PayloadAction<NewEvent>) => {
      const { date, eventDetail } = action.payload;
      if (state.events[date] === undefined) {
        state.events[date] = [];
      }

      state.eventModalData = null;
      state.events[date].push(eventDetail);
      console.log(state.events[date])
    },
    deleteEvent: (state, action: PayloadAction<SelectedEvent>) => {
      const { date, index } = action.payload;

      state.events[date] = state.events[date].filter((_, i) => i !== index);
    },
  },
});

const { reducer } = eventsSlice;
export const { addEvent, deleteEvent, setEventModalData } = eventsSlice.actions;
export default reducer;