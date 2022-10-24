function ChannelListItem() {
    return (
      <div className="ChannelListItem w-full">
        <div className="indexContext bg-offWhite w-full h-[40px] flex flex-wrap">
          <div className="indexLable bg-[pink] w-3/12 h-[40px] flex justify-end">
            <div className="bg-[pink] mix-blend-multiply w-1/6 h-[40px]" />
          </div>
          <span className="channelName leading-[40px] w-9/12 text-center">
            channelName
          </span>
        </div>
    </div>
    );
  }

  export default ChannelListItem;
  