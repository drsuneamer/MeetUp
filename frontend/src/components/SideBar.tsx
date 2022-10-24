import ChannelList from '../components/ChannelList';
import MeetupList from '../components/MeetupList';

function SideBar() {
  return (
    <div className="SideBar w-full mt-[80px]">
      <ChannelList />
      <MeetupList />
    </div>
  );
}

export default SideBar;
