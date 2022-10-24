import ChannelList from '../components/ChannelList';
import MeetupList from '../components/MeetupList';

function SideBar() {
  return (
    <div className="SideBar w-full">
      <ChannelList />
      <MeetupList />
    </div>
  );
}

export default SideBar;
