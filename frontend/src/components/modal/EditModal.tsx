import React, { useCallback, useEffect, useMemo, useState } from 'react';
import { useAppDispatch, useAppSelector } from '../../stores/ConfigHooks';
import { ModalSelector, setEditModalOpen } from '../../stores/modules/modal';
import { getStringDateFormat } from '../../utils/GetStringDateFormat';
import { createTimeOptions, Option } from '../../utils/CreateTimeOptions';
import SingleSelect from '../common/SingleSelect';
import TextField from '@mui/material/TextField';
import Autocomplete from '@mui/material/Autocomplete';
import { isFulfilled, isRejected } from '@reduxjs/toolkit';
import { editMeetingDetail, editScheduleDetail } from '../../stores/modules/schedules';
import { alarmChannelSelector, fetchAlarmChannelList } from '../../stores/modules/channelAlarm';
import { tAlarm } from '../../types/channels';
import { detailSelector, fetchScheduleDetail } from '../../stores/modules/schedules';
import Swal from 'sweetalert2';

import Switch from '@mui/material/Switch';
import { getThisWeek } from '../../utils/GetThisWeek';
import { useParams } from 'react-router-dom';
import { fetchSchedule } from '../../stores/modules/schedules';
import { getSundayOfWeek } from '../../utils/GetSundayOfWeek';


const EditModal = () => {
  // userId
  const params = useParams();
  const userId = params.userId;
  const dispatch = useAppDispatch();

  const channels = useAppSelector(alarmChannelSelector);
  const { editModalIsOpen } = useAppSelector((state) => state.modal);
  const { editModalType } = useAppSelector((state) => state.modal);
  const scheduleDetail = useAppSelector(detailSelector).scheduleModal.scheduleDetail;
  const modalSelector = useAppSelector(ModalSelector);
  
  const [title, setTitle] = useState<string>('');
  const [date, setDate] = useState<string>('');
  const [content, setContent] = useState<string>('');
  const [alarmChannelId, setAlarmChannelId] = useState<number>(0);
  const [alarmChannel, setAlarmChannel] = useState<tAlarm>({ meetupId: 0, displayName: '' });
  const [alarmChannels, setAlarmChannels] = useState([]);
  const [meetupId, setMeetupId] = useState<number | null>(null);
  
  const startSelectOptions: Option[] = useMemo(() => createTimeOptions(), []);
  const [startTimeIndex, setStartTimeIndex] = useState<number>(0);
  const { currentDate } = useAppSelector((state) => state.dates);

  const weekly = useMemo(() => {
    return getThisWeek(currentDate);
  }, [currentDate]);

  const sunday = useMemo(() => {
    return getSundayOfWeek(currentDate, weekly);
  }, [currentDate]);

  useEffect(() => {
    if (editModalIsOpen) {
      const loadData = async () => {
        if (modalSelector.scheduleId !== 0) {
          const action = await dispatch(fetchScheduleDetail(modalSelector.scheduleId));
          if (isFulfilled(action)) {
            // console.log(action.payload);
            return action.payload;
          }
        }
      };
      loadData().then((detail: any) => {
        if (detail) {
          setTitle(detail.title);
          setContent(detail.content);
          setDate(detail.start.slice(0, 10));
          setStartTime(changeToStartTime());
          setEndTime(changeToEndTime());
          setMeetupId(detail.meetupId);
          setChecked(detail.open);

          const loadAlarmData = async () => {
            const action = await dispatch(fetchAlarmChannelList(detail.managerId));
            if (isFulfilled(action)) {
              // console.log(action.payload);
              return action.payload;
            }
            return null;
          };
          if (modalSelector.editModalType === 'meeting') {
            loadAlarmData().then((channelList: any) => {
              if (channelList) {
                // console.log('channel list', channelList);
                setAlarmChannels(channelList);
                // console.log(channelList.find((ch: any) => ch.meetupId === detail.meetupId));
                setAlarmChannel(channelList.find((ch: any) => ch.meetupId === detail.meetupId));
              }
            });
          }
          return null;
        }
      });
    }
  }, [modalSelector]);

  // 날짜, 시간
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
    if (startTimeIndex < 47 && startTimeIndex > endTimeIndex) {
      setEndTimeIndex(startTimeIndex);
      setEndTime(startSelectOptions[startTimeIndex + 1]);
    } else if (startTimeIndex === 47) {
      setEndTime({ value: '2330', label: '신청 불가능' });
    }
  }, [startTimeIndex]);

  useEffect(() => {
    newEndTime();
  }, [date, end]);

  // 공개, 비공개 여부(토글)
  const [checked, setChecked] = useState<boolean>(scheduleDetail.open);

  const switchHandler = (e: any) => {
    setChecked(e.target.checked);
  };

  // onChange
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
    if (value !== null) {
      const alarmChannelValue = value.meetupId || '';
      setMeetupId(alarmChannelValue);
      setAlarmChannel(value);
    }
  };

  const scheduleDetailId = useAppSelector(detailSelector).scheduleModal.scheduleDetail.id;

  useEffect(() => {
    setAlarmChannelId(scheduleDetail.meetupId);
  }, [scheduleDetail]);

  const handleToggleModal = useCallback(() => {
    dispatch(setEditModalOpen([scheduleDetail.id, 'close']));
  }, []);

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

  const flatProps = {
    options: channels.alarmChannels && channels.alarmChannels.map((option: any) => option.displayname),
  };

  const [value, setValue] = React.useState<tAlarm['meetupId'] | null>(null);

  const handleEditMeeting = async () => {
    const parsedMeetingData: any = {
      id: scheduleDetailId,
      title: title,
      content: content,
      start: newStartTime(),
      end: newEndTime(),
      meetupId: alarmChannel.meetupId,
      open: checked,
    };

    if (!parsedMeetingData.title) {
      Swal.fire({ text: '제목은 필수 입력사항입니다.', icon: 'error', confirmButtonColor: '#0552AC' });
    } else if (!parsedMeetingData.meetupId) {
      Swal.fire({ text: '참여중인 밋업은 필수 입력사항입니다.', icon: 'error', confirmButtonColor: '#0552AC' });
    } else if (parsedMeetingData.start === parsedMeetingData.end || parsedMeetingData.start > parsedMeetingData.end) {
      Swal.fire({ text: '이 시간에는 등록할 수 없습니다.', icon: 'error', confirmButtonColor: '#0552AC' });
    } else if (parsedMeetingData) {
      const action = await dispatch(editMeetingDetail(parsedMeetingData));
      if (isFulfilled(action)) {
        dispatch(fetchSchedule([userId, sunday]));
        handleToggleModal();
        handleResetInput();
        setAlarmChannelId(0);
      }
    }
  };

  const handleEditSchedule = async () => {
    const parsedData: any = {
      id: scheduleDetailId,
      title: title,
      content: null,
      start: newStartTime(),
      end: newEndTime(),
      open: checked,
    };

    if (!parsedData.title) {
      Swal.fire({ text: '제목은 필수 입력사항입니다.', icon: 'error', confirmButtonColor: '#0552AC' });
    } else if (parsedData.start === parsedData.end || parsedData.start > parsedData.end) {
      Swal.fire({ text: '이 시간에는 등록할 수 없습니다.', icon: 'error', confirmButtonColor: '#0552AC' });
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
      <div className={`${editModalIsOpen ? 'fixed' : 'hidden'} w-[100%] h-[100%] flex justify-center items-center z-30`}>
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
                // defaultValue={scheduleDetail.title || undefined}
                value={title || ''}
                maxLength={50}
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
                value={date || ''}
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
              <div className="mt-[15px]">
                {modalSelector.editModalType === 'schedule' ? null : (
                  <div>
                    <div className="text-s text-title font-bold">내용</div>
                    <input
                      type="text"
                      name="title"
                      // defaultValue={scheduleDetail.content || undefined}
                      value={content || ''}
                      maxLength={110}
                      onChange={onContentChange}
                      className="w-[450px] h-[30px] outline-none border-solid border-b-2 border-title focus:border-b-point active:border-b-point"
                    />
                  </div>
                )}
              </div>
              <div className="mt-[15px]">
                {modalSelector.editModalType && modalSelector.editModalType === 'schedule' ? null : (
                  <div>
                    <div className="text-s text-title font-bold">참여중인 밋업</div>
                    <Autocomplete
                      onChange={onAlarmChannel}
                      className="w-[450px]"
                      disabled
                      ListboxProps={{ style: { maxHeight: '150px' } }}
                      isOptionEqualToValue={(option: tAlarm, value: tAlarm) => option.meetupId === value.meetupId}
                      options={alarmChannels}
                      getOptionLabel={(option: tAlarm) => option.displayName}
                      id="select-channel"
                      renderInput={(params) => <TextField {...params} label={alarmChannel.displayName} variant="standard" />}
                    />
                  </div>
                )}
              </div>
              {modalSelector.editModalType === 'schedule' ? (
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
            {modalSelector.editModalType === 'schedule' ? (
              <div className="flex flex-col justify-center items-center">
                <button
                  onClick={handleEditSchedule}
                  className="font-bold bg-title hover:bg-hover text-background rounded w-[450px] h-s drop-shadow-button"
                >
                  내 스케줄 등록하기
                </button>
                <span className="text-xs text-label mt-[5px]">내 스케줄을 등록한 시간에는 다른 사람들이 미팅을 신청할 수 없습니다</span>
              </div>
            ) : (
              <button
                onClick={handleEditMeeting}
                className="font-bold bg-title hover:bg-hover text-background rounded w-[450px] mb-[10px] h-s drop-shadow-button"
              >
                미팅 등록하기
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
