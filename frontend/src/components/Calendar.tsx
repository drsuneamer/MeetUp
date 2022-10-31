import React from 'react';
import MonthlyCalenderHeader from './MonthlyCalendarHeader';
import WeeklyCalenderHeader from './WeeklyCalendarHeader';
import WeeklyCalendarBody from './WeeklyCalendarBody';

const WeeklyCalendar = () => {
  return (
    <section className="w-full flex flex-col m-2 -z-9">
      <div className="flex flex-col basis-9/12 min-w-[500px]">
        <MonthlyCalenderHeader />
        <WeeklyCalenderHeader />
        <WeeklyCalendarBody />
      </div>
    </section>
  );
};

export default WeeklyCalendar;
