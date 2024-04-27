export const placeService = (() => {

    const getList = async (page, sort, district, detailDistrict, keyword, callback) => {
        let uri = `/place/api/list?page=${page}`;
        if (sort) {
            uri += `&sort=${sort}`;
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

        const response = await fetch(uri, {method: 'GET'});

        const data = await response.json();

        if (callback) {
            return callback(data);
        }

        return data;
    }


    return {
        getList: getList,
    }
})();