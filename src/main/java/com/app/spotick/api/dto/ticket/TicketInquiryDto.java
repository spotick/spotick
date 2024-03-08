package com.app.spotick.api.dto.ticket;

import com.app.spotick.domain.entity.place.PlaceInquiry;
import com.app.spotick.domain.entity.ticket.TicketInquiry;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.format.DateTimeFormatter;

public class TicketInquiryDto {

    @Data
    @NoArgsConstructor
    public static class Request {
        private Long ticketId;
        private String inquiryTitle;
        private String inquiryContent;
    }


    @Data
    @NoArgsConstructor
    public static class Response {
        private Long ticketId;
        private String inquiryTitle;
        private String inquiryContent;
        private Long questionerId;
        private String questionerNickname;
        private String questionDate;
        private String inquiryResponse;
        private String inquiryReplyDate;

        public Response(Long ticketId, String inquiryTitle, String inquiryContent, Long questionerId, String questionerNickname, String questionDate, String inquiryResponse, String inquiryReplyDate) {
            this.ticketId = ticketId;
            this.inquiryTitle = inquiryTitle;
            this.inquiryContent = inquiryContent;
            this.questionerId = questionerId;
            this.questionerNickname = questionerNickname;
            this.questionDate = questionDate;
            this.inquiryResponse = inquiryResponse;
            this.inquiryReplyDate = inquiryReplyDate;
        }

        public static Response from(TicketInquiry ticketInquiry) {
            Response resp = new Response();
            resp.setTicketId(ticketInquiry.getTicket().getId());
            resp.setInquiryTitle(ticketInquiry.getTitle());
            resp.setInquiryContent(ticketInquiry.getContent());
            resp.setQuestionDate(ticketInquiry.getCreatedDate()
                    .format(DateTimeFormatter.ofPattern("yyyy-MM-dd")));
            resp.setInquiryResponse(ticketInquiry.getResponse());
            resp.setInquiryReplyDate(ticketInquiry.getModifiedDate()
                    .format(DateTimeFormatter.ofPattern("yyyy-MM-dd")));
            if (ticketInquiry.getUser() != null) {
                resp.setQuestionerId(ticketInquiry.getUser().getId());
                resp.setQuestionerNickname(ticketInquiry.getUser().getNickName());
            }

            return resp;
        }
    }
}
