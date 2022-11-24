import { useState, useEffect, useMemo } from 'react';
import Button from './common/Button';
import IconLeft from './common/IconLeft';
import IconRight from './common/IconRight';
import { useAppSelector, useAppDispatch } from '../stores/ConfigHooks';
import { setCurrentDate, setToday } from '../stores/modules/dates';
import { setMyCalendar, toCurrentTime } from '../stores/modules/mycalendar';
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

  const toTime = () => {
    dispatch(toCurrentTime(true));
  };

  const userId = window.localStorage.getItem('id');
  const url = `/calendar/${userId}`;

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
          <div className="absolute left-10 text-title font-semibold py-1 px-2">나의 캘린더</div>
        ) : (
          <>
            <div className="absolute left-10 text-center absolute text-title font-semibold py-1 px-2 truncate">{nickname}의 캘린더</div>
            <a
              href={url}
              className="absolute right-3 top-6 text-center py-1 px-4 drop-shadow-button rounded text-background truncate bg-point font-semibold cursor-pointer"
            >
              🗓️ 내 캘린더로 돌아가기
            </a>
          </>
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
        <div className="flex justify-center items-center text-center gap-1">
          <Button onClick={handleTodayBtnClick} className="group rounded px-3 hover:bg-hover bg-primary text-[white]">
            오늘
            {/* <p className="absolute text-body rounded hidden text-center group-hover:block">{getStringDateFormat(new Date(), '-')}</p> */}
          </Button>
          <Button onClick={toTime} className="group rounded px-3 hover:bg-hover bg-primary text-[white]">
            현재시간
            {/* <p className="absolute text-body rounded hidden text-center group-hover:block">{getStringDateFormat(new Date(), '-')}</p> */}
          </Button>
        </div>
      </header>
    </>
  );
};

export default Header;
