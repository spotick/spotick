export const reservationService = (() => {

    const cancelReservation = async (reservationId) => {
        const response = await fetch(`/reservation/api/cancel/${reservationId}`, {
            method: 'PATCH'
        });

        return response.json();
    }

    const deleteReservation = async (reservationId) => {
        const response = await fetch(`/reservation/api/delete/${reservationId}`, {
            method: 'PATCH'
        });

        return response.json();
    }

    const getList = async (placeId, page, callback) => {
        const response = await fetch(`/reservation/api/getList/${placeId}?page=${page}`);

        const data = await response.json();

        if (callback) {
            return callback(data);
        }

        return data;
    }

    const approveReservation = async (reservationId) => {
        const response = await fetch(`/reservation/api/approve/${reservationId}`, {
            method: 'PATCH'
        });
        return response.json();
    }

    const rejectReservation = async (reservationId) => {
        const response = await fetch(`/reservation/api/reject/${reservationId}`, {
            method: 'PATCH'
        });
        return response.json();
    }

    return {
        cancelReservation: cancelReservation,
        deleteReservation: deleteReservation,
        getList: getList,
        approveReservation: approveReservation,
        rejectReservation: rejectReservation,
    }
})();