function getTimeGapFromNow(datetime) {
    const now = new Date();
    const date = new Date(datetime);
    let gap = Math.floor((now.getTime() - date.getTime()) / 1000 / 60);

    if (gap < 1) {
        return "방금 전";
    }
    if (gap < 60) {
        return `${gap}분 전`;
    }

    gap = Math.floor(gap / 60);
    if (gap < 24) {
        return `${gap}시간 전`;
    }

    gap = Math.floor(gap / 24);
    if (gap < 32) {
        return `${gap}일 전`;
    }

    gap = Math.floor(gap / 31);
    if (gap < 13) {
        return `${gap}개월 전`;
    }

    gap = Math.floor(gap / 12);
    return `${gap}년 전`;
}


function formatKoreanDatetime(datetimeString) {
    return new Date(datetimeString).toLocaleString('ko-KR', {
        year: 'numeric',
        month: 'long',
        day: 'numeric',
        hour: 'numeric',
        minute: 'numeric',
        hour12: false,
    });
}

function formatKoreanDate(dateString) {
    return new Date(dateString).toLocaleString('ko-KR', {
        year: 'numeric',
        month: 'long',
        day: 'numeric'
    });
}

function dateDifferenceInDays(date1, date2) {
    const oneDay = 24 * 60 * 60 * 1000;

    const timeDifference = Math.abs(date2.getTime() - date1.getTime());

    return Math.round(timeDifference / oneDay);
}

function formatDate(date) {

    const year = date.getFullYear();
    const month = String(date.getMonth() + 1).padStart(2, '0');
    const day = String(date.getDate()).padStart(2, '0');

    return `${year}-${month}-${day}`;
}

export {getTimeGapFromNow, formatKoreanDatetime, formatKoreanDate, dateDifferenceInDays, formatDate};