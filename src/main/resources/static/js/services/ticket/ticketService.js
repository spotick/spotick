export const ticketService = (() => {

    const getList = async (page, category, ratingType, sortType, district, detailDistrict, callback) => {
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

        console.log(uri)

        const response = await fetch(uri,
            {
                method: "GET"
            }
        );

        const ticketsSlice = await response.json();

        if (callback) {
            return callback(ticketsSlice.content);
        }

        return ticketsSlice.content;
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