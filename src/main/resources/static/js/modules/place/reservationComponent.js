import {reservationService} from "../../services/reservation/reservationService.js";
import {reservationLayouts} from "../../layouts/reservation/reservationLayouts.js";

const sliceReservationListComponent = async (placeId, page) => {
    return reservationService.getList(placeId, page, reservationLayouts.reservationListLayout);
}

export {sliceReservationListComponent}