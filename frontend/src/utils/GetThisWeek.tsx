import { getStringDateFormat } from './GetStringDateFormat';

type Day = {
  isToday: boolean;
  day: number;
  date: number;
  stringDate: string;
};

export const getThisWeek = (selectedDate: string): Day[] => {
  const date = new Date(selectedDate);
  const today = new Date();

  const day = date.getDay();
  const month = date.getMonth();
  const year = date.getFullYear();

  let diff = date.getDate() - day;

  return Array.from({ length: 7 }, () => {
    const date = new Date(year, month, diff++);

    return {
      isToday:
        date.getDate() === today.getDate() &&
        date.getMonth() === today.getMonth(),
      day: date.getDay(),
      date: date.getDate(),
      stringDate: getStringDateFormat(date),
    };
  });
};