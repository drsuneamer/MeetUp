import { tMember } from '../../types/members';
import { useCallback } from 'react';
import { useAppSelector, useAppDispatch } from '../../stores/ConfigHooks';
import { memberSelector } from '../../stores/modules/members';
import { groupMemberSelector } from '../../stores/modules/groups';
import MemberListItem from './MemberListItem';
import { setMemberListModalOpen } from '../../stores/modules/modal';

function MemberListModal() {
  const dispatch = useAppDispatch();

  // 모달의 여닫힘 상태 관리
  const { memberListModalIsOpen } = useAppSelector((state) => state.modal);

  // 채널의 멤버인지, 그룹의 멤버인지를 구분하기 위한 타입 selector
  const { memberListModalType } = useAppSelector((state) => state.modal);

  // 모달 제목에 표시할 채널/그룹의 이름 selector
  const channelTitle: string = useAppSelector((state: any) => state.channelInfo.value.title);
  const groupName: string = useAppSelector((state: any) => state.groups.group.name);

  // 각 채널/그룹에 해당하는 멤버 selector
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
        {/* 모달 닫기 버튼 */}
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
          // 밋업 멤버를 표시하는 경우
          <div>
            <p className="text-placeholder text-[14px] ml-[12px]">{channelTitle} 채널 내 멤버</p>
            <div className="mt-[10px] mx-3 h-[320px] overflow-auto scrollbar-hide">
              {meetupMemberSort.map((value: tMember, index: number) => (
                <MemberListItem key={value.id} member={value} />
              ))}
            </div>
          </div>
        ) : (
          // 그룹 멤버를 표시하는 경우
          <div>
            <p className="text-placeholder text-[14px] ml-[12px]">{groupName} 그룹 내 멤버</p>
            <div className="mt-[10px] mx-3 h-[320px] overflow-auto scrollbar-hide">
              {groupMemberSort.map((value, index) => (
                <div key={index} className="MemberListItem p-[5px] cursor-default">
                  <div className="text-[18px] font-bold pb-2">{value.nickname}</div>
                  <hr className="text-placeholder" />
                </div>
              ))}
            </div>
          </div>
        )}
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
