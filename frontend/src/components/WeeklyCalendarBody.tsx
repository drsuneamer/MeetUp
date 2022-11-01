import React, { useEffect, useMemo, useRef, useState } from 'react';
import { getHours } from '../utils/GetHours';
import { useAppSelector, useAppDispatch } from '../stores/ConfigHooks';
import { getThisWeek } from '../utils/GetThisWeek';
import { getSundayOfWeek } from '../utils/GetSundayOfWeek';
import { deleteEvent, setEventModalData } from '../stores/modules/events';
import { setEventModalOpen, ModalSelector } from '../stores/modules/modal';
import { setDetailModalOpen } from '../stores/modules/modal';
import { SelectedEvent } from '../types/events';
import { useSelector, useDispatch } from 'react-redux';
import { holidaySelector, fetchHolidays } from '../stores/modules/holidays';
import { scheduleSelector, fetchMySchedule } from '../stores/modules/schedules';
import _ from 'lodash';

interface Week {
  name: string;
  date: string;
}

const WeeklyCalendarBody = () => {
  const detailModalSelector = useSelector(ModalSelector);

  const { currentDate } = useAppSelector((state) => state.dates);
  const { events } = useAppSelector((state) => state.events);
  const dispatch = useAppDispatch();

  const rDispatch = useDispatch();
  const { holidays } = useSelector(holidaySelector);
  const [holidayThisWeek, setHolidayThisWeek] = useState(Array<Week>);

  
  const userId = 
  
  useEffect(() => {
    async function fetchAndSetHolidays() {
      await rDispatch(fetchHolidays());
    }
    fetchAndSetHolidays();
    renderHoliday();
    dispatch(getSundayOfWeek);
    dispatch(fetchMySchedule('d'))

  }, [holidays, currentDate]);

  const weekly = useMemo(() => {
    return getThisWeek(currentDate);
  }, [currentDate]);

  const hours = useMemo(() => {
    return getHours();
  }, []);

  function renderHoliday() {
    const holidayResult: Week[] = [];

    for (let week of weekly) {
      for (let holiday of holidays) {
        if (week.stringDate === holiday.locdate) {
          holidayResult.push({ name: holiday.dateName, date: holiday.locdate });
        }
      }
    }

    if (!_.isEmpty(holidayResult)) {
      setHolidayThisWeek(holidayResult); // array of objects
    }
  }

  const deletePopupContainerRef = useRef<HTMLDivElement>(null);

  const [selectedEventPosition, setSelectedEventPosition] = useState<{
    top: number;
    left: number;
  } | null>(null);

  const [selectedEvent, setSelectedEvent] = useState<SelectedEvent | null>(null);

  const handleSelectedEvent = (e: React.MouseEvent<HTMLDivElement>, date: string, index: number) => {
    setSelectedEventPosition({ top: e.clientY, left: e.clientX });
    setSelectedEvent({ date, index });

    // if (deletePopupContainerRef.current !== null) {
    //   deletePopupContainerRef.current.style.overflow = 'hidden';
    // }
  };

  const handleNewEvent = (stringDate: string, hour: number, minute: string) => {
    const time = hour.toString() + minute;
    setSelectedEventPosition(null);
    dispatch(setEventModalData({ date: stringDate, startTime: time }));
    dispatch(setEventModalOpen());
  };

  const handleViewEvent = () => {
    if (selectedEvent !== null) {
      dispatch(setDetailModalOpen());
      // setSelectedEventPosition(selectedEventPosition);
    }
  };

  // const handleDeleteEvent = () => {
  //   if (selectedEvent !== null) {
  //     dispatch(deleteEvent(selectedEvent));
  //   }
  //   setSelectedEvent(null);
  //   setSelectedEventPosition(null);

  //   if (deletePopupContainerRef.current !== null) {
  //     deletePopupContainerRef.current.style.overflow = 'scroll';
  //   }
  // };

  return (
    <div ref={deletePopupContainerRef} className="calendar-body flex flex-1 max-h-[calc(100vh-9.3rem)] overflow-y-scroll scrollbar-hide pb-10">
      <div className="flex flex-col h-fit">
        {hours.map((hour, index) => {
          return (
            <div className="text-label text-xs h-[50px] text-right pr-2" key={index}>
              {hour}
            </div>
          );
        })}
      </div>
      <div className="flex flex-1 h-fit p-2 md:ml-0">
        {weekly.map(({ date, stringDate }) => {
          return (
            <div className="flex flex-1 flex-col relative" key={`${date}-border`}>
              {/* 여기서 holiday check */}
              {holidayThisWeek.length > 0
                ? holidayThisWeek.map((element, index) => {
                    const top = 0;
                    const height = 24 * 50;
                    if (element.date === stringDate)
                      return (
                        <div
                          key={`${element.date}${index}`}
                          style={{ top, height }}
                          className={`flex flex-wrap absolute w-full overflow-y-auto bg-line rounded p-1 text-[16px] border-solid border-background border-2`}
                        >
                          <span key={`${element.name}`} className={`w-full text-center text-cancel font-medium pt-2`}>
                            {element.name}
                          </span>
                        </div>
                      );
                    return null;
                  })
                : null}
              {hours.map((hour, index) => {
                return (
                  <div
                    key={`${hour}${index}`}
                    className="border-1 border-t border-l h-[50px] border-line hover:bg-line"
                    onClick={(e: React.MouseEvent<HTMLDivElement>) => {
                      const rect = e.currentTarget.getBoundingClientRect();
                      const y = e.clientY - rect.top;
                      let minute = '00';
                      if (y > 50 / 2) {
                        minute = '30';
                      }
                      handleNewEvent(stringDate, index, minute);
                    }}
                  />
                );
              })}
              {selectedEventPosition !== null && (
                <div
                  className="fixed text-sm shadow rounded-lg bg-background cursor-pointer px-4 py-2"
                  style={selectedEventPosition}
                  onClick={(e: React.MouseEvent<HTMLDivElement>) => {
                    handleViewEvent();
                  }}
                />
              )}
              {events[stringDate]?.map((event, index) => {
                const { title, start, end } = event;
                const startMinute = parseInt(start.slice(-2));
                const startHour = parseInt(start.slice(0, start.length - 2));

                const endMinute = parseInt(end.slice(-2));
                const endHour = parseInt(end.slice(0, end.length - 2));

                const top = startHour * 50 + startMinute;
                let height = (endHour - startHour) * 50 + (endMinute - startMinute);

                if (height < 24) {
                  height = 24;
                }

                return (
                  <div
                    key={`${stringDate}${index}`}
                    style={{ top, height }}
                    className={`flex flex-wrap items-center absolute w-full text-background overflow-y-auto bg-title rounded p-1 text-[13px] cursor-pointer border-2 border-solid border-background`}
                    onClick={(e: React.MouseEvent<HTMLDivElement>) => handleSelectedEvent(e, stringDate, index)}
                  >
                    <div className="mr-1">{title}</div>
                    <div className="hidden sm:block">
                      <span>
                        {startHour < 12 ? '오전' : '오후'} {startHour !== 0 ? startHour : 12}
                      </span>
                      <span>{startMinute !== 0 && `:${startMinute}`}</span>
                      <span> ~ </span>
                      <span>
                        {endHour < 12 ? '오전' : '오후'} {endHour !== 0 ? endHour : 12}
                      </span>
                      <span>{endMinute !== 0 && `:${endMinute}`}</span>
                    </div>
                  </div>
                );
              })}
            </div>
          );
        })}
      </div>

      {selectedEventPosition !== null && (
        <div
          className="fixed text-sm shadow rounded bg-background cursor-pointer px-2 py-2"
          style={selectedEventPosition}
          onClick={(e: React.MouseEvent<HTMLDivElement>) => {
            handleViewEvent();
          }}
        >
          자세히 보기
        </div>
      )}
    </div>
  );
};

export default WeeklyCalendarBody;
