import { configureStore } from '@reduxjs/toolkit';

import dates from './modules/dates';
import events from './modules/events';
import modal from './modules/modal';
import holidays from './modules/holidays';
import meetups from './modules/meetups';
import channels from './modules/channels';

const store = configureStore({
  reducer: { dates, events, modal, holidays, meetups, channels },
});

export type RootState = ReturnType<typeof store.getState>;
export type AppDispatch = typeof store.dispatch;

export default store;
