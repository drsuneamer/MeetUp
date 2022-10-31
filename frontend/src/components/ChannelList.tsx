import ChannelListItem from './ChannelListItem';
import { tChannel } from '../types/channels';

import { Link } from 'react-router-dom';
import { useAppSelector } from '../stores/ConfigHooks';
import { useEffect } from 'react';


let channelArray: Array<tChannel> = [
  {
    id: 1,
    name: 'A102',
    color: '#ED8383',
  },
  {
    id: 2,
    name: '팀장채널',
    color: '#838EED',
  },
  {
    id: 3,
    name: '스크럼',
    color: '#BEED83',
  },
];

function ChannelList() {
  const channelSelector = useAppSelector((state) => (state.channels))

  useEffect(() => {
    console.log(channelSelector)
  })

  return (
    <div className="ChannelList mb-[50px] -z-10">
      <div className="flex mb-[10px]">
        <h1 className="text-xl font-bold">밋업 관리하기</h1>
        <Link to="/create-channel" className="flex flex-col justify-center">
          {/* plus button */}
          <svg xmlns="http://www.w3.org/2000/svg" viewBox="0 0 20 20" fill="currentColor" className="w-7 h-7 text-point">
            <path
              fillRule="evenodd"
              d="M10 18a8 8 0 100-16 8 8 0 000 16zm.75-11.25a.75.75 0 00-1.5 0v2.5h-2.5a.75.75 0 000 1.5h2.5v2.5a.75.75 0 001.5 0v-2.5h2.5a.75.75 0 000-1.5h-2.5v-2.5z"
              clipRule="evenodd"
            />
          </svg>
        </Link>
      </div>
      {channelArray.map((value: tChannel, index: number) => (
        <ChannelListItem key={value.id} channel={value} />
      ))}
    </div>
  );
}

export default ChannelList;
