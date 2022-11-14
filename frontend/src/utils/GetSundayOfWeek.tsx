import { Day } from '../types/events';

const formatMonthWithZeroAndDateWithZero = (date: Date, month: number, weekly: Day[]) => {
  return `${date.getFullYear()}-0${month}-0${weekly[0].date}`;
};

const formatDateWithoutZero = (date: Date, month: number, weekly: Day[]) => {
  return `${date.getFullYear()}-${month}-${weekly[0].date}`;
};

const formatDateWithZero = (date: Date, month: number, weekly: Day[]) => {
  return `${date.getFullYear()}-${month}-0${weekly[0].date}`;
};

const formatMonthWithZero = (date: Date, month: number, weekly: Day[]) => {
  return `${date.getFullYear()}-0${month}-${weekly[0].date}`;
};

const isMonthOneDigit = (date: Date) => date.getMonth() + 1 < 10;

const isOverlapMonth = (date: Date) => date.getDate() <= date.getDay();

const formatDateWithOneDigit = (date: Date, month: number, weekly: Day[]) => {
  if (isMonthOneDigit(date)) {
    return formatMonthWithZeroAndDateWithZero(date, month, weekly);
  }
  return formatDateWithZero(date, month, weekly);
};

const formatDateWithTwoDigits = (date: Date, month: number, weekly: Day[]) => {
  if (isMonthOneDigit(date)) {
    return formatMonthWithZero(date, month, weekly);
  }
  return formatDateWithoutZero(date, month, weekly);
};

const determineMonth = (date: Date, month: number, weekly: Day[]) => {
  if (weekly[0].date < 10) {
    return formatDateWithOneDigit(date, month, weekly);
  } else {
    return formatDateWithTwoDigits(date, month, weekly);
  }
};

export const getSundayOfWeek = (currentDate: string, weekly: Day[]) => {
  const date = new Date(currentDate);

  if (isOverlapMonth(date)) {
    return determineMonth(date, date.getMonth(), weekly);
  } else {
    return determineMonth(date, date.getMonth() + 1, weekly);
  }
};
