import React, { useCallback, useEffect, useMemo, useState } from 'react';
import { useAppDispatch, useAppSelector } from '../../stores/ConfigHooks';
import { useSelector } from 'react-redux';
import { ModalSelector, setDetailModalOpen, setEditModalOpen } from '../../stores/modules/modal';
import { getStringDateFormat } from '../../utils/GetStringDateFormat';
import { createTimeOptions, Option } from '../../utils/CreateTimeOptions';
import { addEvent } from '../../stores/modules/events';
import { NewEvent } from '../../types/events';
import SingleSelect from '../common/SingleSelect';
import TextField from '@mui/material/TextField';
import Autocomplete from '@mui/material/Autocomplete';
import { setMyCalendar } from '../../stores/modules/mycalendar';
import { isValidDateValue } from '@testing-library/user-event/dist/utils';
import { useNavigate, useParams } from 'react-router-dom';
import { isFulfilled } from '@reduxjs/toolkit';
import { addMeeting, editMeetingDetail, fetchScheduleDetail } from '../../stores/modules/schedules';
import { alarmChannelSelector } from '../../stores/modules/channelAlarm';
import { myScheduleSelector, meetingToMeSelector, meetingFromMeSelector } from '../../stores/modules/schedules';
import { tSchedule } from '../../types/events';
import { tAlarm } from '../../types/channels';
import { detailSelector } from '../../stores/modules/schedules';

// interface ChannelOptionType {
//   title: string;
// }

// const channels = [
//   { title: '서울_1반_팀장채널'},
//   { title: 'A101' },
//   { title: 'A102' },
//   { title: 'A103' },
//   { title: 'A104' },
//   { title: 'A105' },
//   { title: 'A106' },
//   { title: 'A107' },
//   { title: 'A102_scrum' },
//   { title: 'A102_jira_bot' },
// ];

const EditModal = () => {
  const channels = useSelector(alarmChannelSelector);
  // const editModalSelector = useSelector(ModalSelector);
  const { editModalIsOpen } = useAppSelector((state) => state.modal);

  const dispatch = useAppDispatch();
  const navigate = useNavigate();
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

  const onTitleChange = (e: any) => {
    setTitle(e.currentTarget.value);
  };

  const onDateChange = (e: any) => {
    setDate(e.currentTarget.value);
  };

  const onContentChange = (e: any) => {
    setContent(e.currentTarget.value);
  };
  const onAlarmChannel = (e: any, value: any) => {
    const alarmChannelValue = value.meetupId || undefined;
    setAlarmChannelId(alarmChannelValue);
  };

  // const parsedData: any = {
  //   title: title,
  //   content: null,
  //   start: newStartTime(),
  //   end: newEndTime(),
  // };

  // const parsedMeetingData: any = {
  //   id: scheduleDetailId,
  //   title: title,
  //   content: content,
  //   start: newStartTime(),
  //   end: newEndTime(),
  //   meetupId: alarmChannelId,
  // };

  useEffect(() => {
    setStartTime(startSelectOptions[startTimeIndex]);

    if (startTimeIndex > endTimeIndex) {
      setEndTimeIndex(startTimeIndex);
      setEndTime(startSelectOptions[startTimeIndex]);
    }
  }, [startTimeIndex]);

  const handleToggleModal = useCallback(() => {
    dispatch(setEditModalOpen());
    window.location.reload();
  }, []);

  // const handleSubmitToMe = async () => {
  //   const action = await dispatch(addSchedule(parsedData));
  //   if (isFulfilled(action)) {
  //     const userId = localStorage.getItem('id');
  //     handleToggleModal();
  //   }
  // };

  const handleSubmitToYou = async () => {
    const action = await dispatch(addMeeting(parsedMeetingData));
    if (isFulfilled(action)) {
      // const userId = localStorage.getItem('id')
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

  const scheduleDetail = useAppSelector(detailSelector).scheduleModal.scheduleDetail;
  const scheduleDetailId = useAppSelector(detailSelector).scheduleModal.scheduleDetail.id;
  // const handleSubmitToMe = () => {
  //   console.log('되야만해..')
  //   console.log(myScheduleId);
  //   console.log(meetingToMeId);
  //   console.log(meetingFromMeId);
  //   console.log('===========');
  //   console.log(scheduleDetail);
  //   console.log(startTime);
  //   console.log('========================')
  //   console.log(scheduleDetailId);
  // }

  // useEffect(() => {
  //   const loadData = async () => {
  //     if (scheduleDetail ) {

  //       const action = await dispatch(fetchScheduleDetail(scheduleDetail.id))
  //       if (isFulfilled(action)) {
  //         console.log(action)
  //         return action.payload;
  //       }
  //     }
  //     return null
  //   };
  //   loadData().then(() => {
  //     setTitle(title)
  //     setContent(content)
  //     setDate(date)
  //     setStartTime(startTime)
  //     setEndTime(endTime)
  //     setAlarmChannelId(alarmChannelId)
  //   })
  // }, []);

  const parsedMeetingData: any = {
    id: scheduleDetailId,
    title: title,
    content: content,
    start: newStartTime(),
    end: newEndTime(),
    meetupId: alarmChannelId,
  };

  const handleEditEvent = async () => {
    const action = await dispatch(editMeetingDetail(parsedMeetingData));
    if (isFulfilled(action)) {
      handleToggleModal();
      // dispatch(setDetailModalOpen());
    }
  };
  // const handleEditEvent() => {
  //   console.log(valueTime)
  // }
  // const temp = {value: '030'}
  const changeToStartTime = () => {
    const startTime = scheduleDetail.start.slice(11, 15);
    const newTime = startTime.replace(':', '');
    if (newTime[0] === '0') {
      const valueTime = startTime.slice(1, 4);
      // startTime.value = valueTime
      return valueTime;
    } else {
      const valueTime = newTime;
      // startTime.value = valueTime
      return valueTime;
    }

    // console.log(scheduleDetail.start)
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
            <div className="mt-[20px]">
              <div className="text-s text-title font-bold">
                미팅명<span className="text-cancel">&#42;</span>
              </div>
              <input
                type="text"
                name="title"
                value={scheduleDetail.title}
                onChange={onTitleChange}
                className="mb-[0px] w-[450px] h-[30px] outline-none border-solid border-b-2 border-title focus:border-b-point active:border-b-point"
              />
            </div>
            <div className="mt-[20px]">
              <div className="text-s text-title font-bold">
                날짜<span className="text-cancel">&#42;</span>
              </div>
              <input
                type="date"
                value={scheduleDetail.start.slice(0, 10)}
                onChange={onDateChange}
                className="mb-[0px] w-[450px] h-[30px] outline-none border-solid border-b-2 border-title focus:border-b-point active:border-b-point"
              />
              <div className="mt-[20px]">
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
              <div className="mt-[20px]">
                <div>
                  <div className="text-s text-title font-bold">내용</div>
                  <input
                    type="text"
                    name="title"
                    value={scheduleDetail.content}
                    onChange={onContentChange}
                    className="w-[450px] h-[30px] outline-none border-solid border-b-2 border-title focus:border-b-point active:border-b-point"
                  />
                </div>
              </div>
              <div className="mt-[20px]">
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
              </div>
            </div>
            <button
              onClick={handleEditEvent}
              className="font-bold bg-title hover:bg-hover text-background mt-[50px] rounded w-[450px] h-s drop-shadow-button"
            >
              밋업 등록하기
            </button>
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
