import {ticketService} from "../../sevices/ticket/ticketService.js";
import {ticketLayout} from "../../layouts/ticket/ticketLayout.js";

export const showTicketListEvent = async (page, ticketSortType, container) => {
    const html = await ticketService.getList(page, ticketSortType, ticketLayout.showTicketList);
    container.insertAdjacentHTML("beforeend", html);
}