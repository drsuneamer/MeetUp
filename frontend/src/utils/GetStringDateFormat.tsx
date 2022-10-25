const monthAndDayFormatter = (number: number) => {
  if (number < 10) {
    return `0${number.toString()}`;
  }

  return number;
};

export const getStringDateFormat = (date: Date, formatter = '-'): string => {
  const year = date.getFullYear();
  const month = monthAndDayFormatter(date.getMonth() + 1);
  const day = monthAndDayFormatter(date.getDate());

  return [year, month, day].join(formatter);
};