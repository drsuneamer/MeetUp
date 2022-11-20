import Sidebar from '../components/SideBar';
import Calendar from '../components/Calendar';
import Modal from '../components/modal/Modal';
import DetailModal from '../components/modal/DetailModal';
import WebexModal from '../components/modal/WebexModal';
import Layout from '../components/layout/Layout';
import { axiosInstance } from '../components/auth/axiosConfig';
import { useNavigate } from 'react-router-dom';

import CircularProgress from '@mui/material/CircularProgress';

import EditModal from '../components/modal/EditModal';
import { useState, useEffect } from 'react';

const MeetUp = () => {
  const navigate = useNavigate();
  const [fab, setFab] = useState(false);

  const isFabClicked = () => {
    setFab(!fab);
  };

  const [syncChecked, setSyncChecked] = useState(false);
  const [isClicked, setIsClicked] = useState(false);

  useEffect(() => {
    const timeId = setTimeout(() => {
      setSyncChecked(false);
    }, 3000);

    return () => {
      clearTimeout(timeId);
    };
  }, [syncChecked]);

  const syncRequest = async () => {
    setIsClicked(true);
    await axiosInstance.get('/meetup/sync').then((res: any) => {
      if (res.status === 201) {
        // console.log('동기화 완료', res);
        setSyncChecked(true);
        setIsClicked(false);
        navigate(`/calendar/${localStorage.getItem('id')}`);
      }
    });
  };

  return (
    <Layout>
      <div className={'h-screen flex flex-col overflow-y-scroll scrollbar-hide '}>
        <main className={'flex flex-1'}>
          <div className={'flex basis-3/12 mr-7'}>
            <Sidebar />
          </div>
          <Calendar />
        </main>

        <div className="fab-container fixed z-[10000] bottom-10 right-8 ">
          <ul>
            <li className="float-right ml-2">
              {/* 메인 버튼 */}
              <button
                onClick={isFabClicked}
                className={`bg-primary w-12 h-12 rounded-full drop-shadow-lg flex justify-center items-center text-xl hover:bg-blue-700 hover:drop-shadow-2xl  duration-300 text-background ${
                  fab ? 'animate-bounce' : 'hover:animate-bounce'
                }`}
              >
                &#63;
              </button>
            </li>
            {/* 튜토리얼 */}
            <li className={`float-right ml-2 ${fab ? '' : 'hidden'}`}>
              <div
                onClick={() => window.open('https://meetup.gitbook.io/meetup-docs/', '_blank')}
                className="tutorial bg-point w-12 h-12 rounded-full drop-shadow-lg flex justify-center items-center text-xl hover:bg-blue-700 hover:drop-shadow-2xl animate-bounce-late text-background cursor-pointer"
              >
                <div className="absolute pb-[70px] text-body text-[10px] hidden ease-out hover:opacity-100">튜토리얼</div>
                <svg xmlns="https://www.w3.org/2000/svg" fill="none" viewBox="0 0 24 24" strokeWidth="1.5" stroke="currentColor" className="w-6 h-6">
                  <path
                    strokeLinecap="round"
                    strokeLinejoin="round"
                    d="M12 6.042A8.967 8.967 0 006 3.75c-1.052 0-2.062.18-3 .512v14.25A8.987 8.987 0 016 18c2.305 0 4.408.867 6 2.292m0-14.25a8.966 8.966 0 016-2.292c1.052 0 2.062.18 3 .512v14.25A8.987 8.987 0 0018 18a8.967 8.967 0 00-6 2.292m0-14.25v14.25"
                  />
                </svg>
              </div>
            </li>
            {/* mattermost와 동기화하기 */}
            <li className={`float-right ${fab ? '' : 'hidden'}`}>
              <div
                onClick={syncRequest}
                className="sync bg-title w-12 h-12 rounded-full drop-shadow-lg flex justify-center items-center text-xl hover:bg-blue-700 hover:drop-shadow-2xl animate-bounce duration-200 text-background cursor-pointer"
              >
                {!syncChecked && isClicked ? (
                  <CircularProgress sx={{ color: 'white' }} size="1.5rem" />
                ) : (
                  <div>
                    {syncChecked ? (
                      <div>
                        <svg
                          xmlns="https://www.w3.org/2000/svg"
                          fill="none"
                          viewBox="0 0 24 24"
                          strokeWidth="1.5"
                          stroke="currentColor"
                          className="w-6 h-6 animate-pulse"
                        >
                          <path strokeLinecap="round" strokeLinejoin="round" d="M4.5 12.75l6 6 9-13.5" />
                        </svg>
                      </div>
                    ) : (
                      <div>
                        <div className="absolute bottom-[10px] pb-[40px] w-full left-1 text-body text-[10px] hidden ease-out hover:opacity-100">
                          MM 동기화
                        </div>

                        <svg
                          xmlns="https://www.w3.org/2000/svg"
                          fill="none"
                          viewBox="0 0 24 24"
                          strokeWidth="1.5"
                          stroke="currentColor"
                          className="w-5 h-5"
                        >
                          <path
                            strokeLinecap="round"
                            strokeLinejoin="round"
                            d="M16.023 9.348h4.992v-.001M2.985 19.644v-4.992m0 0h4.992m-4.993 0l3.181 3.183a8.25 8.25 0 0013.803-3.7M4.031 9.865a8.25 8.25 0 0113.803-3.7l3.181 3.182m0-4.991v4.99"
                          />
                        </svg>
                      </div>
                    )}
                  </div>
                )}
              </div>
            </li>
          </ul>
        </div>
      </div>
    </Layout>
  );
};

export default MeetUp;
