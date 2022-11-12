import React from 'react';
import { useNavigate } from 'react-router-dom';
import { tMember } from '../../types/members';

interface MemberListItemProps {
  member: tMember;
}

export const MemberListItem: React.FC<MemberListItemProps> = ({ member }) => {
  return (
    <a href={`/calendar/${member.id}`}>
      <div className="MemberListItem p-[5px] hover:bg-line cursor-pointer">
        <div className="text-[18px] font-bold pb-2">{member.nickname}</div>
        <hr className="text-placeholder" />
      </div>
    </a>
  );
};

export default MemberListItem;
