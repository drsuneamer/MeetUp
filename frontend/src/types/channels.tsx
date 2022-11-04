export type tChannel = {
  id: string;
  title: string;
  color: string;
};

export type tMeetup = {
  id: string;
  userName: string;
};

export type tAlarm = {
  meetupId: number;
  displayName: string;
}
