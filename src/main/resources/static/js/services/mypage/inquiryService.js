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

    return {
        getPlaceInquiriesOfUser: getPlaceInquiriesOfUser,
        getTicketInquiriesOfUser: getTicketInquiriesOfUser,
        deletePlaceInquiry: deletePlaceInquiry,
        deleteTicketInquiry: deleteTicketInquiry,
    }
})();