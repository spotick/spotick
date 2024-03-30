export const ticketService = (() => {

    const getList = async (page, ticketSortType, callback) => {
        const response = await fetch(`/ticket/api/list?page=${page}&ticketSortType=${ticketSortType}`,
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