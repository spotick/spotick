export async function ticketGradeFetch(ticketId, date, callback) {
    try {
        const response = await fetch(`/ticket/api/getGrades?ticketId=${ticketId}&date=${date}`, {
            method: 'GET'
        });
        const responseData = await response.json();

        if (callback) {
            callback(responseData.data);
            return responseData.data;
        }

        return responseData.data;
    } catch (error) {
        console.error('Error:', error);
    }
}