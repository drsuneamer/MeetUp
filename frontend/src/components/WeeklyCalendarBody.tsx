import React, { useEffect, useMemo, useRef, useState } from 'react';
import { getHours } from '../utils/GetHours';
import { getNow } from '../utils/GetNow';
import { useAppSelector, useAppDispatch } from '../stores/ConfigHooks';
import { getThisWeek } from '../utils/GetThisWeek';
import { getSundayOfWeek } from '../utils/GetSundayOfWeek';
import { setEventModalData } from '../stores/modules/events';
import { setEventModalOpen } from '../stores/modules/modal';
import { setDetailModalOpen } from '../stores/modules/modal';
import { toCurrentTime } from '../stores/modules/mycalendar';
import { SelectedEvent, tSchedule } from '../types/events';
import { useSelector } from 'react-redux';
import { holidaySelector, fetchHolidays } from '../stores/modules/holidays';
import { myScheduleSelector, meetingFromMeSelector, meetingToMeSelector, fetchSchedule, fetchScheduleDetail } from '../stores/modules/schedules';
import _ from 'lodash';
import { useParams } from 'react-router-dom';
import { myCalendarSelector } from '../stores/modules/mycalendar';

interface Week {
  name: string;
  date: string;
}

const WeeklyCalendarBody = () => {
  const { currentDate } = useAppSelector((state) => state.dates);
  const { events } = useAppSelector((state) => state.events);
  const { holidays } = useSelector(holidaySelector);
  const mySchedule = useSelector(myScheduleSelector);
  const meetingToMe = useSelector(meetingToMeSelector);
  const meetingFromMe = useSelector(meetingFromMeSelector);

  const dispatch = useAppDispatch();

  const [holidayThisWeek, setHolidayThisWeek] = useState(Array<Week>);

  const param = useParams();

  const weekly = useMemo(() => {
    return getThisWeek(currentDate);
  }, [currentDate]);

  const hours = useMemo(() => {
    return getHours();
  }, []);

  const nows = useMemo(() => {
    return getNow();
  }, []);

  const userId = param.userId;

  const sunday = useMemo(() => {
    const date = new Date(currentDate);
    const firstDayOfTheMonth = date.getDay();

    if (date.getDate() <= firstDayOfTheMonth) {
      if (weekly[0].date < 10) {
        if (date.getMonth() + 1 < 10) {
          return `${date.getFullYear()}-0${date.getMonth()}-0${weekly[0].date}`;
        }
        return `${date.getFullYear()}-${date.getMonth()}-0${weekly[0].date}`;
      } else {
        if (date.getMonth() + 1 < 10) {
          return `${date.getFullYear()}-0${date.getMonth()}-${weekly[0].date}`;
        }
        return `${date.getFullYear()}-${date.getMonth()}-${weekly[0].date}`;
      }
    } else {
      if (weekly[0].date < 10) {
        if (date.getMonth() + 1 < 10) {
          return `${date.getFullYear()}-0${date.getMonth() + 1}-0${weekly[0].date}`;
        }
        return `${date.getFullYear()}-${date.getMonth() + 1}-0${weekly[0].date}`;
      } else {
        if (date.getMonth() + 1 < 10) {
          return `${date.getFullYear()}-0${date.getMonth() + 1}-${weekly[0].date}`;
        }
        return `${date.getFullYear()}-${date.getMonth() + 1}-${weekly[0].date}`;
      }
    }
  }, [currentDate]);

  const thunkAPI = [userId, sunday];

  const myCalendar = useAppSelector(myCalendarSelector);

  const scrollRef = useRef<HTMLDivElement>(null);

  const moveScroll = () => {
    scrollRef.current?.scrollIntoView({ behavior: 'smooth', block: 'center', inline: 'start' });
    // window.scrollTo({ top: Number(scrollRef.current?.style.top.slice(0, -2)), behavior: 'smooth' });
  };

  useEffect(() => {
    if (myCalendar.mycalendar.currentTime) {
      // console.log(myCalendar.mycalendar.currentTime);
      moveScroll();
      dispatch(toCurrentTime(false));
    }
  }, [myCalendar.mycalendar.currentTime]);
  useEffect(() => {
    async function fetchAndSetHolidays() {
      await dispatch(fetchHolidays());
    }

    // console.log('sunday of this week', getSundayOfWeek())

    fetchAndSetHolidays();
    renderHoliday();
    if (userId && sunday) {
      dispatch(fetchSchedule(thunkAPI));
    }
  }, [holidays, currentDate]);

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

  const handleViewEvent = (id: string, prop: string) => {
    if (prop === 'myCalendar') {
      dispatch(fetchScheduleDetail(id));
      dispatch(setDetailModalOpen('myCalendar'));
    } else {
      dispatch(fetchScheduleDetail(id));
      dispatch(setDetailModalOpen('myMeeting'));
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
              {/* 나의 스케쥴(회색으로 블락) */}
              {mySchedule.map((element: tSchedule, index: number) => {
                const startMinute = parseInt(element.start.slice(-5, -3));
                const startHour = parseInt(element.start.slice(-8, -5));
                const endMinute = parseInt(element.end.slice(-5, -3));
                const endHour = parseInt(element.end.slice(-8, -5));

                const top = startHour * 50 + startMinute;
                let height = (endHour - startHour) * 50 + (endMinute - startMinute);

                const scheduleDate = element.start.slice(0, 10);
                const scheduleId = element.id;
                const ownerId = element.userId;
                const myId = localStorage.getItem('id');

                if (scheduleDate === stringDate && ownerId === myId)
                  return (
                    <div
                      key={`${scheduleDate}${index}`}
                      style={{ top, height }}
                      className={`flex flex-wrap absolute w-full overflow-y-auto bg-line rounded p-1 text-[12px] border-solid border-background border-2 cursor-pointer scrollbar-hide`}
                      onClick={(e: React.MouseEvent<HTMLDivElement>) => {
                        handleViewEvent(scheduleId, 'myCalendar');
                      }}
                    >
                      <span key={`${element.id}`} className={`w-full text-center text-label font-medium pt-2`}>
                        {element.title}
                      </span>
                    </div>
                  );
                else if (scheduleDate === stringDate && ownerId !== myId) {
                  return (
                    <div
                      key={`${scheduleDate}${index}`}
                      style={{ top, height }}
                      className={`flex flex-wrap absolute w-full overflow-y-auto bg-line rounded p-1 text-[12px] border-solid border-background border-2 scrollbar-hide`}
                    >
                      <span key={`${element.id}`} className={`w-full text-center text-label font-medium pt-2`}>
                        {element.title ? element.title : '비공개'}
                      </span>
                    </div>
                  );
                }
                return null;
              })}
              {/* 나에게 신청한 미팅(컨설턴트 입장) */}
              {meetingToMe.map((element: tSchedule, index: number) => {
                const startMinute = parseInt(element.start.slice(-5, -3));
                const startHour = parseInt(element.start.slice(-8, -5));
                const endMinute = parseInt(element.end.slice(-5, -3));
                const endHour = parseInt(element.end.slice(-8, -5));

                const top = startHour * 50 + startMinute;
                let height = (endHour - startHour) * 50 + (endMinute - startMinute);

                const scheduleDate = element.start.slice(0, 10);
                const meetingId = element.id;

                if (scheduleDate === stringDate && element.open)
                  return (
                    <div
                      key={`${scheduleDate}${index}`}
                      style={{ top: top, height: height, background: `${element.meetupColor}` }}
                      className={`flex flex-wrap absolute w-full overflow-y-auto rounded p-1 text-[12px] border-solid border-background border-2 cursor-pointer scrollbar-hide`}
                      onClick={(e: React.MouseEvent<HTMLDivElement>) => {
                        handleViewEvent(meetingId, 'myMeeting');
                      }}
                    >
                      <span key={`${element.id}`} className={`w-full text-center text-body font-medium pt-2`}>
                        {element.title}
                      </span>
                    </div>
                  );
                else if (scheduleDate === stringDate && !element.open)
                  return (
                    <div
                      key={`${scheduleDate}${index}`}
                      style={{ top, height }}
                      className={`flex flex-wrap absolute w-full overflow-y-auto rounded p-1 text-[12px] border-solid border-background border-2 bg-line scrollbar-hide`}
                    >
                      <span key={`${element.id}`} className={`w-full text-center text-body font-medium pt-2`}>
                        비공개
                      </span>
                    </div>
                  );
                return null;
              })}
              {/* 내가 신청한 미팅(다른 컨설턴트/코치에게) */}
              {meetingFromMe.map((element: tSchedule, index: number) => {
                const startMinute = parseInt(element.start.slice(-5, -3));
                const startHour = parseInt(element.start.slice(-8, -5));
                const endMinute = parseInt(element.end.slice(-5, -3));
                const endHour = parseInt(element.end.slice(-8, -5));

                const top = startHour * 50 + startMinute;
                let height = (endHour - startHour) * 50 + (endMinute - startMinute);

                const scheduleDate = element.start.slice(0, 10);
                const meetingId = element.id;

                if (scheduleDate === stringDate)
                  return (
                    <div
                      key={`${scheduleDate}${index}`}
                      style={{ top, height }}
                      className={`flex flex-wrap absolute w-full overflow-y-auto bg-title rounded p-1 text-[12px] border-solid border-background border-2 cursor-pointer scrollbar-hide`}
                      onClick={(e: React.MouseEvent<HTMLDivElement>) => {
                        handleViewEvent(meetingId, 'myMeeting');
                      }}
                    >
                      <span key={`${element.id}`} className={`w-full text-center text-background font-medium pt-2`}>
                        {element.title}
                      </span>
                    </div>
                  );
                return null;
              })}
              {hours.map((hour, index) => {
                return (
                  <div
                    key={`${hour}${index}`}
                    className="border-1 border-t border-l h-[50px] border-line hover:bg-line "
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
              {hours.map((hour, index) => {
                if (nows) {
                  const top = nows.hours * 50 + nows.minutes * (5 / 6);
                  let height = 0;

                  if (hour === nows.parsedTimeNow) {
                    return (
                      <div
                        ref={scrollRef}
                        key={`${nows}${index}`}
                        id="current"
                        style={{ top, height }}
                        className="absolute w-full h-[1.5px] bg-primary"
                      />
                    );
                  }
                }
                return null;
              })}
              {selectedEventPosition !== null && (
                <div
                  className="fixed text-sm shadow rounded-lg bg-background px-4 py-2"
                  style={selectedEventPosition}
                  onClick={(e: React.MouseEvent<HTMLDivElement>) => {
                    // handleViewEvent('');
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
                    className={`flex flex-wrap items-center absolute w-full text-background overflow-y-auto bg-title rounded p-1 text-[13px] cursor-pointer border-2 border-solid border-background scrollbar-hide`}
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

      {/* {selectedEventPosition !== null && (
        <div
          className="fixed text-sm shadow rounded bg-background cursor-pointer px-2 py-2"
          style={selectedEventPosition}
          onClick={(e: React.MouseEvent<HTMLDivElement>) => {
            handleViewEvent();
          }}
        >
          자세히 보기
        </div>
      )} */}
    </div>
  );
};

export default WeeklyCalendarBody;
