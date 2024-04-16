export const promotionService = (() => {

    const getList = async (category, page) => {

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

    return {
        getList: getList,
        getListOfUser: getListOfUser
    }
})();