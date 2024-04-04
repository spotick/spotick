export async function requestTicketInquiryRegister(inquiryReq) {
    try {
        const response = await fetch(`/tickets/api/inquiry/register`, {
            method: 'POST',
            headers: {
                'Content-Type': 'application/json',
            },
            body: JSON.stringify(inquiryReq),
        });

        return await response;

    } catch (error) {
        console.error(error);
    }
}

export async function requestTicketInquiryList(ticketId, page) {
    try {
        const response = await fetch(`/tickets/api/inquiry/${ticketId}/list?page=${page}`, {
            method: 'GET',
        });

        return await response.json();

    } catch (error) {
        console.error(error);
    }
}