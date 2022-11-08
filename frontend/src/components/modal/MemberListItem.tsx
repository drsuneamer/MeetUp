import React from 'react';
import { useNavigate } from 'react-router-dom';
import { tMember } from '../../types/members';

interface MemberListItemProps {
  member: tMember;
}

export const MemberListItem: React.FC<MemberListItemProps> = ({ member }) => {
  const navigate = useNavigate();
  function memberCalendar() {
    navigate(`/calendar/${member.id}`);
    window.location.reload();
  }
  return (
    <div onClick={memberCalendar} key="{member}" className="MemberListItem p-[5px] hover:bg-line cursor-pointer">
      <div className="text-[18px] font-bold pb-2">{member.nickname}</div>
      <hr className="text-placeholder" />
    </div>
  );
};

export default MemberListItem;
