import React from 'react';
import { useNavigate } from 'react-router-dom';
import { tChannel } from '../types/channels';
import { useAppDispatch } from '../stores/ConfigHooks';
import { update } from '../stores/modules/channelInfo';
import { setMemberListModalOpen } from '../stores/modules/modal';
import { fetchMemberList } from '../stores/modules/members';

interface ChannelListItemProps {
  channel: tChannel;
}

export const ChannelListItem: React.FC<ChannelListItemProps> = ({ channel }) => {
  const dispatch = useAppDispatch();
  const navigate = useNavigate();

  // 멤버를 불러올 채널의 id는 따로 redux를 이용하지 않고, dispatch 함수에 parameter로 바로 전달
  const handleMemberListModal = () => {
    dispatch(update({ id: channel.id, title: channel.title }));
    dispatch(fetchMemberList(Number(channel.id)));
    dispatch(setMemberListModalOpen('meetup'));
  };

  // 톱니바퀴 버튼 누르면 -> 밋업 수정 페이지로 이동
  const goEdit = (event: React.MouseEvent<HTMLButtonElement>) => {
    event.stopPropagation(); // 버블링으로 인해 멤버 모달까지 함께 뜨는 것을 막음
    // console.log(channel.id);
    // console.log(typeof channel.id);
    dispatch(update({ id: channel.id }));
    navigate(`/edit-meetup/${channel.id}`);
  };

  return (
    <div className="ChannelListItem w-full mb-1 drop-shadow-button cursor-pointer">
      <div onClick={handleMemberListModal} className="indexContext bg-offWhite w-full h-[40px] flex flex-wrap ">
        <div style={{ background: `${channel.color}` }} className="indexLable  w-3/12 h-[40px] flex justify-end">
          <div style={{ background: `${channel.color}` }} className=" mix-blend-multiply y w-1/6 h-[40px]" />
        </div>
        <span className="channelName leading-[40px] w-7/12 text-center overflow-hidden truncate">{channel.title}</span>
        <div className="ColDiv flex flex-col justify-center">
          {/* settings button */}
          <span className="w-2/12 text-center cursor-pointer" onClick={goEdit}>
            <svg xmlns="https://www.w3.org/2000/svg" viewBox="0 0 24 24" fill="currentColor" className="w-6 h-6 text-label">
              <path
                fillRule="evenodd"
                d="M11.078 2.25c-.917 0-1.699.663-1.85 1.567L9.05 4.889c-.02.12-.115.26-.297.348a7.493 7.493 0 00-.986.57c-.166.115-.334.126-.45.083L6.3 5.508a1.875 1.875 0 00-2.282.819l-.922 1.597a1.875 1.875 0 00.432 2.385l.84.692c.095.078.17.229.154.43a7.598 7.598 0 000 1.139c.015.2-.059.352-.153.43l-.841.692a1.875 1.875 0 00-.432 2.385l.922 1.597a1.875 1.875 0 002.282.818l1.019-.382c.115-.043.283-.031.45.082.312.214.641.405.985.57.182.088.277.228.297.35l.178 1.071c.151.904.933 1.567 1.85 1.567h1.844c.916 0 1.699-.663 1.85-1.567l.178-1.072c.02-.12.114-.26.297-.349.344-.165.673-.356.985-.57.167-.114.335-.125.45-.082l1.02.382a1.875 1.875 0 002.28-.819l.923-1.597a1.875 1.875 0 00-.432-2.385l-.84-.692c-.095-.078-.17-.229-.154-.43a7.614 7.614 0 000-1.139c-.016-.2.059-.352.153-.43l.84-.692c.708-.582.891-1.59.433-2.385l-.922-1.597a1.875 1.875 0 00-2.282-.818l-1.02.382c-.114.043-.282.031-.449-.083a7.49 7.49 0 00-.985-.57c-.183-.087-.277-.227-.297-.348l-.179-1.072a1.875 1.875 0 00-1.85-1.567h-1.843zM12 15.75a3.75 3.75 0 100-7.5 3.75 3.75 0 000 7.5z"
                clipRule="evenodd"
              />
            </svg>
          </span>
        </div>
      </div>
    </div>
  );
};

export default ChannelListItem;
