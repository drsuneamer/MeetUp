export const getMonthly = (year: number, month: number): Date[][] => {
    const firstDayOfTheMonth = new Date(year, month, 1).getDay();
    let currentMonthCount = 0 - firstDayOfTheMonth;
  
    const monthly = Array.from({ length: 6 }, () =>
      Array(7)
        .fill(null)
        .map(() => {
          currentMonthCount++;
          return new Date(year, month, currentMonthCount);
        }),
    );
  
    return monthly;
  };