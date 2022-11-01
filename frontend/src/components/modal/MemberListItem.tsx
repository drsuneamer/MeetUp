import React from 'react';
import { tMember } from '../../types/members';

interface MemberListItemProps {
  member: tMember;
}

export const MemberListItem: React.FC<MemberListItemProps> = ({ member }) => {
  return (
    <div className="MemberListItem p-[5px] hover:bg-line">
      <div className="text-[18px] font-bold">{member.nickname}</div>
    </div>
  );
};

export default MemberListItem;
