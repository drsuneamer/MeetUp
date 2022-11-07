export const getNow = (start = 0, end = 24) => {
    let now = new Date()

    const result = {parsedTimeNow : "", timeNow : now.getHours()};

    if ( result.timeNow < 12 ) {
        result.parsedTimeNow = '오전 ' + result.timeNow.toString() + '시'
        return result;
    }
    else if ( result.timeNow === 12 ) {
        result.parsedTimeNow = '오후 12시'
        return result
    } else if ( result.timeNow > 12 ) {
        result.parsedTimeNow = '오후 ' + (result.timeNow-12) + '시'
        return result;
    }

  };
  