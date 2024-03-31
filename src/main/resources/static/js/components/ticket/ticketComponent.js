import {ticketService} from "../../sevices/ticket/ticketService.js";
import {ticketLayout} from "../../layouts/ticket/ticketLayout.js";

export const showTicketListEvent = async (page, category, ticketSortType, container) => {
    const html = await ticketService.getList(page, category, ticketSortType, ticketLayout.showTicketList);
    container.insertAdjacentHTML("beforeend", html);
}