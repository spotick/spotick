export const promotionService = (() => {

    const getList = async (category, sortType, page, keyword, callback) => {
        let uri = `/promotion/api/list?page=${page}&sort=${sortType}`;

        if (category) {
            uri += `&category=${category}`;
        }
        if (keyword) {
            uri += `&keyword=${encodeURIComponent(keyword)}`;
        }

        const response = await fetch(uri);
        const data = await response.json();

        if (callback) {
            return callback(data.data);
        }

        return data.data;
    }

    const getListOfUser = async (userId, promotionId, page, callback) => {
        const response = await fetch(`/promotion/api/list/${userId}/${promotionId}?page=${page}`, {
            method: 'GET',
        });

        const data = await response.json();

        if (callback) {
            return callback(data.data);
        }

        return data.data;
    }

    const likeRequest = async (promotionId, status) => {
        const response = await fetch(`/promotion/api/like?promotionId=${promotionId}&status=${status}`,
            {method: 'GET'}
        );

        return response.json();
    }

    return {
        getList: getList,
        getListOfUser: getListOfUser,
        likeRequest: likeRequest,
    }
})();