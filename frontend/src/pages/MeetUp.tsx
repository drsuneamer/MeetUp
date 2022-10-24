import Header from '../components/layout/Header';
// import SideCalendar from '../components/SideCalendar';
// import SideCalendarTitle from '../components/SideCalendarTitle';
import { useSelector } from 'react-redux';
import { currentCalendar } from '../stores/calendar';
import ScheduleCalendar from '../components/Calendar';
import getThisWeek from '../util/GetThisWeek';
import { useState } from 'react';
// import AddScheduleButton from '../components/AddScheduleButton';
import AddScheduleModal from '../components/AddScheduleModal';
import formatDay from '../util/FormatDay';
import SideBar from '../components/SideBar';

export default function MeetUp() {
  const { year, month, days } = useSelector(currentCalendar);
  const [isSideCalendar, setIsSideCalendar] = useState<boolean>(true);
  const [isOpenModal, setIsOpenModal] = useState<boolean>(false);
  const [isDeleteOpen, setIsDeleteOpen] = useState<boolean>(false);
  const [modalDate, setModalDate] = useState<string>(formatDay(new Date()));
  const [timeIndex, setTimeIndex] = useState<number>(0);

  return (
    <>
      {/* <Header year={year} month={month} isSideCalendar={isSideCalendar} setIsSideCalendar={setIsSideCalendar} /> */}
      <main className="flex h-[calc(100%_-_3.5rem)] flex-1">
        {/* <AddScheduleButton isSideCalendar={isSideCalendar} isOpenModal={isOpenModal} setIsOpenModal={setIsOpenModal} /> */}
        <div className={`p-5 flex flex-col mt-[65px] ${isSideCalendar ? 'block' : 'hidden'}`}>
          <SideBar />
        </div>
        <div className="flex flex-col h-full overflow-x-scroll flex-1 pr-2">
          <ScheduleCalendar
            days={getThisWeek(days)}
            setModalDate={setModalDate}
            setTimeIndex={setTimeIndex}
            setIsOpenModal={setIsOpenModal}
            isDeleteOpen={isDeleteOpen}
            setIsDeleteOpen={setIsDeleteOpen}
          />
        </div>
        <AddScheduleModal defaultDate={modalDate} timeIndex={timeIndex} isOpen={isOpenModal} setIsOpen={setIsOpenModal} />
      </main>
    </>
  );
}
