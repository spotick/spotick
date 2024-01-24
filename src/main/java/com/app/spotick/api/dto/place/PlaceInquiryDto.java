package com.app.spotick.api.dto.place;

import com.app.spotick.domain.entity.place.PlaceInquiry;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.format.DateTimeFormatter;

public class PlaceInquiryDto {

    @Data
    @NoArgsConstructor
    public static class Request {
        private Long placeId;
        private String inquiryTitle;
        private String inquiryContent;
    }


    @Data
    @NoArgsConstructor
    public static class Response {
        private Long placeId;
        private String inquiryTitle;
        private String inquiryContent;
        private Long questionerId;
        private String questionerNickname;
        private String questionDate;
        private String inquiryResponse;
        private String inquiryReplyDate;

        public Response(Long placeId, String inquiryTitle, String inquiryContent, Long questionerId, String questionerNickname, String questionDate, String inquiryResponse, String inquiryReplyDate) {
            this.placeId = placeId;
            this.inquiryTitle = inquiryTitle;
            this.inquiryContent = inquiryContent;
            this.questionerId = questionerId;
            this.questionerNickname = questionerNickname;
            this.questionDate = questionDate;
            this.inquiryResponse = inquiryResponse;
            this.inquiryReplyDate = inquiryReplyDate;
        }

        public static Response from(PlaceInquiry placeInquiry) {
            Response resp = new Response();
            resp.setPlaceId(placeInquiry.getPlace().getId());
            resp.setInquiryTitle(placeInquiry.getTitle());
            resp.setInquiryContent(placeInquiry.getContent());
            resp.setQuestionDate(placeInquiry.getCreatedDate()
                    .format(DateTimeFormatter.ofPattern("yyyy-MM-dd")));
            resp.setInquiryResponse(placeInquiry.getResponse());
            resp.setInquiryReplyDate(placeInquiry.getModifiedDate()
                    .format(DateTimeFormatter.ofPattern("yyyy-MM-dd")));
            if (placeInquiry.getUser() != null) {
                resp.setQuestionerId(placeInquiry.getUser().getId());
                resp.setQuestionerNickname(placeInquiry.getUser().getNickName());
            }

            return resp;
        }


    }


}
