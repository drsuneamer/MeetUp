import Sidebar from '../components/SideBar';
import Calendar from '../components/Calendar';
import Modal from '../components/modal/Modal';
import DetailModal from '../components/modal/DetailModal';
import WebexModal from '../components/modal/WebexModal';
import Layout from '../components/layout/Layout';
import EditModal from '../components/modal/EditModal';

const MeetUp = () => {
  return (
    <Layout>
      <div className={'h-screen flex flex-col overflow-y-scroll scrollbar-hide '}>
        <main className={'flex flex-1'}>
          <div className={'flex basis-3/12'}>
            <Sidebar />
          </div>
          <Calendar />
          {/* <Modal />
          <DetailModal />
          <EditModal />
          <WebexModal /> */}
        </main>

        <button
          onClick={() => window.open('https://meetup.gitbook.io/meetup-docs/', '_blank')}
          title="tutorial"
          className="fixed z-90 bottom-10 right-8 bg-primary w-12 h-12 rounded-full drop-shadow-lg flex justify-center items-center text-xl hover:bg-blue-700 hover:drop-shadow-2xl hover:animate-bounce duration-300 text-background"
        >
          &#63;
        </button>
      </div>
    </Layout>
  );
};

export default MeetUp;
