export const placeService = (() => {

    const getList = async (page, sort, area, keyword, callback) => {
        let uri = `/place/api/list?page=${page}`;
        if (sort) {
            uri += `&sort=${sort}`;
        }
        if (area) {
            uri += `&area=${encodeURIComponent(area)}`;
        }
        if (keyword) {
            uri += `&keyword=${encodeURIComponent(keyword)}`;
        }

        const response = await fetch(uri, {method: 'GET'});

        const data = await response.json();

        console.log(data);

        if (callback) {
            return callback(data.data);
        }

        return data.data;
    }


    return {
        getList: getList,
    }
})();