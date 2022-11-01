export const getSundayOfWeek = () => {
  let now = new Date();
  // console.log("현재 :", now); //  Mon Oct 31 2022 13:46:51 GMT+0900 (한국 표준시)

  const getFormatDate = (date: any) => {
    let year = date.getFullYear();
    let month = 1 + date.getMonth();
    month = month >= 10 ? month : '0' + month;
    let day = date.getDate();
    day = day >= 10 ? day : '0' + day;
    return year + '-' + month + '-' + day;
  };
  const week = ['SUN', 'MON', 'TUE', 'WED', 'THU', 'FRI', 'SAT'];
  const getDayOfWeek = (strDate: any) => {
    const dayOfWeek = week[new Date(strDate).getDay()];
    return dayOfWeek;
  };

  // console.log(getFormatDate(now)); // 2022-10-31
  // console.log(getDayOfWeek(getFormatDate(now))); //MON

  const getSundayOfWeek = (today: any) => {
    let diff = week.indexOf(today);
    let sunday = new Date(now.setDate(now.getDate() - diff));
    return sunday;
  };

  const sunday = getSundayOfWeek(getDayOfWeek(getFormatDate(now)));
  // console.log(sunday); // Sun Oct 30 2022 13:47:52 GMT+0900 (한국 표준시)
  // console.log(getFormatDate(sunday)); // 2022-10-30
  return sunday
};
