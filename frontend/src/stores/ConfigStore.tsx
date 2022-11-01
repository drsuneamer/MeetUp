import { configureStore } from '@reduxjs/toolkit';

import dates from './modules/dates';
import events from './modules/events';
import modal from './modules/modal';
import holidays from './modules/holidays';
import calendars from './modules/meetups';
import channels from './modules/channels';
import teamId from './modules/teamId';
import members from './modules/members';
import myCalendar from './modules/mycalendar';

const store = configureStore({
  reducer: { dates, events, modal, holidays, calendars, channels, teamId, members, myCalendar},
});

export type RootState = ReturnType<typeof store.getState>;
export type AppDispatch = typeof store.dispatch;

export default store;
