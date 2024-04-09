package com.app.spotick.domain.dto.ticket;

import com.app.spotick.domain.embedded.post.PostAddress;
import com.app.spotick.domain.type.ticket.TicketCategory;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data @NoArgsConstructor
public class TicketInquiryListDto {
    private Long ticketId;
    private Long inquiryId;
    private String title;
    private PostAddress ticketAddress;
    private TicketCategory ticketCategory;
    private Integer lowestPrice;
    private LocalDate startDate;
    private LocalDate endDate;
    private String fileName;
    private String uuid;
    private String uploadPath;
    private String inquiryTitle;
    private String content;
    private String response;
    private boolean isResponded;

    public TicketInquiryListDto(Long ticketId, Long inquiryId, String title, PostAddress ticketAddress, TicketCategory ticketCategory, Integer lowestPrice, LocalDate startDate, LocalDate endDate, String fileName, String uuid, String uploadPath, String inquiryTitle, String content, String response, boolean isResponded) {
        this.ticketId = ticketId;
        this.inquiryId = inquiryId;
        this.title = title;
        this.ticketAddress = ticketAddress;
        this.ticketCategory = ticketCategory;
        this.lowestPrice = lowestPrice;
        this.startDate = startDate;
        this.endDate = endDate;
        this.fileName = fileName;
        this.uuid = uuid;
        this.uploadPath = uploadPath;
        this.inquiryTitle = inquiryTitle;
        this.content = content;
        this.response = response;
        this.isResponded = isResponded;
    }
}
