import { tMember } from '../../types/members';
import { useAppDispatch, useAppSelector } from '../../stores/ConfigHooks';
import { useCallback } from 'react';
import { memberSelector } from '../../stores/modules/members';
import MemberListItem from './MemberListItem';
import { setMemberListModalOpen } from '../../stores/modules/modal';

function MemberListModal() {
  const dispatch = useAppDispatch();
  const channelTitle: string = useAppSelector((state: any) => state.channelInfo.value.title);

  // 받아온 member array를 nickname 순으로 정렬
  const member = useAppSelector(memberSelector).members;
  const memberSort = [...member].sort(function (a, b) {
    return a.nickname < b.nickname ? -1 : a.nickname > b.nickname ? 1 : 0;
  });

  // 모달 닫힘 관리
  const { memberListModalIsOpen } = useAppSelector((state) => state.modal);
  const handleToggleModal = useCallback(() => {
    dispatch(setMemberListModalOpen());
  }, [dispatch]);

  if (!member) {
    return <div>멤버가 없습니다</div>;
  }
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
        <p className="text-placeholder text-[14px] ml-[12px]">{channelTitle} 채널 내 멤버</p>
        <div className="mt-[10px] mx-3 h-[320px] overflow-auto scrollbar-hide">
          {memberSort.map((value: tMember, index: number) => (
            <MemberListItem key={value.id} member={value} />
          ))}
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

export default MemberListModal;
