export const inquiryService = (() => {

    const getPlaceInquiriesOfUser = async (page) => {
        const response = await fetch(`/inquiries/api/places?page=${page}`);
        return response.json();
    }

    const getTicketInquiriesOfUser = async (page) => {
        const response = await fetch(`/inquiries/api/tickets?page=${page}`);
        return response.json();
    }

    const deletePlaceInquiry = async (inquiryId) => {
        const response = await fetch(`/inquiries/api/placeDelete/${inquiryId}`, {
            method: 'DELETE'
        });
        return response.json();
    }

    const deleteTicketInquiry = async (inquiryId) => {
        const response = await fetch(`/inquiries/api/ticketDelete/${inquiryId}`, {
            method: 'DELETE'
        });
        return response.json();
    }

    const getPlaceInquiriesOfHost = async (placeId, page, callback) => {
        const response = await fetch(`/inquiries/api/getPlace/${placeId}?page=${page}`);
        const data = await response.json();

        if (callback) {
            return callback(data);
        }

        return data;
    }

    const responsePlaceInquiry = async (placeId, inquiryId, response) => {
        const inquiryResponseDto = {
            placeId: placeId,
            inquiryId: inquiryId,
            response: response
        }

        const res = await fetch(`/inquiries/api/responsePlaceInquiry`, {
            method: 'PATCH',
            headers: {
                'Content-Type': 'application/json',
            },
            body: JSON.stringify(inquiryResponseDto)
        });

        return res.json();
    }

    return {
        getPlaceInquiriesOfUser: getPlaceInquiriesOfUser,
        getTicketInquiriesOfUser: getTicketInquiriesOfUser,
        deletePlaceInquiry: deletePlaceInquiry,
        deleteTicketInquiry: deleteTicketInquiry,
        getPlaceInquiriesOfHost: getPlaceInquiriesOfHost,
        responsePlaceInquiry: responsePlaceInquiry,
    }
})();