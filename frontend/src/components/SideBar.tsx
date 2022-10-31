import ChannelList from '../components/ChannelList';
import MeetupList from '../components/MeetupList';
import axios from 'axios';
import { useNavigate } from 'react-router-dom';
import Alert from '@mui/material/Alert';
import { useState, useEffect } from 'react';

function SideBar() {

  const navigate = useNavigate()
  const [syncChecked, setSyncChecked] = useState(false)

  useEffect(() => {
    const timeId = setTimeout(() => {
      setSyncChecked(false)
    }, 2000)

    return () => {
      clearTimeout(timeId)
    }
  }, [syncChecked]);

  const syncRequest = async () => {
    await axios.get('http://localhost:8080/meetup/sync', {
    headers: {
      Authorization: `Bearer ${window.localStorage.getItem('accessToken')}`,
    },
  }).then((res) => {
      if (res.status === 201) {
        console.log('동기화 완료', res)
        setSyncChecked(true)
        navigate('/');
      }
    });
  };

  return (
    <div className="SideBar relative w-full pl-2 mt-[70px] -z-10">
      <ChannelList />
      <MeetupList />

      <button onClick={syncRequest} className="bg-title hover:bg-hover text-background rounded w-full h-s drop-shadow-button flex justify-center items-center space-x-2 absolute bottom-24">
        <svg xmlns="http://www.w3.org/2000/svg" fill="none" viewBox="0 0 24 24" strokeWidth="1.5" stroke="currentColor" className="w-6 h-6">
          <path strokeLinecap="round" strokeLinejoin="round" d="M16.023 9.348h4.992v-.001M2.985 19.644v-4.992m0 0h4.992m-4.993 0l3.181 3.183a8.25 8.25 0 0013.803-3.7M4.031 9.865a8.25 8.25 0 0113.803-3.7l3.181 3.182m0-4.991v4.99" />
        </svg>
        <span> MatterMost와 동기화하기</span>
      </button>
      { syncChecked ? 
      <Alert severity="info" className="mt-32 text-[13px]">
        동기화가 완료되었습니다
      </Alert> :
      null
      }

    </div>
  );
}

export default SideBar;
