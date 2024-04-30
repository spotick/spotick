export const reservationService = (() => {

    const cancelReservation = async (reservationId) => {
        const response = await fetch(`/reservation/api/cancel/${reservationId}`, {
            method: 'PATCH'
        });

        return await response.json();
    }

    const deleteReservation = async (reservationId) => {
        const response = await fetch(`/reservation/api/delete/${reservationId}`, {
            method: 'PATCH'
        });

        return await response.json();
    }

    return {
        cancelReservation: cancelReservation,
        deleteReservation: deleteReservation,
    }
})();