import React from 'react';
import WeeklyCalenderHeader from './WeeklyCalendarHeader';
import WeeklyCalendarBody from './WeeklyCalendarBody';

const WeeklyCalendar = () => {
  return (
    <section className="w-full flex flex-col mb-2 overflow-x-scroll">
      <div className="flex flex-col flex-1 w-full min-w-[500px]">
        <WeeklyCalenderHeader />
        <WeeklyCalendarBody />
      </div>
    </section>
  );
};

export default WeeklyCalendar;