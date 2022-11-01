import { useNavigate } from 'react-router-dom';
import { tMeetup } from '../types/channels';

interface MeetupListItemProps {
  meetup: tMeetup;
}


function MeetupListItem(meetup: MeetupListItemProps) {
  const navigate = useNavigate()

  const OthersCalendar =()=> {
    navigate(`/calendar/${meetup.meetup.id}`)
  }

  return (
    <div onClick={OthersCalendar} key={meetup.meetup.id} className="MeetupListItem w-full mb-1 drop-shadow-button -z-10">
      <div className="indexContext bg-offWhite w-full h-[40px] flex flex-wrap">
        <div className="indexLable bg-title w-3/12 h-[40px] flex justify-end ">
          <div className="bg-title mix-blend-multiply w-1/6 h-[40px]" />
        </div>
        <span className="MeetupName leading-[40px] w-9/12 text-center">{meetup.meetup.userName}</span>
      </div>
    </div>
);

}

export default MeetupListItem;
