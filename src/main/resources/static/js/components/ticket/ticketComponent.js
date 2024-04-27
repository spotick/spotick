import {ticketService} from "../../services/ticket/ticketService.js";
import {ticketLayout} from "../../layouts/ticket/ticketLayout.js";

export const sliceTicketListComponent = async (page, category, ratingType, ticketSortType, district, detailDistrict, keyword) => {
    return ticketService.getList(page, category, ratingType, ticketSortType, district, detailDistrict, keyword, ticketLayout.showTicketList);
}