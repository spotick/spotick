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
    }

    return {
        getList: getList,
    }
})();