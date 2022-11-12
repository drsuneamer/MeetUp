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

  const handleMemberListModal = () => {
    dispatch(update({ id: group.id, name: group.name }));
    dispatch(fetchGroupMemberList(group.id));
    dispatch(setMemberListModalOpen('group'));
  };

  const onDelete = () => {
    dispatch(update({ id: group.id, name: group.name }));
    dispatch(setDeleteModalOpen(['delete', 'group']));
  };

  return (
    <div key={group.id} className="w-[18vw] my-[1vh] mr-[1vw] rounded shadow-button">
      <div className="flex items-center justify-between bg-point rounded-t text-s font-semibold px-2 pt-1 pb-3">
        <div className="flex items-center ">
          <div className="cursor-default">{group.name}</div>
          {/* 자신이 만든 그룹일 경우에만 왕관 아이콘 확인 가능 */}

          {group.leader === true ? <img className="h-[18px] w-[15px] ml-1" src={crown} alt="team-leader" /> : ''}
        </div>
        {/* 휴지통 아이콘 - 그룹 삭제 모달 연결 */}
        <svg
          onClick={onDelete}
          xmlns="https://www.w3.org/2000/svg"
          fill="none"
          viewBox="0 0 24 24"
          strokeWidth="1.5"
          stroke="black"
          className="w-5 h-5 cursor-pointer"
        >
          <path d="M14.74 9l-.346 9m-4.788 0L9.26 9m9.968-3.21c.342.052.682.107 1.022.166m-1.022-.165L18.16 19.673a2.25 2.25 0 01-2.244 2.077H8.084a2.25 2.25 0 01-2.244-2.077L4.772 5.79m14.456 0a48.108 48.108 0 00-3.478-.397m-12 .562c.34-.059.68-.114 1.022-.165m0 0a48.11 48.11 0 013.478-.397m7.5 0v-.916c0-1.18-.91-2.164-2.09-2.201a51.964 51.964 0 00-3.32 0c-1.18.037-2.09 1.022-2.09 2.201v.916m7.5 0a48.667 48.667 0 00-7.5 0" />
        </svg>
      </div>
      <div onClick={handleMemberListModal} className="text-[14px] px-1 py-1 text-end cursor-pointer">
        멤버 확인하기
      </div>
    </div>
  );
}

export default GroupListItem;
