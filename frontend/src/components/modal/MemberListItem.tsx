import React from 'react';
import { tMember } from '../../types/members';

interface MemberListItemProps {
  member: tMember;
}

export const MemberListItem: React.FC<MemberListItemProps> = ({ member }) => {
  return (
    <div key="{member}" className="MemberListItem p-[5px] hover:bg-line">
      <div className="text-[18px] font-bold pb-2">{member.nickname}</div>
      <hr className="text-placeholder" />
    </div>
  );
};

export default MemberListItem;
