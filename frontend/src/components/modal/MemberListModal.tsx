import { PropsWithChildren } from 'react';

import { tMember } from '../../types/members';
import { useAppDispatch, useAppSelector } from '../../stores/ConfigHooks';
import { useEffect, useState } from 'react';
import { fetchMemberList, memberSelector } from '../../stores/modules/members';
import { channelSelector } from '../../stores/modules/channels';
import MemberListItem from './MemberListItem';
import Spinner from '../common/Spinner';
import { useSelector } from 'react-redux';
import { axiosInstance } from '../../components/auth/axiosConfig';

interface ModalDefaultType {
  onClickToggleModal: () => void;
}


function MemberListModal({ onClickToggleModal }: PropsWithChildren<ModalDefaultType>) {
  const dispatch = useAppDispatch();

  const [member, setMember] = useState([]);
  const channel = useSelector(channelSelector);
  const meetupId = channel.channels;

  // console.log(meetUpId)
  useEffect(() => {
    axiosInstance
      .get('/meetup/users/1')
      .then((res) => {
        setMember(res.data.userInfoDtoList);
      })
      .catch((err) => {
        console.log('에러임');
      });
  });

  useEffect(() => {
    console.log('!!');
    dispatch(fetchMemberList());
  }, []);

  // if ( !member.loading ) {
  //   return <Spinner></Spinner>
  // }
  if (!member) {
    return <div>멤버가 없습니다</div>;
  }
  return (
    <div className="w-[100%] h-[100%] fixed flex justify-center items-center">
      <div className="w-[450px] h-[400px] bg-background z-10 rounded drop-shadow-shadow">
        <svg
          onClick={onClickToggleModal}
          xmlns="https://www.w3.org/2000/svg"
          fill="none"
          viewBox="0 0 24 24"
          strokeWidth="2.5"
          className="w-6 h-6 stroke-title mt-[15px] ml-[420px] cursor-pointer"
        >
          <path strokeLinecap="round" strokeLinejoin="round" d="M6 18L18 6M6 6l12 12" />
        </svg>
        <p className="text-placeholder text-[16px] ml-[5px]">서울1반_팀장채널 내 멤버</p>
        <div className="mt-[10px] h-[320px] overflow-auto scrollbar-hide">

          {member.map((value: tMember, index: number) => (
            <MemberListItem key={value.id} member={value} />
          ))}
        </div>
      </div>
      <div
        className="w-[100%] h-[100%] fixed top:0 z-9 bg-[rgba(0,0,0,0.45)]"
        onClick={(e: React.MouseEvent) => {
          e.preventDefault();

          if (onClickToggleModal) {
            onClickToggleModal();
          }
        }}
      />
    </div>
  );
}

export default MemberListModal;
