import { configureStore } from '@reduxjs/toolkit';

import dates from './modules/dates';
import events from './modules/events';
import modal from './modules/modal';
import teamNum from './modules/teamNum';

const store = configureStore({
  reducer: { dates, events, modal, teamNum },
});

export type RootState = ReturnType<typeof store.getState>;
export type AppDispatch = typeof store.dispatch;

export default store;
