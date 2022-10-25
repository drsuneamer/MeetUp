import ChannelList from '../components/ChannelList';
import MeetupList from '../components/MeetupList';

function SideBar() {
  return (
    <div className="SideBar w-full pl-2 mt-[70px]">
      <ChannelList />
      <MeetupList />
    </div>
  );
}

export default SideBar;
