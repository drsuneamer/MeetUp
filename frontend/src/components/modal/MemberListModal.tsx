import { tMember } from '../../types/members';
import { useAppDispatch, useAppSelector } from '../../stores/ConfigHooks';
import { useState, useCallback, useEffect } from 'react';
import { memberSelector } from '../../stores/modules/members';
import { groupMemberSelector } from '../../stores/modules/groups';
import MemberListItem from './MemberListItem';
import { setMemberListModalOpen } from '../../stores/modules/modal';
import { useNavigate } from 'react-router-dom';

function MemberListModal() {
  const dispatch = useAppDispatch();
  const channelTitle: string = useAppSelector((state: any) => state.channelInfo.value.title);
  const groupName: string = useAppSelector((state: any) => state.groups.group.name);
  const { memberListModalIsOpen } = useAppSelector((state) => state.modal);
  const { memberListModalType } = useAppSelector((state) => state.modal);

  const meetupMembers = useAppSelector(memberSelector).members;
  const groupMembers = useAppSelector(groupMemberSelector).groupMembers;

  // 받아온 member array를 nickname 순으로 정렬
  const meetupMemberSort = [...meetupMembers].sort(function (a, b) {
    return a.nickname < b.nickname ? -1 : a.nickname > b.nickname ? 1 : 0;
  });

  const groupMemberSort = [...groupMembers].sort(function (a, b) {
    return a.nickname < b.nickname ? -1 : a.nickname > b.nickname ? 1 : 0;
  });

  // 모달 닫힘 관리
  const handleToggleModal = useCallback(() => {
    dispatch(setMemberListModalOpen('close'));
  }, [dispatch]);

  return (
    <div className={`${memberListModalIsOpen ? 'fixed' : 'hidden'} w-[100%] h-[100%] flex justify-center items-center`}>
      <div className="w-[450px] h-[400px] bg-background z-10 rounded drop-shadow-shadow">
        <svg
          onClick={handleToggleModal}
          xmlns="https://www.w3.org/2000/svg"
          fill="none"
          viewBox="0 0 24 24"
          strokeWidth="2.5"
          className="w-6 h-6 stroke-title mt-[15px] ml-[420px] cursor-pointer"
        >
          <path strokeLinecap="round" strokeLinejoin="round" d="M6 18L18 6M6 6l12 12" />
        </svg>
        {memberListModalType === 'meetup' ? (
          <div>
            <p className="text-placeholder text-[14px] ml-[12px]">{channelTitle} 채널 내 멤버</p>
            <div className="mt-[10px] mx-3 h-[320px] overflow-auto scrollbar-hide">
              {meetupMemberSort.map((value: tMember, index: number) => (
                <MemberListItem key={value.id} member={value} />
              ))}
            </div>
          </div>
        ) : (
          <div>
            <p className="text-placeholder text-[14px] ml-[12px]">{groupName} 그룹 내 멤버</p>
            <div className="mt-[10px] mx-3 h-[320px] overflow-auto scrollbar-hide">
              {groupMemberSort.map((value, index) => (
                <a key={index} href={`/calendar/${value.id}`}>
                  <div className="MemberListItem p-[5px] hover:bg-line cursor-pointer">
                    <div className="text-[18px] font-bold pb-2">{value.nickname}</div>
                    <hr className="text-placeholder" />
                  </div>
                </a>
              ))}
            </div>
          </div>
        )}
        {/*
         */}
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

export default MemberListModal;
