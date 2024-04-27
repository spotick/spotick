export const ticketService = (() => {

    const getList = async (page, category, ratingType, sortType, district, detailDistrict, keyword, callback) => {
        let uri = `/ticket/api/list?page=${page}&sortType=${sortType}`;
        if (category !== 'ALL') {
            uri += `&category=${category}`;
        }
        if (ratingType !== 'no') {
            uri += `&ratingType=${ratingType}`;
        }
        if (district) {
            uri += `&district=${encodeURIComponent(district)}`;
        }
        if (detailDistrict) {
            uri += `&detailDistrict=${encodeURIComponent(detailDistrict)}`;
        }
        if (keyword) {
            uri += `&keyword=${encodeURIComponent(keyword)}`;
        }

        const response = await fetch(uri);

        const data = await response.json();

        if (callback) {
            return callback(data);
        }

        return data;
    }

    /*
    * 한 티켓의 특정날짜를 받아와 그 날짜의 판매 갯수 또한 보여준다.
    * 반환 : commonResponse / data = gradeId, gradeName, price, sold, maxPeople;
    * */
    const getGrades = async (ticketId, date, callback) => {
        const response = await fetch(`/ticket/api/getGrades?ticketId=${ticketId}&date=${date}`, {
            method: 'GET'
        });

        const responseData = await response.json();

        if (callback) {
            return callback(responseData.data);
        }

        return responseData.data;
    }

    return {
        getList: getList,
        getGrades: getGrades
    }
})();