import crown from '../assets/crown.png';
import { update } from '../stores/modules/groups';
import { fetchGroupMemberList } from '../stores/modules/groups';
import { setMemberListModalOpen } from '../stores/modules/modal';
import { useAppDispatch } from '../stores/ConfigHooks';
import { setDeleteModalOpen } from '../stores/modules/modal';

interface GroupListItemProps {
  group: {
    id: number;
    leader: boolean;
    name: string;
  };
  key: number;
}

function GroupListItem({ group }: GroupListItemProps) {
  const dispatch = useAppDispatch();

  // 멤버 확인하기 클릭 시 멤버 리스트 모달 상태 관리
  const handleMemberListModal = () => {
    dispatch(update({ id: group.id, name: group.name }));
    dispatch(fetchGroupMemberList(group.id));
    dispatch(setMemberListModalOpen('group'));
  };

  // 휴지통 버튼 클릭 시 삭제 모달 출력 (그룹의 id와 이름 값 redux에 전달)
  const onDelete = () => {
    dispatch(update({ id: group.id, name: group.name }));
    dispatch(setDeleteModalOpen(['delete', 'group']));
  };

  return (
    <div key={group.id} className="w-[18vw] my-[1vh] mr-[1vw] rounded shadow-button">
      <div className="flex items-center justify-between bg-point rounded-t text-s px-2 pb-4">
        <div className="flex items-center ">
          <div className="text-background cursor-default pt-1">{group.name}</div>
          {/* 자신이 만든 그룹일 경우에만 왕관 아이콘 확인 가능 */}

          {group.leader === true ? <img className="h-[18px] w-[15px] ml-1" src={crown} alt="team-leader" /> : ''}
        </div>
        {/* 휴지통 아이콘 - 그룹 삭제 모달 연결 */}

        <svg
          onClick={onDelete}
          xmlns="http://www.w3.org/2000/svg"
          fill="none"
          viewBox="0 0 24 24"
          strokeWidth="2.1"
          stroke="white"
          className="w-5 h-6"
        >
          <path d="M6 18L18 6M6 6l12 12" />
        </svg>
      </div>
      <div onClick={handleMemberListModal} className="text-[14px] px-1 py-1 text-end cursor-pointer">
        멤버 확인하기
      </div>
    </div>
  );
}

export default GroupListItem;
