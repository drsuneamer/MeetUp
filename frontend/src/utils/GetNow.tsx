export const getNow = (start = 0, end = 24) => {
  let now = new Date();

  const result = { parsedTimeNow: '', hours: now.getHours(), minutes: now.getMinutes() };

  if (result.hours === 0) {
    result.parsedTimeNow = '오전 12시';
    return result;
  } else if (result.hours < 12) {
    result.parsedTimeNow = '오전 ' + result.hours.toString() + '시';
    return result;
  } else if (result.hours === 12) {
    result.parsedTimeNow = '오후 12시';
    return result;
  } else if (result.hours > 12) {
    result.parsedTimeNow = '오후 ' + (result.hours - 12) + '시';
    return result;
  }
};
