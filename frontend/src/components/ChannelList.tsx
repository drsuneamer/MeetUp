import ChannelListItem from './ChannelListItem';

function ChannelList() {
  return (
    <div className="ChannelList mb-[50px]">
      <div className="flex mb-[10px]">
        <h1 className="text-xl font-bold">채널 관리하기</h1>
        <div className="flex flex-col justify-center">
          {/* plus button */}
          <svg xmlns="http://www.w3.org/2000/svg" viewBox="0 0 24 24" fill="currentColor" className="w-8 h-8 text-body align-middle">
            <path
              fill-rule="evenodd"
              d="M12 2.25c-5.385 0-9.75 4.365-9.75 9.75s4.365 9.75 9.75 9.75 9.75-4.365 9.75-9.75S17.385 2.25 12 2.25zM12.75 9a.75.75 0 00-1.5 0v2.25H9a.75.75 0 000 1.5h2.25V15a.75.75 0 001.5 0v-2.25H15a.75.75 0 000-1.5h-2.25V9z"
              clip-rule="evenodd"
            />
          </svg>
        </div>
      </div>
      <ChannelListItem />
    </div>
  );
}

export default ChannelList;
