import ChannelList from '../components/ChannelList';
import MeetupList from '../components/MeetupList';
import Alert from '@mui/material/Alert';
import { useEffect, useState } from 'react';

function SideBar() {
  const [isStudent, setIsStudent] = useState(false);

  const checkRole = () => {
    if (localStorage.getItem('roleType') === 'ROLE_Student') {
      setIsStudent(true);
    }
  };

  useEffect(() => {
    checkRole();
  }, []);

  return (
    <div className="SideBar relative w-full pl-2 mt-[70px] min-w-[200px]">
      {isStudent ? <div /> : <ChannelList />}
      <MeetupList />
    </div>
  );
}

export default SideBar;
