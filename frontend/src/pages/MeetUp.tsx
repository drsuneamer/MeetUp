import React from 'react';
import Sidebar from '../components/SideBar';
import Calendar from '../components/Calendar';
import EventModal from '../components/EventModal';

const MeetUp = () => {
  return (
    <div className={'h-screen flex flex-col overflow-hidden m-3'}>
      <main className={'flex flex-1'}>
        <div className={'flex basis-3/12'}>
          <Sidebar />
        </div>
        <Calendar />
        <EventModal />
      </main>
    </div>
  );
};

export default MeetUp;