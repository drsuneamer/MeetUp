import { useState, useEffect, useMemo } from 'react';
import Button from './common/Button';
import IconLeft from './common/IconLeft';
import IconRight from './common/IconRight';
import { useAppSelector, useAppDispatch } from '../stores/ConfigHooks';
import { setCurrentDate, setToday } from '../stores/modules/dates';
import { setMyCalendar } from '../stores/modules/mycalendar';
// import { calendarSelector } from '../stores/modules/meetups';
import { axiosInstance } from './auth/axiosConfig';
import { useParams } from 'react-router-dom';
import webexIcon from '../assets/webex_icon.png';
import { setWebexModalOpen } from '../stores/modules/modal';

const Header = () => {
  const params = useParams();
  const { currentDate } = useAppSelector((state) => state.dates);
  const dispatch = useAppDispatch();
  // const [isMyCalendar, setIsMyCalendar] = useState(false)
  const { myCalendar } = useAppSelector((state) => state.mycalendar);

  useEffect(() => {
    if (params.userId === `${localStorage.getItem('id')}`) {
      // setIsMyCalendar(true)
      dispatch(setMyCalendar());
    }
  }, [window.location.href]);

  const [nickname, setNickname] = useState('');

  useEffect(() => {
    const userId = params.userId;
    axiosInstance.get(`/user/nickname/${userId}`).then((res) => {
      setNickname(res.data);
    });
  }, [nickname]);

  const displayDate = useMemo(() => {
    const date = new Date(currentDate);

    const firstDayOfTheMonth = date.getDay();

    if (date.getDate() <= firstDayOfTheMonth) {
      return `${date.getFullYear()}년 ${date.getMonth()}월 ~ ${date.getMonth() + 1}월`;
    } else {
      return `${date.getFullYear()}년 ${date.getMonth() + 1}월`;
    }
  }, [currentDate]);

  const handleTodayBtnClick = () => {
    dispatch(setToday());
  };

  const handlePrevWeek = () => {
    const prevWeek = new Date(currentDate);
    prevWeek.setDate(new Date(currentDate).getDate() - 7);

    dispatch(setCurrentDate(prevWeek.toLocaleDateString()));
  };

  const handleNextWeek = () => {
    const nextWeek = new Date(currentDate);
    nextWeek.setDate(new Date(currentDate).getDate() + 7);

    dispatch(setCurrentDate(nextWeek.toLocaleDateString()));
  };

  // 웹엑스 모달 온오프
  const handleWebexModal = () => {
    dispatch(setWebexModalOpen());
  };

  return (
    <>
      <header className="flex flex-col relative items-center px-4 justify-center w-full h-[80px] mt-[50px]">
        <img src={webexIcon} alt="webex-icon" className="absolute left-0 w-9 cursor-pointer" onClick={handleWebexModal} />
        {myCalendar ? (
          <div className="absolute left-10 bg-point py-1 px-8 drop-shadow-button rounded text-background">나의 캘린더</div>
        ) : (
          <div className="absolute left-10 bg-point py-1 px-8 drop-shadow-button rounded text-background">{nickname}의 캘린더</div>
        )}
        <div className="flex items-center">
          <Button className="p-1 sm:mx-1 hover:bg-line hover:rounded-full" onClick={handlePrevWeek}>
            <IconLeft className="w-8 h-8" />
          </Button>

          <h2 className="text-md sm:text-xl text-body">{displayDate}</h2>

          <Button className="p-1 sm:mx-1 hover:bg-line hover:rounded-full" onClick={handleNextWeek}>
            <IconRight className="w-8 h-8" />
          </Button>
        </div>
        <Button onClick={handleTodayBtnClick} className="group rounded px-3 mr-5 ml-5 hover:bg-hover bg-primary text-[white]">
          오늘
          {/* <p className="absolute text-body rounded hidden text-center group-hover:block">{getStringDateFormat(new Date(), '-')}</p> */}
        </Button>
      </header>
    </>
  );
};

export default Header;
