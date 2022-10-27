export const getHours = (start = 0, end = 24) => {
    const hours = [];
  
    for (let i = start; i < end; i++) {
      if (i === 0) {
        hours.push(`오전 12시`);
      } else if (i > 12) {
        hours.push(`오후 ${i - 12}시`);
      } else {
        hours.push(`오전 ${i}시`);
      }
    }
  
    return hours;
  };