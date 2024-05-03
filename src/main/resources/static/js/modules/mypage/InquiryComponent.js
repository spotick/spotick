import {inquiryService} from "../../services/inquiry/inquiryService.js";
import {inquiryLayouts} from "../../layouts/mypage/inquiryLayouts.js";
import {paginationLayout} from "../../layouts/pagination/paginationLayout.js";

const placeInquiriesPaginationComponent = async (page) => {
    const {data, pagination} = await inquiryService.getPlaceInquiriesOfUser(page);
    const inquiries = data.content;

    const contentHtml = inquiryLayouts.placeInquiryListLayout(inquiries);
    const paginationHtml = paginationLayout(pagination);

    return {inquiries, contentHtml, paginationHtml};
}

const ticketInquiriesPaginationComponent = async (page) => {
    const {data, pagination} = await inquiryService.getTicketInquiriesOfUser(page);
    const inquiries = data.content;

    const contentHtml = inquiryLayouts.ticketInquiryListLayout(inquiries);
    const paginationHtml = paginationLayout(pagination);

    return {inquiries, contentHtml, paginationHtml};
}

export {placeInquiriesPaginationComponent, ticketInquiriesPaginationComponent}