import { configureStore } from '@reduxjs/toolkit';

import dates from './modules/dates';
import events from './modules/events';
import modal from './modules/modal';
import holidays from './modules/holidays';
import calendars from './modules/meetups';
import channels from './modules/channels';
import teamId from './modules/teamId';
import members from './modules/members';
import mycalendar from './modules/mycalendar';
import schedules from './modules/schedules';
import scheduleModal from './modules/schedules';
import channelInfo from './modules/channelInfo';
import alarmChannels from './modules/channelAlarm';
import scheduleId from './modules/schedules';

const store = configureStore({
  reducer: {
    dates,
    events,
    modal,
    holidays,
    calendars,
    channels,
    teamId,
    channelInfo,
    members,
    mycalendar,
    schedules,
    scheduleModal,
    alarmChannels,
    scheduleId,
  },
});

export type RootState = ReturnType<typeof store.getState>;
export type AppDispatch = typeof store.dispatch;

export default store;
