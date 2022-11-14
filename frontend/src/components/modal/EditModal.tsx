import React, { useCallback, useEffect, useMemo, useState } from 'react';
import { useAppDispatch, useAppSelector } from '../../stores/ConfigHooks';
import { useSelector } from 'react-redux';
import { ModalSelector, setEditModalOpen } from '../../stores/modules/modal';
import { getStringDateFormat } from '../../utils/GetStringDateFormat';
import { createTimeOptions, Option } from '../../utils/CreateTimeOptions';
import SingleSelect from '../common/SingleSelect';
import TextField from '@mui/material/TextField';
import Autocomplete from '@mui/material/Autocomplete';
import { isFulfilled, isRejected } from '@reduxjs/toolkit';
import { editMeetingDetail, editScheduleDetail } from '../../stores/modules/schedules';
import { alarmChannelSelector } from '../../stores/modules/channelAlarm';
import { tAlarm } from '../../types/channels';
import { detailSelector } from '../../stores/modules/schedules';
import Switch from '@mui/material/Switch';
import { getThisWeek } from '../../utils/GetThisWeek';
import { useParams } from 'react-router-dom';
import { fetchSchedule } from '../../stores/modules/schedules';
import { fetchGroupList, groupSelector } from '../../stores/modules/groups';

interface Group {
  id: number;
  leader: boolean;
  name: string;
}

const EditModal = () => {
  // 그 주의 일요일 구하기
  const { currentDate } = useAppSelector((state) => state.dates);

  const weekly = useMemo(() => {
    return getThisWeek(currentDate);
  }, [currentDate]);

  const sunday = useMemo(() => {
    const date = new Date(currentDate);
    const firstDayOfTheMonth = date.getDay();

    if (date.getDate() <= firstDayOfTheMonth) {
      if (weekly[0].date < 10) {
        if (date.getMonth() + 1 < 10) {
          return `${date.getFullYear()}-0${date.getMonth()}-0${weekly[0].date}`;
        }
        return `${date.getFullYear()}-${date.getMonth()}-0${weekly[0].date}`;
      } else {
        if (date.getMonth() + 1 < 10) {
          return `${date.getFullYear()}-0${date.getMonth()}-${weekly[0].date}`;
        }
        return `${date.getFullYear()}-${date.getMonth()}-${weekly[0].date}`;
      }
    } else {
      if (weekly[0].date < 10) {
        if (date.getMonth() + 1 < 10) {
          return `${date.getFullYear()}-0${date.getMonth() + 1}-0${weekly[0].date}`;
        }
        return `${date.getFullYear()}-${date.getMonth() + 1}-0${weekly[0].date}`;
      } else {
        if (date.getMonth() + 1 < 10) {
          return `${date.getFullYear()}-0${date.getMonth() + 1}-${weekly[0].date}`;
        }
        return `${date.getFullYear()}-${date.getMonth() + 1}-${weekly[0].date}`;
      }
    }
  }, [currentDate]);

  // userId
  const params = useParams();
  const userId = params.userId;

  const channels = useSelector(alarmChannelSelector);
  const groups = useAppSelector(groupSelector);
  const { eventModalIsOpen } = useAppSelector((state) => state.modal);
  const scheduleDetail = useSelector(detailSelector).scheduleModal.scheduleDetail;
  const detailModalSelector = useSelector(ModalSelector);
  const { editModalIsOpen } = useAppSelector((state) => state.modal);
  const { editModalType } = useAppSelector((state) => state.modal);
  const dispatch = useAppDispatch();
  const [title, setTitle] = useState<string>('');
  const [date, setDate] = useState<string>('');
  const [content, setContent] = useState<string>('');
  const [alarmChannelId, setAlarmChannelId] = useState<number>(0);
  const [groupId, setGroupId] = useState<number | null>(0);
  const [partyId, setPartyId] = useState<number | null>(0);
  const startSelectOptions: Option[] = useMemo(() => createTimeOptions(), []);
  const [startTimeIndex, setStartTimeIndex] = useState<number>(0);

  const newDate = () => {
    const dateTime = scheduleDetail.start.slice(0, 10);
    return dateTime;
  };
  const changeToStartTime = () => {
    const newStartTime = { value: '', label: '' };
    const newTime = scheduleDetail.start.slice(11, 16);
    const numberTime = Number(newTime.slice(0, 2));

    const newValue = newTime.replace(':', '');
    if (newValue[0] === '0') {
      const valueTime = newValue.slice(1, 4);
      newStartTime.value = valueTime;
    } else {
      const valueTime = newValue;
      newStartTime.value = valueTime;
    }

    if (numberTime < 12) {
      const labelTime = '오전' + ' ' + newTime.slice(0, 2) + '시' + ' ' + newTime.slice(3, 5) + '분';
      newStartTime.label = labelTime;
    } else if (numberTime === 12) {
      const labelTime = '오후' + ' ' + newTime.slice(0, 2) + '시' + ' ' + newTime.slice(3, 5) + '분';
      newStartTime.label = labelTime;
    } else if (12 < numberTime) {
      const hour = (numberTime - 12).toString();
      const labelTime = '오후' + ' ' + hour + '시' + ' ' + newTime.slice(3, 5) + '분';
      newStartTime.label = labelTime;
    }
    return newStartTime;
  };

  const changeToEndTime = () => {
    const newEndTime = { value: '', label: '' };

    const newTime = scheduleDetail.end.slice(11, 16);
    const numberTime = Number(newTime.slice(0, 2));

    const newValue = newTime.replace(':', '');
    if (newValue[0] === '0') {
      const valueTime = newValue.slice(1, 4);
      newEndTime.value = valueTime;
    } else {
      const valueTime = newValue;
      newEndTime.value = valueTime;
    }

    if (numberTime < 12) {
      const labelTime = '오전' + ' ' + newTime.slice(0, 2) + '시' + ' ' + newTime.slice(3, 5) + '분';
      newEndTime.label = labelTime;
    } else if (numberTime === 12) {
      const labelTime = '오후' + ' ' + newTime.slice(0, 2) + '시' + ' ' + newTime.slice(3, 5) + '분';
      newEndTime.label = labelTime;
    } else if (12 < numberTime) {
      const hour = (numberTime - 12).toString();
      const labelTime = '오후' + ' ' + hour + '시' + ' ' + newTime.slice(3, 5) + '분';
      newEndTime.label = labelTime;
    }
    return newEndTime;
  };
  const start = changeToStartTime();
  const [startTime, setStartTime] = useState<Option>({ value: '', label: '' });
  const end = changeToEndTime();
  const [endTime, setEndTime] = useState<Option>({ value: '', label: '' });

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

  useEffect(() => {
    newStartTime();
  }, [date, start]);

  useEffect(() => {
    newEndTime();
  }, [date, end]);

  const [checked, setChecked] = useState(false);

  const switchHandler = (e: any) => {
    setChecked(e.target.checked);
  };

  const { myCalendar } = useAppSelector((state) => state.mycalendar);

  const onTitleChange = (e: any) => {
    setTitle(e.currentTarget.value);
  };

  const onDateChange = (e: any) => {
    setDate(e.currentTarget.value);
  };

  const onContentChange = (e: any) => {
    if (e.currentTarget.value === null) {
      setContent(scheduleDetail.content);
    } else {
      setContent(e.currentTarget.value);
    }
  };

  const onAlarmChannel = (e: any, value: any) => {
    const alarmChannelValue = value.meetupId || undefined;
    setAlarmChannelId(alarmChannelValue);
  };

  const onGroupChange = (e: any, value: any) => {
    const partyValue = value.id || undefined;
    setGroupId(partyValue);
  };

  const scheduleDetailId = useSelector(detailSelector).scheduleModal.scheduleDetail.id;

  useEffect(() => {
    setTitle(scheduleDetail.title);
    setContent(scheduleDetail.content);
    setDate(newDate());
    setStartTime(start);
    setEndTime(end);
  }, [scheduleDetail]);

  useEffect(() => {
    setAlarmChannelId(scheduleDetail.meetupId);
  }, [scheduleDetail]);

  useEffect(() => {
    setGroupId(scheduleDetail.partyId);
  }, [scheduleDetail]);

  const handleToggleModal = useCallback(() => {
    dispatch(setEditModalOpen('close'));
  }, []);

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

  const parsedData: any = {
    id: scheduleDetailId,
    title: title,
    content: null,
    start: newStartTime(),
    end: newEndTime(),
    open: checked,
  };

  const parsedMeetingData: any = {
    id: scheduleDetailId,
    title: title,
    content: content,
    start: newStartTime(),
    end: newEndTime(),
    meetupId: alarmChannelId,
    open: checked,
  };

  const handleEditMeeting = async () => {
    if (!parsedMeetingData.title) {
      alert('제목은 필수 입력사항입니다');
    } else if (!parsedMeetingData.meetupId) {
      alert('참여중인 밋업은 필수 입력사항입니다');
    } else if (parsedMeetingData) {
      const action = await dispatch(editMeetingDetail(parsedMeetingData));
      if (isFulfilled(action)) {
        dispatch(fetchSchedule([userId, sunday]));
        handleToggleModal();
      }
    }
  };

  // const handleEditMeeting = () => {
  //   console.log('====수정====');
  //   // setPartyId(groupId || null);
  //   console.log(parsedMeetingData);
  // };
  const handleEditSchedule = async () => {
    if (!parsedData.title) {
      alert('제목은 필수 입력사항입니다');
    } else if (parsedData) {
      const action = await dispatch(editScheduleDetail(parsedData));
      if (isFulfilled(action)) {
        handleToggleModal();
      } else if (isRejected(action)) {
        console.log(action);
      }
    }
  };

  if (scheduleDetail) {
    return (
      <div className={`${editModalIsOpen ? 'fixed' : 'hidden'} w-[100%] h-[100%] flex justify-center items-center`}>
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
            <div className={`${editModalType === 'schedule' ? 'mt-[30px]' : 'mt-[15px]'}`}>
              {editModalType === 'schedule' ? (
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
                defaultValue={scheduleDetail.title}
                // value={title}
                onChange={onTitleChange}
                className={`${
                  editModalType === 'schedule' ? 'mb-[40px]' : 'mb-[0px]'
                } w-[450px] h-[30px] outline-none border-solid border-b-2 border-title focus:border-b-point active:border-b-point`}
              />
            </div>
            <div className="mt-[20px]">
              <div className="text-s text-title font-bold">
                날짜<span className="text-cancel">&#42;</span>
              </div>
              <input
                type="date"
                defaultValue={scheduleDetail.start.slice(0, 10)}
                onChange={onDateChange}
                className={`${
                  editModalType === 'schedule' ? 'mb-[40px]' : 'mb-[0px]'
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
                {editModalType === 'schedule' ? null : (
                  <div>
                    <div className="text-s text-title font-bold">내용</div>
                    <input
                      type="text"
                      name="title"
                      defaultValue={scheduleDetail.content}
                      onChange={onContentChange}
                      className="w-[450px] h-[30px] outline-none border-solid border-b-2 border-title focus:border-b-point active:border-b-point"
                    />
                  </div>
                )}
              </div>
              <div className="mt-[15px]">
                {editModalType === 'schedule' ? null : (
                  <div>
                    <div className="text-s text-title font-bold">참여중인 밋업</div>
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
              {editModalType === 'schedule' ? (
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
                <div className="mt-[20px] mb-[20px]">
                  <div className="text-s text-title font-bold">공개 설정</div>
                  <Switch checked={checked} onChange={switchHandler} />
                  {checked ? (
                    <span className="text-title text-xs">공개: 알림받을 채널에 알림이 갑니다.</span>
                  ) : (
                    <span className="text-xs text-label">비공개: 캘린더 주인에게 DM으로 알림이 갑니다.</span>
                  )}
                </div>
              )}
            </div>
            {editModalType === 'schedule' ? (
              <button
                onClick={handleEditSchedule}
                className="font-bold bg-title hover:bg-hover text-background rounded w-[450px] h-s drop-shadow-button"
              >
                일정 등록하기
              </button>
            ) : (
              <button
                onClick={handleEditMeeting}
                className="font-bold bg-title hover:bg-hover text-background rounded w-[450px] mb-[10px] h-s drop-shadow-button"
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
  }
  return <div></div>;
};

export default EditModal;
