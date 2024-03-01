package com.app.spotick.domain.dto.ticket;

import com.app.spotick.domain.embedded.post.PostAddress;
import com.app.spotick.domain.type.ticket.TicketCategory;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.List;

@Data @NoArgsConstructor
public class TicketManageListDto {
    private Long ticketId;
    private String title;
    private PostAddress ticketAddress;
    private TicketCategory ticketCategory;
    private Integer minPrice;
    private LocalDate startDate;
    private LocalDate endDate;
    private String fileName;
    private String uuid;
    private String uploadPath;
    private Long inquiriesCount;

    public TicketManageListDto(Long ticketId, String title, PostAddress ticketAddress, TicketCategory ticketCategory, Integer minPrice, LocalDate startDate, LocalDate endDate, String fileName, String uuid, String uploadPath, Long inquiriesCount) {
        this.ticketId = ticketId;
        this.title = title;
        this.ticketAddress = ticketAddress;
        this.ticketCategory = ticketCategory;
        this.minPrice = minPrice;
        this.startDate = startDate;
        this.endDate = endDate;
        this.fileName = fileName;
        this.uuid = uuid;
        this.uploadPath = uploadPath;
        this.inquiriesCount = inquiriesCount;
    }
}
