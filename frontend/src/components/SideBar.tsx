import ChannelList from '../components/ChannelList';
import MeetupList from '../components/MeetupList';

function SideBar() {
  return (
    <div className="SideBar">
      <ChannelList />
      <MeetupList />
    </div>
  );
}

export default SideBar;
