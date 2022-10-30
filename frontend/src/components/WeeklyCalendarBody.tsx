import React, { useEffect, useMemo, useRef, useState } from 'react';
import { getHours } from '../utils/GetHours';
import { useAppSelector, useAppDispatch } from '../stores/ConfigHooks';
import { getThisWeek } from '../utils/GetThisWeek';
import { deleteEvent, setEventModalData } from '../stores/modules/events';
import { setEventModalOpen } from '../stores/modules/modal';
import { SelectedEvent } from '../types/events';
import { useSelector, useDispatch } from 'react-redux';
import { holidaySelector, fetchHolidays } from '../stores/modules/holidays';

interface Week {
  name: string;
  date: string;
}

const WeeklyCalendarBody = () => {
  const { currentDate } = useAppSelector((state) => state.dates);
  const { events } = useAppSelector((state) => state.events);
  const dispatch = useAppDispatch();

  const rDispatch = useDispatch();
  const { holidays } = useSelector(holidaySelector);

  const [thisWeek, setThisWeek] = useState(Array<Week>);

  useEffect(() => {
    async function fetchAndSetHolidays() {
      await rDispatch(fetchHolidays());
    }
    fetchAndSetHolidays();
    renderHoliday();
  }, [holidays]);

  const weekly = useMemo(() => {
    return getThisWeek(currentDate);
  }, [currentDate]);

  const hours = useMemo(() => {
    return getHours();
  }, []);

  function renderHoliday() {
    const holidayThisWeek: Week[] = [];

    for (let i = 0; i < weekly.length; i++) {
      for (let j = 0; j < holidays.length; j++) {
        if (weekly[i].stringDate === holidays[j].locdate) {
          holidayThisWeek.push({ name: holidays[j].dateName, date: holidays[j].locdate });
        }
      }
    }

    if (holidayThisWeek.length !== 0) {
      setThisWeek(holidayThisWeek); // array of objects
    }
  }

  const deletePopupContainerRef = useRef<HTMLDivElement>(null);

  const [selectedEventPosition, setSelectedEventPosition] = useState<{
    top: number;
    left: number;
  } | null>(null);

  const [holidayPosition, setHolidayPosition] = useState<{ top: Number; left: Number } | null>(null);

  // const isHolidayThisWeek = ()

  const [selectedEvent, setSelectedEvent] = useState<SelectedEvent | null>(null);

  if (thisWeek.length > 0) {
    console.log(thisWeek[0].date);
  }
  const handleSelectedEvent = (e: React.MouseEvent<HTMLDivElement>, date: string, index: number) => {
    setSelectedEventPosition({ top: e.clientY, left: e.clientX });
    setSelectedEvent({ date, index });

    if (deletePopupContainerRef.current !== null) {
      deletePopupContainerRef.current.style.overflow = 'hidden';
    }
  };

  const handleNewEvent = (stringDate: string, hour: number, minute: string) => {
    const time = hour.toString() + minute;
    setSelectedEventPosition(null);
    dispatch(setEventModalData({ date: stringDate, startTime: time }));
    dispatch(setEventModalOpen());
  };

  const handleDeleteEvent = () => {
    if (selectedEvent !== null) {
      dispatch(deleteEvent(selectedEvent));
    }
    setSelectedEvent(null);
    setSelectedEventPosition(null);

    if (deletePopupContainerRef.current !== null) {
      deletePopupContainerRef.current.style.overflow = 'scroll';
    }
  };

  return (
    <div ref={deletePopupContainerRef} className="calendar-body flex flex-1 max-h-[calc(100vh-9.3rem)] overflow-y-scroll scrollbar-hide">
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
              {/* 여기서 if? */}
              {thisWeek.length > 0
                ? thisWeek.map((element, index) => {
                    const top = 0;
                    const height = 24 * 50;
                    {
                      if (element.date === stringDate)
                        return (
                          <>
                            <div
                              key={`${element.date}${index}`}
                              style={{ top, height }}
                              className={`flex flex-wrap items-center absolute w-full text-background overflow-y-auto bg-label rounded p-1 text-[13px]`}
                            >
                              {element.name}
                            </div>
                          </>
                        );
                    }
                  })
                : null}
              {hours.map((hour, index) => {
                return (
                  <div>
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
                  </div>
                );
              })}
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

                console.log(thisWeek);

                return (
                  <div>
                    {thisWeek.length > 0 ? (
                      <div
                        key={`${thisWeek[0].date}${index}`}
                        style={{ top, height }}
                        className={`flex flex-wrap items-center absolute w-full text-background overflow-y-auto bg-lable rounded p-1 text-[13px] cursor-pointer`}
                      >
                        <div className="mr-1">{title}</div>
                      </div>
                    ) : (
                      <div
                        key={`${stringDate}${index}`}
                        style={{ top, height }}
                        className={`flex flex-wrap items-center absolute w-full text-background overflow-y-auto bg-title rounded p-1 text-[13px] cursor-pointer`}
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
                    )}
                  </div>
                );
              })}
            </div>
          );
        })}
      </div>

      {selectedEventPosition !== null && (
        <div
          className="fixed text-sm shadow rounded-lg bg-background cursor-pointer px-4 py-2"
          style={selectedEventPosition}
          onClick={handleDeleteEvent}
        >
          삭제
        </div>
      )}
    </div>
  );
};

export default WeeklyCalendarBody;
