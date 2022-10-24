import React, { useMemo } from 'react';
import { DAYS } from '../components/constants/Days';
import { getThisWeek } from '../utils/GetThisWeek';
import { useAppSelector } from '../stores/ConfigHooks';

const WeeklyCalenderHeader = () => {
  const { currentDate } = useAppSelector(state => state.dates);

  const weekly = useMemo(() => {
    return getThisWeek(currentDate);
  }, [currentDate]);

  return (
    <div className="flex ml-[50px]">
      {weekly.map((date, index) => {
        return (
          <div className="flex flex-1 flex-col pt-4" key={date.date}>
            <div
              className={`text-center text-sm ${
                date.isToday ? 'text-blue-500' : 'text-gray-500'
              }`}>
              {DAYS[index]}
            </div>
            <div
              className={`text-center text-2xl p-1 w-10 h-10 rounded-full m-auto flex justify-center items-center font-medium
            ${date.isToday ? 'bg-blue-500 text-white' : 'text-gray-500'}`}>
              {date.date}
            </div>
          </div>
        );
      })}
    </div>
  );
};

export default WeeklyCalenderHeader;