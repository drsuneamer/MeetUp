import React, { useCallback, useEffect, useMemo, useState } from 'react';
import { useAppDispatch, useAppSelector } from '../../stores/ConfigHooks';
import { setEventModalOpen } from '../../stores/modules/modal';
import { getStringDateFormat } from '../../utils/GetStringDateFormat';
import { createTimeOptions, Option } from '../../utils/CreateTimeOptions';
import SingleSelect from '../common/SingleSelect';
import TextField from '@mui/material/TextField';
import Autocomplete from '@mui/material/Autocomplete';
import { useParams } from 'react-router-dom';
import { isFulfilled } from '@reduxjs/toolkit';
import { addSchedule } from '../../stores/modules/schedules';
import { addMeeting } from '../../stores/modules/schedules';
import { alarmChannelSelector, fetchAlarmChannelList } from '../../stores/modules/channelAlarm';
import { tAlarm } from '../../types/channels';
import Switch from '@mui/material/Switch';

const EventModal = () => {
  const channels = useAppSelector(alarmChannelSelector);
  const { eventModalIsOpen } = useAppSelector((state) => state.modal);
  const { eventModalData } = useAppSelector((state) => state.events);
  
  const dispatch = useAppDispatch();
  const [title, setTitle] = useState<string>('');
  const [date, setDate] = useState<string>(getStringDateFormat(new Date()));
  const [content, setContent] = useState<string>('');
  const [alarmChannelId, setAlarmChannelId] = useState<number>(0);

  const startSelectOptions: Option[] = useMemo(() => createTimeOptions(), []);
  const [startTimeIndex, setStartTimeIndex] = useState<number>(0);

  const [startTime, setStartTime] = useState<Option>(startSelectOptions[0]);
  const startTimeValue = startTime.value;

  const newStartTime = () => {
    if (startTimeValue.length === 3) {
      const startTimeNewValue = '0' + startTimeValue;
      const hour = startTimeNewValue.slice(0, 2) + ':';
      const minute = startTimeNewValue.slice(2, 4) + ':';
      const startTimeResult = hour + minute + '00';
      const start = date + ' ' + startTimeResult;
      return start;
    }
    const hour = startTimeValue.slice(0, 2) + ':';
    const minute = startTimeValue.slice(2, 4) + ':';
    const startTimeResult = hour + minute + '00';
    const start = date + ' ' + startTimeResult;
    return start;
  };

  const [endTimeIndex, setEndTimeIndex] = useState<number>(0);
  const endSelectOptions: Option[] = useMemo(() => createTimeOptions().slice(startTimeIndex), [startTimeIndex]);

  const [endTime, setEndTime] = useState<Option>(endSelectOptions[0]);
  const endTimeValue = endTime.value;

  const newEndTime = () => {
    if (endTimeValue.length === 3) {
      const endTimeNewValue = '0' + endTimeValue;
      const hour = endTimeNewValue.slice(0, 2) + ':';
      const minute = endTimeNewValue.slice(2, 4) + ':';
      const endTimeResult = hour + minute + '00';
      const end = date + ' ' + endTimeResult;
      return end;
    }
    const hour = endTimeValue.slice(0, 2) + ':';
    const minute = endTimeValue.slice(2, 4) + ':';
    const endTimeResult = hour + minute + '00';
    const end = date + ' ' + endTimeResult;
    return end;
  };

  const [checked, setChecked] = useState(false);

  const switchHandler = (e:any) => {
    setChecked(e.target.checked)
  }

  const { myCalendar } = useAppSelector((state) => state.mycalendar);

  const onTitleChange = (e: any) => {
    setTitle(e.currentTarget.value);
  };

  const onDateChange = (e: any) => {
    setDate(e.currentTarget.value);
    console.log(date);
  };

  const onContentChange = (e: any) => {
    setContent(e.currentTarget.value);
  };
  const onAlarmChannel = (e: any, value: any) => {
    const alarmChannelValue = value.meetupId || undefined;
    setAlarmChannelId(alarmChannelValue);
  };

  const parsedData: any = {
    title: title,
    content: null,
    start: newStartTime(),
    end: newEndTime(),
    open: checked
  };

  const parsedMeetingData: any = {
    title: title,
    content: content,
    start: newStartTime(),
    end: newEndTime(),
    meetupId: alarmChannelId,
    open: checked
  };

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
    window.location.reload()
  }, []);

  const handleSubmitToMe = async () => {
    const action = await dispatch(addSchedule(parsedData));
    if (isFulfilled(action)) {
      handleToggleModal();
    }
  };

  const handleSubmitToYou = async () => {
    const action = await dispatch(addMeeting(parsedMeetingData));
    if (isFulfilled(action)) {
      handleToggleModal();
    }
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
    options: channels.alarmChannels,
    getOptionLabel: (option: tAlarm) => option.displayName,
  };
  const flatProps = {
    options: channels && channels.alarmChannels.map((option: any) => option.displayname),
  };
  const [value, setValue] = React.useState<tAlarm['meetupId'] | null>(null);

  const params = useParams();
  const userId = params.userId;

  useEffect(() => {
    dispatch(fetchAlarmChannelList(userId));
  }, []);


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
          xmlns="http://www.w3.org/2000/svg"
          fill="none"
          viewBox="0 0 24 24"
          strokeWidth="2.5"
          className="w-6 h-6 stroke-title mt-[15px] ml-[550px] cursor-pointer"
        >
          <path strokeLinecap="round" strokeLinejoin="round" d="M6 18L18 6M6 6l12 12" />
        </svg>
        <div>
          <div className={`${myCalendar ? 'mt-[30px]' : 'mt-[15px]'}`}>
            {myCalendar ? (
              <div className="text-s text-title font-bold">
                제목<span className="text-cancel">&#42;</span>
              </div>
            ) : (
              <div className="text-s text-title font-bold">
                미팅명<span className="text-cancel">&#42;</span>
              </div>
            )}
            <input
              type="text"
              name="title"
              value={title}
              onChange={onTitleChange}
              className={`${
                myCalendar ? 'mb-[40px]' : 'mb-[0px]'
              } w-[450px] h-[30px] outline-none border-solid border-b-2 border-title focus:border-b-point active:border-b-point`}
            />
          </div>
          <div className="mt-[15px]">
            <div className="text-s text-title font-bold">
              날짜<span className="text-cancel">&#42;</span>
            </div>
            <input
              type="date"
              value={date}
              onChange={onDateChange}
              className={`${
                myCalendar ? 'mb-[40px]' : 'mb-[0px]'
              } w-[450px] h-[30px] outline-none border-solid border-b-2 border-title focus:border-b-point active:border-b-point`}
            />
            <div className="mt-[15px]">
              <div className="text-s text-title font-bold">
                시간<span className="text-cancel">&#42;</span>
              </div>
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
            <div className="mt-[15px]">
              {myCalendar ? null : (
                <div>
                  <div className="text-s text-title font-bold">내용</div>
                  <input
                    type="text"
                    name="title"
                    value={content}
                    onChange={onContentChange}
                    className="w-[450px] h-[30px] outline-none border-solid border-b-2 border-title focus:border-b-point active:border-b-point"
                  />
                </div>
              )}
            </div>
            <div className="mt-[15px]">
              {myCalendar ? null : (
                <div>
                  <div className="text-s text-title font-bold">알림 보낼 채널</div>
                  <Autocomplete
                    onChange={onAlarmChannel}
                    className="w-[450px]"
                    ListboxProps={{ style: { maxHeight: '150px' } }}
                    {...defaultProps}
                    id="select-channel"
                    renderInput={(params) => <TextField {...params} label="채널 선택하기" variant="standard" />}
                  />
                </div>
              )}
            </div>
            <div className="mt-[40px] mb-[30px]">
              <div className="text-s text-title font-bold">스케쥴 공개 설정</div>
              <Switch checked={checked} onChange={switchHandler} />
            </div>
          </div>
          {myCalendar ? (
            <button
              onClick={handleSubmitToMe}
              className="font-bold bg-title hover:bg-hover text-background rounded w-[450px] h-s drop-shadow-button"
            >
              밋업 불가시간 설정하기
            </button>
          ) : (
            <button
              onClick={handleSubmitToYou}
              className="font-bold bg-title hover:bg-hover text-background rounded w-[450px] mt-[15px] h-s drop-shadow-button"
            >
              밋업 등록하기
            </button>
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
