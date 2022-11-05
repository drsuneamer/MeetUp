import React, { useCallback, useEffect, useMemo, useState } from 'react';
import { useAppDispatch, useAppSelector } from '../../stores/ConfigHooks';
import { ModalSelector } from '../../stores/modules/modal';
import { setDetailModalOpen } from '../../stores/modules/modal';
import { getStringDateFormat } from '../../utils/GetStringDateFormat';
import { createTimeOptions, Option } from '../../utils/CreateTimeOptions';
import { useSelector } from 'react-redux';
import webex from '../../assets/webex_icon.png';
import { Link, useParams, useNavigate } from 'react-router-dom';
import { detailSelector, scheduleSelector } from '../../stores/modules/schedules';

const DetailModal = () => {
  const detailModalSelector = useSelector(ModalSelector);
  const { detailModalIsOpen } = useAppSelector((state) => state.modal);
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

  const scheduleDetail = useSelector(detailSelector).scheduleModal.scheduleDetail;

  // useEffect(() => {
  //   if (eventModalData !== null) {
  //     const { date, startTime } = eventModalData;
  //     setDate(date);

  //     const foundTimeIndex = startSelectOptions.findIndex((option) => option.value === startTime);

  //     foundTimeIndex !== undefined ? setStartTimeIndex(foundTimeIndex) : setStartTimeIndex(0);
  //   } else {
  //     handleResetInput();
  //   }
  // }, [eventModalData]);

  // useEffect(() => {
  //   setStartTime(startSelectOptions[startTimeIndex]);

  //   if (startTimeIndex > endTimeIndex) {
  //     setEndTimeIndex(startTimeIndex);
  //     setEndTime(startSelectOptions[startTimeIndex]);
  //   }
  // }, [startTimeIndex]);

  const handleToggleModal = useCallback(() => {
    dispatch(setDetailModalOpen());
  }, []);

  // const handleSubmit = () => {
  //   const newEvent: NewEvent = {
  //     date,
  //     eventDetail: {
  //       title: title !== '' ? title : '제목 없음',
  //       start: startTime.value,
  //       end: endTime.value,
  //     },
  //   };

  //   dispatch(addEvent(newEvent));
  //   handleToggleModal();
  //   handleResetInput();
  // };

  // const handleResetInput = useCallback(() => {
  //   setTitle('');
  //   setDate(getStringDateFormat(new Date()));
  //   setStartTime(startSelectOptions[0]);
  //   setStartTimeIndex(0);
  //   setEndTime(endSelectOptions[0]);
  //   setEndTimeIndex(0);
  // }, []);

  // const handleStartSelectClick = useCallback((selected: Option, index?: number) => {
  //   const i = index as number;
  //   setStartTimeIndex(i);
  //   setStartTime(selected);
  // }, []);

  // const handleEndSelectClick = useCallback((selected: Option, index?: number) => {
  //   setEndTimeIndex(index as number);
  //   setEndTime(selected);
  // }, []);

  // const defaultProps = {
  //   options: channels,
  //   getOptionLabel: (option: ChannelOptionType) => option.title,
  // };
  // const flatProps = {
  //   options: channels.map((option) => option.title),
  // };
  // const [value, setValue] = React.useState<ChannelOptionType | null>(null);

  return (
    <div className={`${detailModalSelector.detailModalIsOpen ? 'fixed' : 'hidden'} w-[100%] h-[100%] flex justify-center items-center`}>
      <div
        className="w-[600px] h-[600px] flex flex-col items-center bg-background z-10 rounded drop-shadow-shadow"
        onClick={(e: React.MouseEvent<HTMLDivElement>) => {
          e.stopPropagation();
        }}
      >
        <svg
          xmlns="https://www.w3.org/2000/svg"
          fill="none"
          viewBox="0 0 24 24"
          strokeWidth="2.5"
          className="w-6 h-6 stroke-title mt-[15px] ml-[550px] cursor-pointer"
          onClick={handleToggleModal}
        >
          <path strokeLinecap="round" strokeLinejoin="round" d="M6 18L18 6M6 6l12 12" />
        </svg>
        <div className="flex flex-col p-[20px] ">
          <div className="mt-[20px] flex ">
            <div className="text-s text-title font-bold mr-[15px]">미팅명</div>
            <p>{scheduleDetail.title}</p>
          </div>
          <div className="mt-[20px] flex">
            <div className="text-s text-title font-bold mr-[15px]">날짜</div>
            <p>{scheduleDetail.start.slice(0, 10)}</p>
          </div>
          <div className="mt-[20px] flex">
            <div className="text-s text-title font-bold mr-[15px]">시간</div>
            <p>
              {scheduleDetail.start.slice(11, 16)} - {scheduleDetail.end.slice(11, 16)}
            </p>
          </div>
          <div className="mt-[20px] flex">
            <div className="text-s text-title font-bold mr-[15px]">내용</div>
            <p className="w-[450px]">{scheduleDetail.content}</p>
          </div>

          {scheduleDetail ? (
            <div></div>
          ) : (
            <div className="mt-[20px] flex flex-col">
              <div className="text-s text-title font-bold mb-[10px]">웹엑스 미팅 참여하기</div>
              <div className="flex justify-center items-center gap-x-[50px]">
                <div className="flex flex-col justify-center items-center">
                  <img className="w-[60px]" src={webex} alt="webex" />
                  <a href="#">이태희(컨설턴트)</a>
                </div>
                <div className="flex flex-col justify-center items-center">
                  <img className="w-[60px]" src={webex} alt="webex" />
                  <a href="#">박성민[서울_1반_A102]팀장</a>
                </div>
              </div>
            </div>
          )}
        </div>
        <div className="flex justify-center items-center gap-[20px] mt-[40px]">
          <button
            // onClick={handleSubmit}
            className="font-bold bg-title hover:bg-hover text-background rounded w-[200px] h-s drop-shadow-button"
          >
            밋업 수정하기
          </button>
          <button
            // onClick={handleSubmit}
            className="text-[16px] font-bold bg-background border-solid border-2 border-cancel text-cancel hover:bg-cancelhover hover:text-background rounded w-[200px] h-s drop-shadow-button"
          >
            밋업 삭제하기
          </button>
        </div>
      </div>
      <div
        className="w-[100%] h-[100%] fixed top:0 z-9 bg-[rgba(0,0,0,0.45)]"
        onClick={(e: React.MouseEvent) => {
          e.preventDefault();
        }}
      />
    </div>
  );
};

export default DetailModal;
