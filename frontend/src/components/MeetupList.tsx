import MeetupListItem from './MeetupListItem';

function MeetupList() {
  return (
    <div className="MeetupList">
      <h1 className="text-xl font-bold mb-[10px]">참여중인 밋업</h1>
      <MeetupListItem />
    </div>
  );
}

export default MeetupList;
