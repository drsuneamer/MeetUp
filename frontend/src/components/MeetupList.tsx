import MeetupListItem from './MeetupListItem';
import { tMeetup } from '../types/channels';

let meetupArray: Array<tMeetup> = [
  {
    manager_id: 1,
    name: '이태희(컨설턴트)',
  },
  {
    manager_id: 2,
    name: '당현아[서울]실습코치',
  },
];

function MeetupList() {
  return (
    <div className="MeetupList">
      <h1 className="text-xl font-bold mb-[10px]">참여중인 달력</h1>
      {meetupArray.map((value: tMeetup, index: number) => (
        <MeetupListItem key={value.manager_id} meetup={value} />
      ))}
    </div>
  );
}

export default MeetupList;
