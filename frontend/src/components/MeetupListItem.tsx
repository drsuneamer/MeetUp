function MeetupListItem() {
  return (
    <div className="MeetupListItem w-full">
      <div className="indexContext bg-offWhite w-full h-[40px] flex flex-wrap">
        <div className="indexLable bg-title w-3/12 h-[40px] flex justify-end">
          <div className="bg-title mix-blend-multiply w-1/6 h-[40px]" />
        </div>
        <span className="MeetupName leading-[40px] w-9/12 text-center">
          Meetup Name
        </span>
      </div>
  </div>
  );
}
  
  export default MeetupListItem;
  