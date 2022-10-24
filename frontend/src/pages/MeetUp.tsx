import React from 'react';
import Header from '../components/layout/Header';
import Sidebar from '../components/SideBar';
import Calendar from '../components/Calendar';
import EventModal from '../components/EventModal';

const MeetUp = () => {
  return (
    <div className={'h-screen flex flex-col overflow-hidden'}>
      <main className={'flex flex-1'}>
        <Sidebar />
        <Calendar />
        <EventModal />
      </main>
    </div>
  );
};

export default MeetUp;