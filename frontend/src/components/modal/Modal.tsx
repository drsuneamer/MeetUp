import React, { useCallback, useEffect, useMemo, useState } from 'react';
import { useAppDispatch, useAppSelector } from '../../stores/ConfigHooks';
import { setEventModalOpen } from '../../stores/modules/modal';
import { getStringDateFormat } from '../../utils/GetStringDateFormat';
import { createTimeOptions, Option } from '../../utils/CreateTimeOptions';
import SingleSelect from '../common/SingleSelect';
import TextField from '@mui/material/TextField';
import Autocomplete from '@mui/material/Autocomplete';
import { useParams } from 'react-router-dom';
import { isFulfilled, isRejected } from '@reduxjs/toolkit';
import { addSchedule, fetchSchedule, scheduleSelector } from '../../stores/modules/schedules';
import { addMeeting } from '../../stores/modules/schedules';
import { alarmChannelSelector, fetchAlarmChannelList } from '../../stores/modules/channelAlarm';
import { tAlarm } from '../../types/channels';
import { fetchGroupList, groupSelector } from '../../stores/modules/groups';
import Swal from 'sweetalert2';

import Switch from '@mui/material/Switch';
import { getThisWeek } from '../../utils/GetThisWeek';
import { getNow } from '../../utils/GetNow';
import { getSundayOfWeek } from '../../utils/GetSundayOfWeek';

interface Group {
  id: number;
  leader: boolean;
  name: string;
}

const EventModal = () => {
  const dispatch = useAppDispatch();
  const channels = useAppSelector(alarmChannelSelector);
  const groups = useAppSelector(groupSelector);
  const { eventModalIsOpen } = useAppSelector((state) => state.modal);
  const { eventModalData } = useAppSelector((state) => state.events);
  const { myCalendar } = useAppSelector((state) => state.mycalendar);
  const { currentDate } = useAppSelector((state) => state.dates);

  const [title, setTitle] = useState<string>('');
  const [date, setDate] = useState<string>(getStringDateFormat(new Date()));
  const [content, setContent] = useState<string>('');
  const [alarmChannelId, setAlarmChannelId] = useState<number>(0);
  const [alarmChannel, setAlarmChannel] = useState<tAlarm>({ meetupId: 0, displayName: '' });
  const [alarmChannels, setAlarmChannels] = useState([]);
  const [checked, setChecked] = useState(true);
  const [groupId, setGroupId] = useState<number>(0);
  const [newGroupValue, setNewGroupValue] = useState<Group>({ id: 0, leader: false, name: '' });
  const [partyId, setPartyId] = useState<number | null>(0);

  const startSelectOptions: Option[] = useMemo(() => createTimeOptions(), []);
  const [startTimeIndex, setStartTimeIndex] = useState<number>(0);
  const [startTime, setStartTime] = useState<Option>(startSelectOptions[0]);

  const weekly = useMemo(() => {
    return getThisWeek(currentDate);
  }, [currentDate]);

  const endSelectOptions: Option[] = useMemo(() => createTimeOptions().slice(startTimeIndex + 1), [startTimeIndex + 1]);
  const [endTimeIndex, setEndTimeIndex] = useState<number>(0);
  const [endTime, setEndTime] = useState<Option>(endSelectOptions[0]);

  const newStartTime = () => {
    const startTimeValue = startTime.value;
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

  const newEndTime = () => {
    const endTimeValue = endTime.value;
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

  const onTitleChange = (e: any) => {
    setTitle(e.currentTarget.value);
  };

  const onDateChange = (e: any) => {
    setDate(e.currentTarget.value);
  };

  const onContentChange = (e: any) => {
    setContent(e.currentTarget.value);
  };

  // 참여하고 있는 채널
  const onAlarmChannel = (e: any, value: any) => {
    if (value !== null) {
      const alarmChannelValue = value.meetupId || '';
      setAlarmChannelId(alarmChannelValue);
      setAlarmChannel(value);
    }
  };

  // 공개, 비공개 여부
  const switchHandler = (e: any) => {
    setChecked(e.target.checked);
  };

  // 그룹
  const onGroupChange = (e: any, value: any) => {
    if (value !== null) {
      const partyValue = value.id || undefined;
      setGroupId(partyValue);
      setNewGroupValue(value);
    }
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

    if (startTimeIndex < 47 && startTimeIndex > endTimeIndex) {
      setEndTimeIndex(startTimeIndex);
      setEndTime(startSelectOptions[startTimeIndex + 1]);
    } else {
      setEndTime({ value: '2330', label: '신청 불가능' });
    }
  }, [startTimeIndex]);

  const handleToggleModal = useCallback(() => {
    dispatch(setEventModalOpen());
    handleResetInput();
    setAlarmChannelId(0);
    setAlarmChannel({ meetupId: 0, displayName: '' });
    setGroupId(0);
    setNewGroupValue({ id: 0, leader: false, name: '' });
    handleResetInput();
  }, []);

  // 스케줄 등록할 때 보내는 data
  const parsedData: any = {
    title: title,
    content: null,
    start: newStartTime(),
    end: newEndTime(),
    open: checked,
  };

  // 그룹이 없으면 groupId null로
  useEffect(() => {
    setPartyId(groupId || null);
  }, [groupId]);

  // 미팅 등록할 때 보내는 data
  const parsedMeetingData: any = {
    title: title,
    content: content,
    start: newStartTime(),
    end: newEndTime(),
    meetupId: alarmChannelId,
    open: checked,
    partyId: partyId,
  };

  // 나의 스케줄 등록
  const handleSubmitToMe = async () => {
    if (!parsedData.title) {
      Swal.fire({ text: '제목은 필수 입력사항입니다.', icon: 'error', confirmButtonColor: '#0552AC' });
    } else if (parsedData.start === parsedData.end || parsedData.start > parsedData.end) {
      Swal.fire({ text: '이 시간에는 등록할 수 없습니다.', icon: 'error', confirmButtonColor: '#0552AC' });
    } else if (parsedData) {
      const action = await dispatch(addSchedule(parsedData));
      if (isFulfilled(action)) {
        dispatch(fetchSchedule([userId, sunday]));
        handleToggleModal();
      } else if (isRejected(action)) {
        // console.log(action);
      }
    }
  };

  // 미팅 등록
  const handleSubmitToYou = async () => {
    if (!parsedMeetingData.title) {
      Swal.fire({ text: '미팅명은 필수 입력사항입니다.', icon: 'error', confirmButtonColor: '#0552AC' });
    } else if (!parsedMeetingData.meetupId) {
      Swal.fire({ text: '참여중인 밋업은 필수 입력사항입니다.', icon: 'error', confirmButtonColor: '#0552AC' });
    } else if (parsedMeetingData.start === parsedMeetingData.end || parsedMeetingData.start > parsedMeetingData.end) {
      Swal.fire({ text: '이 시간에는 등록할 수 없습니다.', icon: 'error', confirmButtonColor: '#0552AC' });
    } else if (parsedMeetingData) {
      const action = await dispatch(addMeeting(parsedMeetingData));
      if (isFulfilled(action)) {
        dispatch(fetchSchedule([userId, sunday]));
        handleToggleModal();
        handleResetInput();
        setAlarmChannelId(0);
        setAlarmChannel({ meetupId: 0, displayName: '' });
        setGroupId(0);
        setNewGroupValue({ id: 0, leader: false, name: '' });
        handleResetInput();
      }
    }
  };

  const handleResetInput = useCallback(() => {
    setTitle('');
    setDate(getStringDateFormat(new Date()));
    setStartTime(startSelectOptions[0]);
    setStartTimeIndex(0);
    setEndTime(endSelectOptions[0]);
    setEndTimeIndex(0);
    setContent('');
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

  // 참여중인 밋업 띄우기 - Autocomplete 이용
  const defaultProps = {
    options: alarmChannels,
    getOptionLabel: (option: tAlarm) => option.displayName,
    isOptionEqualToValue: (option: tAlarm, value: tAlarm) => option === value,
  };

  const flatProps = {
    options: channels.alarmChannels && channels.alarmChannels.map((option: any) => option.displayname),
  };
  // const [value, setValue] = React.useState<tAlarm['meetupId'] | null>(null);

  // 그룹 선택하기 - Autocomplete 이용
  const defaultGroupProps = {
    options: groups.groups,
    getOptionLabel: (option: any) => option.name,
    isOptionEqualToValue: (option: any, value: any) => option.id === value.id,
  };

  const flatGroupProps = {
    options: groups && groups.groups.map((option: any) => option.name),
  };
  const [gruoupValue, setGroupValue] = React.useState<Group['id'] | null>(null);

  const params = useParams();
  const userId = params.userId;

  useEffect(() => {
    if (eventModalIsOpen) {
      dispatch(fetchAlarmChannelList(userId)).then((action) => setAlarmChannels(action.payload));
      dispatch(fetchGroupList()).then((action) => setGroupValue(action.payload[0]));
    }
  }, [eventModalIsOpen]);

  // 그 주의 일요일 구하기
  const sunday = useMemo(() => {
    return getSundayOfWeek(currentDate, weekly);
  }, [currentDate]);

  // 날짜 & 시간 비교하기
  const nows = useMemo(() => {
    return getNow();
  }, []);

  const isPast = () => {
    const today = new Date();
    const selectedDate = new Date(date);

    if (nows) {
      const now = nows.hours.toString() + nows.minutes.toString();

      // 오늘을 포함한 날짜가 선택한 날짜보다 크다면 - 즉 과거
      if (today > selectedDate) {
        // 오늘 내에서 현재시간 이전과 이후
        if (today.toString().slice(0, 10) === selectedDate.toString().slice(0, 10) && Number(startTime.value) > Number(now)) {
          return false;
        }
        return true;
      }
      return false;
    }
  };

  return (
    <div className={`${eventModalIsOpen ? 'fixed' : 'hidden'} w-[100%] h-[100%] flex justify-center items-center z-30`}>
      <div
        className={`${
          !myCalendar && groups.groups.length >= 1 ? 'w-[600px] h-[640px]' : 'w-[600px] h-[600px]'
        } flex flex-col items-center bg-background z-10 rounded drop-shadow-shadow`}
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
          className="w-6 h-6 stroke-title mt-[10px] ml-[550px] cursor-pointer"
        >
          <path strokeLinecap="round" strokeLinejoin="round" d="M6 18L18 6M6 6l12 12" />
        </svg>
        <div>
          <div className={`${myCalendar ? 'mt-[30px]' : 'mt-[10px]'}`}>
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
              maxLength={50}
              onChange={onTitleChange}
              className={`${
                myCalendar ? 'mb-[40px]' : 'mb-[0px]'
              } w-[450px] h-[30px] outline-none border-solid border-b-2 border-title focus:border-b-point active:border-b-point`}
            />
          </div>
          <div className={`${!myCalendar && groups.groups.length >= 1 ? 'mt-[10px]' : 'mt-[15px]'}`}>
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
            <div className={`${!myCalendar && groups.groups.length >= 1 ? 'mt-[10px]' : 'mt-[15px]'}`}>
              <div className="text-s text-title font-bold">
                시간<span className="text-cancel">&#42;</span>
              </div>
              <div className="flex items-center w-[450px] h-[30px] outline-none border-solid border-b-2 border-title focus:border-b-point active:border-b-point">
                <SingleSelect className="text-sm w-[180px]" options={startSelectOptions} onChange={handleStartSelectClick} selected={startTime} />
                <span className="mx-2">-</span>
                <SingleSelect className="text-sm w-[180px]" options={endSelectOptions} onChange={handleEndSelectClick} selected={endTime} />
                <svg
                  xmlns="https://www.w3.org/2000/svg"
                  fill="none"
                  viewBox="0 0 24 24"
                  strokeWidth="2"
                  stroke="currentColor"
                  className="w-6 h-6 ml-[140px]"
                >
                  <path strokeLinecap="round" strokeLinejoin="round" d="M12 6v6h4.5m4.5 0a9 9 0 11-18 0 9 9 0 0118 0z" />
                </svg>
              </div>
            </div>
            <div className="mt-[10px]">
              {myCalendar ? null : (
                <div>
                  <div className="text-s text-title font-bold">내용</div>
                  <input
                    type="text"
                    name="title"
                    value={content}
                    maxLength={110}
                    onChange={onContentChange}
                    className="w-[450px] h-[30px] outline-none border-solid border-b-2 border-title focus:border-b-point active:border-b-point"
                  />
                </div>
              )}
            </div>
            <div className={`${!myCalendar && groups.groups.length >= 1 ? 'mt-[10px]' : 'mt-[15px]'}`}>
              {myCalendar ? null : (
                <div>
                  <div className="text-s text-title font-bold">
                    참여중인 밋업
                    <span className="text-cancel">&#42;</span>
                  </div>
                  <Autocomplete
                    onChange={onAlarmChannel}
                    className="w-[450px]"
                    ListboxProps={{ style: { maxHeight: '150px' } }}
                    value={alarmChannel}
                    // isOptionEqualToValue={(option, value) => option.meetupId === value.meetupId}
                    // inputValue={alarmChannel.displayName}
                    {...defaultProps}
                    id="select-channel"
                    renderInput={(params) => <TextField {...params} label="채널 선택하기" variant="standard" />}
                  />
                </div>
              )}
            </div>
            {myCalendar ? (
              <div className="mt-[40px] mb-[30px]">
                <div className="text-s text-title font-bold">공개 설정</div>
                <Switch checked={checked} onChange={switchHandler} />
                {checked ? (
                  <span className="text-title text-xs">공개: 일정 제목이 다른 사람에게 노출됩니다.</span>
                ) : (
                  <span className="text-xs text-label">비공개: 일정 제목이 비공개로 숨겨집니다.</span>
                )}
              </div>
            ) : (
              <div className="mt-[10px] mb-[10px]">
                <div className="text-s text-title font-bold">공개 설정</div>
                <Switch checked={checked} onChange={switchHandler} />
                {checked ? (
                  <span className="text-title text-xs">공개: 알림받을 채널에 알림이 갑니다.</span>
                ) : (
                  <span className="text-xs text-label">비공개: 캘린더 주인에게 DM으로 알림이 갑니다.</span>
                )}
              </div>
            )}
            <div className="mt-[10px]">
              {!myCalendar && groups.groups.length >= 1 ? (
                <div>
                  <div className="text-s text-title font-bold mt-[1px]">그룹 선택</div>
                  <Autocomplete
                    onChange={onGroupChange}
                    value={newGroupValue}
                    // inputValue={newGroupValue.name}
                    // isOptionEqualToValue={(option, value) => option.id === value.id}
                    className="w-[450px]"
                    ListboxProps={{ style: { maxHeight: '150px' } }}
                    {...defaultGroupProps}
                    id="select-channel"
                    renderInput={(params) => <TextField {...params} label="그룹 선택하기" variant="standard" />}
                  />
                </div>
              ) : null}
            </div>
          </div>
          <div className={`${!myCalendar && groups.groups.length >= 1 ? 'mt-[20px]' : 'mt-[30px]'}`}>
            {myCalendar && isPast() ? (
              <button disabled className="font-bold bg-disabled text-background rounded w-[450px] h-s drop-shadow-button">
                현재 시간 이전에는 등록할 수 없습니다
              </button>
            ) : myCalendar && !isPast() ? (
              <div className="flex flex-col justify-center items-center">
                <button
                  onClick={handleSubmitToMe}
                  className="font-bold bg-title hover:bg-hover text-background rounded w-[450px] h-s drop-shadow-button"
                >
                  내 스케줄 등록하기
                </button>
                <span className="text-xs text-label mt-[5px]">내 스케줄을 등록한 시간에는 다른 사람들이 미팅을 신청할 수 없습니다</span>
              </div>
            ) : !myCalendar && isPast() ? (
              <button className="font-bold bg-disabled text-background rounded w-[450px] mb-[10px] h-s drop-shadow-button">
                현재 시간 이전에는 등록할 수 없습니다
              </button>
            ) : (
              <button
                onClick={handleSubmitToYou}
                className="font-bold bg-title hover:bg-hover text-background rounded w-[450px] mb-[10px] h-s drop-shadow-button"
              >
                미팅 등록하기
              </button>
            )}
          </div>
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
