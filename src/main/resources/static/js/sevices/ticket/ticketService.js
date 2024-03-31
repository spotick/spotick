export const ticketService = (() => {

    const getList = async (page, category, sortType, callback) => {
        let uri = `/ticket/api/list?page=${page}&sortType=${sortType}`;

        if (category !== 'ALL') {
            uri += `&category=${category}`;
        }

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