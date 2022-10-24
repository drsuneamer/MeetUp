import React, { useMemo } from 'react';
import { DAYS } from '../components/constants/Days';
import { getThisWeek } from '../utils/GetThisWeek';
import { useAppSelector } from '../stores/ConfigHooks';

const WeeklyCalenderHeader = () => {
  const { currentDate } = useAppSelector((state) => state.dates);

  const weekly = useMemo(() => {
    return getThisWeek(currentDate);
  }, [currentDate]);

  return (
    <div className="flex ml-[65px] mr-[20px]">
      {weekly.map((date, index) => {
        return (
          <div className="flex flex-1 flex-col" key={date.date}>
            <div className={`text-center text-sm ${date.isToday ? 'text-title' : 'text-body'}`}>{DAYS[index]}</div>
            <div
              className={`text-center text-2xl p-1 w-10 h-10 rounded-full m-auto flex justify-center items-center text-s
            ${date.isToday ? 'bg-title text-background' : 'text-body'}`}
            >
              {date.date}
            </div>
          </div>
        );
      })}
    </div>
  );
};

export default WeeklyCalenderHeader;
