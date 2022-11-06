import MeetupListItem from './MeetupListItem';
import { tMeetup } from '../types/channels';
import Spinner from './common/Spinner';
import { useAppDispatch } from '../stores/ConfigHooks';
import { useEffect } from 'react';
import { fetchCalendarList, calendarSelector } from '../stores/modules/meetups';
import { useSelector } from 'react-redux';

function MeetupList() {
  const dispatch = useAppDispatch();
  const calendar = useSelector(calendarSelector);

  useEffect(() => {
    dispatch(fetchCalendarList());
  }, []);

  if (!calendar.loading) {
    return <Spinner />;
  }
  if (!calendar.calendars) {
    return null;
  }
  return (
    <div className="MeetupList">
      <h1 className="text-xl font-bold mb-[10px]">참여중인 달력</h1>
      {calendar.calendars.map((value: tMeetup, index: number) => (
        <MeetupListItem key={index} meetup={value} />
      ))}
    </div>
  );
}

export default MeetupList;
