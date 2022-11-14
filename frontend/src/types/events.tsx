export type EventDetail = {
  start: string;
  end: string;
  title: string;
};

export type NewEvent = {
  date: string;
  eventDetail: EventDetail;
};

export type SelectedEvent = {
  date: string;
  index: number;
};

export type EventModalData = {
  date: string;
  startTime: string;
};

export type HolidayDetail = {
  locdate: string;
  dateName: string;
};

export type tSchedule = {
  id: string;
  start: string;
  end: string;
  title: string;
  content: string;
  userId: string;
  userName: string;
  meetupName: string;
  meetupColor: string;
  open: boolean;
};

export type tScheduleDetail = {
  id: number;
  start: string;
  end: string;
  title: string;
  content: string;
  userId: string;
  userName: string;
  myWebex: string;
  diffWebex: string;
  managerId: string;
  managerName: string;
  meetupId: number;
  open: boolean;
  partyId: number;
  partyName: string;
};

export type Day = {
  isToday: boolean;
  day: number;
  date: number;
  stringDate: string;
};

export type tPartyDetail = {
  id: string;
  start: string;
  end: string;
  title: string;
  content: string;
  userId: string;
  userName: string;
  meetupName: string;
  meetupColor: string;
  open: boolean;
  partyId: string;
  partyName: string;
};
