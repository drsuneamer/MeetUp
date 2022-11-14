import React, { useCallback, useEffect, useMemo, useState } from 'react';
import { useAppDispatch, useAppSelector } from '../../stores/ConfigHooks';
import { setDetailModalOpen } from '../../stores/modules/modal';
import { getStringDateFormat } from '../../utils/GetStringDateFormat';
import { createTimeOptions, Option } from '../../utils/CreateTimeOptions';
import { useSelector } from 'react-redux';
import { ModalSelector } from '../../stores/modules/modal';
import { detailSelector } from '../../stores/modules/schedules';
import { setEditModalOpen } from '../../stores/modules/modal';
import { setDeleteModalOpen } from '../../stores/modules/modal';
import { fetchAlarmChannelList } from '../../stores/modules/channelAlarm';
import webex from '../../assets/webex_icon.png';
import { fetchGroupList, groupSelector } from '../../stores/modules/groups';

const DetailModal = () => {
  const dispatch = useAppDispatch();
  const detailModalSelector = useSelector(ModalSelector);
  const scheduleDetail = useSelector(detailSelector).scheduleModal.scheduleDetail;
  const myId = localStorage.getItem('id');

  // useEffect(() => {
  //   console.log('스케줄 신청자:', scheduleDetail.userId);
  //   console.log('현재 로그인한 유저:', myId);
  //   console.log(scheduleDetail);
  // });
  const handleToggleModal = useCallback(() => {
    dispatch(setDetailModalOpen('close'));
  }, []);

  const editMeeting = () => {
    dispatch(setEditModalOpen('meeting'));
    dispatch(fetchAlarmChannelList(scheduleDetail.managerId));
    dispatch(fetchGroupList());
    handleToggleModal();
  };

  const editSchedule = () => {
    dispatch(setEditModalOpen('schedule'));
    dispatch(fetchAlarmChannelList(scheduleDetail.userId));
    handleToggleModal();
  };
  // const editSchedule = () => {
  //   dispatch(setEditModalOpen('schedule'));
  //   dispatch(fetchAlarmChannelList(scheduleDetail.managerId));
  //   handleToggleModal();
  // };

  const deleteMeeting = () => {
    dispatch(setDeleteModalOpen(['delete', 'meeting']));
    handleToggleModal();
  };

  const deleteSchedule = () => {
    dispatch(setDeleteModalOpen(['delete', 'schedule']));
    handleToggleModal();
  };

  if (scheduleDetail) {
    return (
      <div className={`${detailModalSelector.detailModalIsOpen ? 'fixed' : 'hidden'} w-[100%] h-[100%] flex justify-center items-center z-30`}>
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
              {scheduleDetail && detailModalSelector.modalType === 'myMeeting' ? (
                <>
                  <div className="text-s text-title font-bold mr-[15px]">미팅명</div>
                  <p className="font-bold">{scheduleDetail.title}</p>
                </>
              ) : (
                <>
                  <div className="text-s text-title font-bold mr-[15px]">제목</div>
                  <p className="font-bold">{scheduleDetail.title}</p>
                </>
              )}
            </div>
            {scheduleDetail && detailModalSelector.modalType === 'myMeeting' ? (
              <>
                <div className="mt-[20px] flex ">
                  <div className="text-s text-title font-bold mr-[15px]">신청자</div>
                  <p>{scheduleDetail.userName}</p>
                </div>
              </>
            ) : null}
            {scheduleDetail && detailModalSelector.modalType === 'myMeeting' && scheduleDetail.partyId ? (
              <>
                <div className="mt-[20px] flex ">
                  <div className="text-s text-title font-bold mr-[15px]">신청그룹</div>
                  <p>{scheduleDetail.partyName}</p>
                </div>
              </>
            ) : null}
            <div className="mt-[20px] flex">
              <div className="text-s text-title font-bold mr-[15px]">날짜</div>
              {scheduleDetail && detailModalSelector.modalType === 'myMeeting' ? (
                <p>{scheduleDetail.start.slice(0, 10)}</p>
              ) : (
                <p>{scheduleDetail.start.slice(0, 10)}</p>
              )}
            </div>
            <div className="mt-[20px] flex">
              <div className="text-s text-title font-bold mr-[15px]">시간</div>
              {scheduleDetail && detailModalSelector.modalType === 'myMeeting' ? (
                <p>
                  {scheduleDetail.start.slice(11, 16)} - {scheduleDetail.end.slice(11, 16)}
                </p>
              ) : (
                <p>
                  {scheduleDetail.start.slice(11, 16)} - {scheduleDetail.end.slice(11, 16)}
                </p>
              )}
            </div>
            <div className="mt-[20px] flex">
              {scheduleDetail && detailModalSelector.modalType === 'myMeeting' ? (
                <>
                  <div className="text-s text-title font-bold mr-[15px]">내용</div>
                  <p className="w-[450px]">{scheduleDetail.content}</p>
                </>
              ) : (
                <>
                  <div className="text-s text-title font-bold mr-[15px]"></div>
                  <p className="w-[450px]">{scheduleDetail.content}</p>
                </>
              )}
            </div>

            {scheduleDetail && detailModalSelector.modalType === 'myMeeting' && scheduleDetail.diffWebex ? (
              <div className="mt-[20px] flex flex-col">
                <div className="text-s text-title font-bold mb-[20px]">웹엑스 미팅 참여하기</div>
                <div className="flex justify-center gap-[20px] mt-[10px]">
                  <div className="flex justify-center items-center gap-x-[50px]">
                    <div className="flex flex-col justify-center items-center">
                      <a href={scheduleDetail.diffWebex} className="flex flex-col justify-center items-center">
                        <img className="w-[50px]" src={webex} alt="webex" />
                        <p className="font-bold">{scheduleDetail.managerName}</p>
                      </a>
                    </div>
                  </div>
                  <div className="flex justify-center items-center gap-x-[50px]">
                    <div className="flex flex-col justify-center items-center">
                      <a href={scheduleDetail.myWebex} className="flex flex-col justify-center items-center">
                        <img className="w-[50px]" src={webex} alt="webex" />
                        <p className="font-bold">{localStorage.getItem('nickname')}</p>
                      </a>
                    </div>
                  </div>
                </div>
              </div>
            ) : null}
          </div>
          {scheduleDetail && detailModalSelector.modalType === 'myCalendar' ? (
            <div className="flex justify-center items-center gap-[20px] mt-[40px]">
              <button onClick={editSchedule} className="font-bold bg-title hover:bg-hover text-background rounded w-[200px] h-s drop-shadow-button">
                일정 수정하기
              </button>
              <button
                onClick={deleteSchedule}
                className="text-[16px] font-bold bg-background border-solid border-2 border-cancel text-cancel hover:bg-cancelhover hover:text-background rounded w-[200px] h-s drop-shadow-button"
              >
                일정 삭제하기
              </button>
            </div>
          ) : scheduleDetail.userId === myId ? (
            <div className="flex justify-center items-center gap-[20px] mt-[40px]">
              <button onClick={editMeeting} className="font-bold bg-title hover:bg-hover text-background rounded w-[200px] h-s drop-shadow-button">
                밋업 수정하기
              </button>
              <button
                onClick={deleteMeeting}
                className="text-[16px] font-bold bg-background border-solid border-2 border-cancel text-cancel hover:bg-cancelhover hover:text-background rounded w-[200px] h-s drop-shadow-button"
              >
                밋업 삭제하기
              </button>
            </div>
          ) : null}
        </div>
        <div
          className="w-[100%] h-[100%] fixed top:0 z-9 bg-[rgba(0,0,0,0.45)]"
          onClick={(e: React.MouseEvent) => {
            e.preventDefault();
          }}
        />
      </div>
    );
  }
  return null;
};

export default DetailModal;
