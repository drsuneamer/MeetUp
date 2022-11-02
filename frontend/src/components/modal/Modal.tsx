import React, { useCallback, useEffect, useMemo, useState } from 'react';
import { useAppDispatch, useAppSelector } from '../../stores/ConfigHooks';
import { setEventModalOpen } from '../../stores/modules/modal';
import { getStringDateFormat } from '../../utils/GetStringDateFormat';
import { createTimeOptions, Option } from '../../utils/CreateTimeOptions';
import { addEvent } from '../../stores/modules/events';
import { NewEvent } from '../../types/events';
import SingleSelect from '../common/SingleSelect';
import TextField from '@mui/material/TextField';
import Autocomplete from '@mui/material/Autocomplete';
import { setMyCalendar } from '../../stores/modules/mycalendar';

interface ChannelOptionType {
  title: string;
}

const channels = [
  { title: '서울_1반_팀장채널' },
  { title: 'A101' },
  { title: 'A102' },
  { title: 'A103' },
  { title: 'A104' },
  { title: 'A105' },
  { title: 'A106' },
  { title: 'A107' },
  { title: 'A102_scrum' },
  { title: 'A102_jira_bot' },
];

const EventModal = () => {
  const { eventModalIsOpen } = useAppSelector((state) => state.modal);
  const { eventModalData } = useAppSelector((state) => state.events);
  const dispatch = useAppDispatch();

  const [title, setTitle] = useState<string>('');
  const [date, setDate] = useState<string>(getStringDateFormat(new Date()));

  const startSelectOptions: Option[] = useMemo(() => createTimeOptions(), []);
  const [startTime, setStartTime] = useState<Option>(startSelectOptions[0]);
  const [startTimeIndex, setStartTimeIndex] = useState<number>(0);

  const endSelectOptions: Option[] = useMemo(() => createTimeOptions().slice(startTimeIndex), [startTimeIndex]);
  const [endTime, setEndTime] = useState<Option>(endSelectOptions[0]);
  const [endTimeIndex, setEndTimeIndex] = useState<number>(0);
  
  const { myCalendar } = useAppSelector((state) => state.mycalendar);
  
  useEffect(()=>{
    dispatch(setMyCalendar())
    console.log('내꺼', myCalendar)
  },[myCalendar])
  
  useEffect(() => {
    if (eventModalData !== null) {
      const { date, startTime } = eventModalData;
      setDate(date);

      const foundTimeIndex = startSelectOptions.findIndex((option) => option.value === startTime);

      foundTimeIndex !== undefined ? setStartTimeIndex(foundTimeIndex) : setStartTimeIndex(0);
    } else {
      handleResetInput();
    }
  }, [eventModalData]);

  useEffect(() => {
    setStartTime(startSelectOptions[startTimeIndex]);

    if (startTimeIndex > endTimeIndex) {
      setEndTimeIndex(startTimeIndex);
      setEndTime(startSelectOptions[startTimeIndex]);
    }
  }, [startTimeIndex]);

  const handleToggleModal = useCallback(() => {
    dispatch(setEventModalOpen());
  }, []);

  const handleSubmit = () => {
    const newEvent: NewEvent = {
      date,
      eventDetail: {
        title: title !== '' ? title : '제목 없음',
        start: startTime.value,
        end: endTime.value,
      },
    };

    dispatch(addEvent(newEvent));
    handleToggleModal();
    handleResetInput();
  };

  const handleResetInput = useCallback(() => {
    setTitle('');
    setDate(getStringDateFormat(new Date()));
    setStartTime(startSelectOptions[0]);
    setStartTimeIndex(0);
    setEndTime(endSelectOptions[0]);
    setEndTimeIndex(0);
  }, []);

  const handleStartSelectClick = useCallback((selected: Option, index?: number) => {
    const i = index as number;
    setStartTimeIndex(i);
    setStartTime(selected);
  }, []);

  const handleEndSelectClick = useCallback((selected: Option, index?: number) => {
    setEndTimeIndex(index as number);
    setEndTime(selected);
  }, []);

  const defaultProps = {
    options: channels,
    getOptionLabel: (option: ChannelOptionType) => option.title,
  };
  const flatProps = {
    options: channels.map((option) => option.title),
  };
  const [value, setValue] = React.useState<ChannelOptionType | null>(null);

  return (
    <div className={`${eventModalIsOpen ? 'fixed' : 'hidden'} w-[100%] h-[100%] flex justify-center items-center`}>
      <div
        className="w-[600px] h-[600px] flex flex-col items-center bg-background z-10 rounded drop-shadow-shadow"
        onClick={(e: React.MouseEvent<HTMLDivElement>) => {
          e.stopPropagation();
        }}
      >
        <svg
          onClick={handleToggleModal}
          xmlns="https://www.w3.org/2000/svg"
          fill="none"
          viewBox="0 0 24 24"
          strokeWidth="2.5"
          className="w-6 h-6 stroke-title mt-[15px] ml-[550px] cursor-pointer"
        >
          <path strokeLinecap="round" strokeLinejoin="round" d="M6 18L18 6M6 6l12 12" />
        </svg>
        <div>
          <div className={`${myCalendar ? 'mt-[50px]' : 'mt-[20px]'}`}>
            {myCalendar ? (
            <div className="text-s text-title font-bold">제목</div>
          ) : (
            <div className="text-s text-title font-bold">미팅명<span className="text-cancel">&#42;</span></div>
          )}
            <input
              type="text"
              name="title"
              value={title}
              onChange={(e) => setTitle(e.target.value)}
              className={`${myCalendar ? 'mb-[50px]' : 'mb-[0px]'} w-[450px] h-[30px] outline-none border-solid border-b-2 border-title focus:border-b-point active:border-b-point`}
            />
          </div>
          <div className="mt-[20px]">
            <div className="text-s text-title font-bold">
              날짜<span className="text-cancel">&#42;</span>
            </div>
            <input
              type="date"
              value={date}
              onChange={(e) => setDate(e.target.value)}
              className="w-[450px] h-[30px] outline-none border-solid border-b-2 border-title focus:border-b-point active:border-b-point"
            />
            <div className="mt-[20px]">
              <div className="text-s text-title font-bold">날짜<span className="text-cancel">&#42;</span></div>
              <input 
                type="date" 
                value={date} 
                onChange={(e) => setDate(e.target.value)} 
                className={`${myCalendar ? 'mb-[50px]' : 'mb-[0px]'} w-[450px] h-[30px] outline-none border-solid border-b-2 border-title focus:border-b-point active:border-b-point`}
              />
              <div className="mt-[20px]">
                <div className="text-s text-title font-bold">시간<span className="text-cancel">&#42;</span></div>
                <div className="flex items-center w-[450px] h-[30px] outline-none border-solid border-b-2 border-title focus:border-b-point active:border-b-point">
                  <SingleSelect className="text-sm w-[180px]" options={startSelectOptions} onChange={handleStartSelectClick} selected={startTime} />
                  <span className="mx-2">-</span>
                  <SingleSelect className="text-sm w-[180px]" options={endSelectOptions} onChange={handleEndSelectClick} selected={endTime} />
                  <svg
                    xmlns="http://www.w3.org/2000/svg"
                    fill="none"
                    viewBox="0 0 24 24"
                    strokeWidth="2.5"
                    stroke="currentColor"
                    className="w-7 h-7 ml-[181px]"
                  >
                    <path strokeLinecap="round" strokeLinejoin="round" d="M12 6v6h4.5m4.5 0a9 9 0 11-18 0 9 9 0 0118 0z" />
                  </svg>
                </div>
              </div>
              <div className="mt-[20px]">
                {myCalendar ? (
                  null
              ) : (
                <div>
                  <div className="text-s text-title font-bold">내용</div>
                 <input type="text" name="title" className="w-[450px] h-[30px] outline-none border-solid border-b-2 border-title focus:border-b-point active:border-b-point"/>
                </div>
              )}
              </div>
              <div className="mt-[20px]">
                {myCalendar ? (
                    null
                ) : (
                  <div>
                   <div className="text-s text-title font-bold">알림 보낼 채널</div>
                   <Autocomplete
                     className="w-[450px]"
                     ListboxProps={{ style: { maxHeight: '150px' } }}
                     {...defaultProps}
                     id="select-channel"
                     renderInput={(params) => (
                      <TextField {...params} label="채널 선택하기" variant="standard" />
                     )}
                    />
                  </div>
                )}
              </div>
            </div>
            {myCalendar ? (
               <button 
               onClick={handleSubmit}
               className="font-bold bg-title hover:bg-hover text-background mt-[70px] rounded w-[450px] h-s drop-shadow-button">밋업 불가시간 설정하기</button>
            ) : (
              <button 
              onClick={handleSubmit}
              className="font-bold bg-title hover:bg-hover text-background mt-[50px] rounded w-[450px] h-s drop-shadow-button">밋업 등록하기</button>
            )}
        </div>
      </div>
      <div
        className="w-[100%] h-[100%] fixed top:0 z-9 bg-[rgba(0,0,0,0.45)]"
        onClick={(e: React.MouseEvent) => {
          e.preventDefault();

          if (handleToggleModal) {
            handleToggleModal();
          }
        }}
      />
    </div>
  );
};

export default EventModal;
