import {ticketService} from "../../sevices/ticket/ticketService.js";
import {ticketLayout} from "../../layouts/ticket/ticketLayout.js";

export const showTicketListEvent = async (page, category, ratingType, ticketSortType, district, detailDistrict, container) => {
    const html = await ticketService.getList(page, category, ratingType, ticketSortType, district, detailDistrict, ticketLayout.showTicketList);
    container.insertAdjacentHTML("beforeend", html);
}