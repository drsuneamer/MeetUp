import React, { useCallback, useEffect, useMemo, useState } from 'react';
import { useAppDispatch, useAppSelector } from '../stores/ConfigHooks';
import { setEventModalOpen } from '../stores/modules/modal';
import { getStringDateFormat } from '../utils/GetStringDateFormat';
import { createTimeOptions, Option } from '../utils/CreateTimeOptions';
import { addEvent } from '../stores/modules/events';
import { NewEvent } from '../types/events';
import SingleSelect from './SingleSelect';
import Button from './Button';

const EventModal = () => {
  const { eventModalIsOpen } = useAppSelector(state => state.modal);
  const { eventModalData } = useAppSelector(state => state.events);
  const dispatch = useAppDispatch();

  const [title, setTitle] = useState<string>('');
  const [date, setDate] = useState<string>(getStringDateFormat(new Date()));

  const startSelectOptions: Option[] = useMemo(() => createTimeOptions(), []);
  const [startTime, setStartTime] = useState<Option>(startSelectOptions[0]);
  const [startTimeIndex, setStartTimeIndex] = useState<number>(0);

  const endSelectOptions: Option[] = useMemo(
    () => createTimeOptions().slice(startTimeIndex),
    [startTimeIndex],
  );
  const [endTime, setEndTime] = useState<Option>(endSelectOptions[0]);
  const [endTimeIndex, setEndTimeIndex] = useState<number>(0);

  useEffect(() => {
    if (eventModalData !== null) {
      const { date, startTime } = eventModalData;
      setDate(date);

      const foundTimeIndex = startSelectOptions.findIndex(
        option => option.value === startTime,
      );

      foundTimeIndex !== undefined
        ? setStartTimeIndex(foundTimeIndex)
        : setStartTimeIndex(0);
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

  const handleStartSelectClick = useCallback(
    (selected: Option, index?: number) => {
      const i = index as number;
      setStartTimeIndex(i);
      setStartTime(selected);
    },
    [],
  );

  const handleEndSelectClick = useCallback(
    (selected: Option, index?: number) => {
      setEndTimeIndex(index as number);
      setEndTime(selected);
    },
    [],
  );

  return (
    <div
      className={`${
        eventModalIsOpen ? 'fixed' : 'hidden'
      } top-0 left-0 flex justify-center w-full h-full`}>
      <div
        className="fixed cursor-pointer w-full h-full"
        onClick={handleToggleModal}
      />

      <div
        className="absolute top-1/2 left-1/2 translate-x-[-50%] translate-y-[-50%] w-96 h-max bg-background shadow-2xl rounded-lg "
        onClick={(e: React.MouseEvent<HTMLDivElement>) => {
          e.stopPropagation();
        }}>
        <header className="h-[36px] bg-label/50 flex justify-end align-center rounded-t-lg">
          <Button className="text-label mr-3" onClick={handleToggleModal}>
            ✖
          </Button>
        </header>

        <div className="p-3">
          <div className="flex flex-col flex-wrap justify-center">
            <input
              type="text"
              name="제목"
              placeholder="제목 추가"
              value={title}
              className="mt-3 pb-2 text-label text-xl w-full border-b-2 border-label after:transition-[width] focus:outline-none focus:border-title"
              onChange={e => setTitle(e.target.value)}
            />

            <div className="mt-3 flex items-center">
              <svg
                xmlns="http://www.w3.org/2000/svg"
                fill="none"
                viewBox="0 0 24 24"
                strokeWidth="1.5"
                stroke="currentColor"
                className="w-5 h-5 mr-1">
                <path
                  strokeLinecap="round"
                  strokeLinejoin="round"
                  d="M12 6v6h4.5m4.5 0a9 9 0 11-18 0 9 9 0 0118 0z"
                />
              </svg>

              <input
                className="w-24 text-sm"
                type="date"
                value={date}
                onChange={e => setDate(e.target.value)}
              />

              <div className="flex items-center">
                <SingleSelect
                  className="text-sm"
                  options={startSelectOptions}
                  onChange={handleStartSelectClick}
                  selected={startTime}
                />

                <span className="mx-2">-</span>

                <SingleSelect
                  className="text-sm"
                  options={endSelectOptions}
                  onChange={handleEndSelectClick}
                  selected={endTime}
                />
              </div>
            </div>

            <div className="flex justify-end p-3 mt-5">
              <Button
                className="bg-title hover:bg-hover px-6 py-2 rounded text-background"
                onClick={handleSubmit}>
                저장
              </Button>
            </div>
          </div>
        </div>
      </div>
    </div>
  );
};

export default EventModal;