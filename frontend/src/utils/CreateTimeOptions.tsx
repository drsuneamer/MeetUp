import { getHours } from './GetHours';

export type Option = {
  [key: string]: string;
  value: string;
  label: string;
};

const hours = getHours();
const defaultMinutes = ['00', '15', '30', '45'];

export const createTimeOptions = (
  minutes: string[] = defaultMinutes,
): Option[] => {
  const options: Option[] = [];

  hours.forEach((hour, index) => {
    minutes.forEach(min => {
      options.push({
        value: index.toString() + min,
        label: hour + ':' + min,
      });
    });
  });

  return options;
};