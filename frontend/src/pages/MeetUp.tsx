import React from 'react';
import Sidebar from '../components/SideBar';
import Calendar from '../components/Calendar';
import Modal from '../components/modal/Modal'
import Layout from '../components/layout/Layout';

const MeetUp = () => {
  return (
    <Layout>
    <div className={'h-screen flex flex-col overflow-hidden '}>
      <main className={'flex flex-1'}>
        <div className={'flex basis-3/12'}>
          <Sidebar />
        </div>
        <Calendar />
        <Modal />
      </main>
    </div>
    </Layout>
  );
};

export default MeetUp;