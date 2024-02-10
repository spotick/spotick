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
    private LocalDate startDate;
    private LocalDate endDate;
    private TicketFileDto ticketFileDto;
    private List<TicketGradeDto> ticketGradeDtos;
    private Long inquiriesCount;

    public TicketManageListDto(Long ticketId, String title, PostAddress ticketAddress, TicketCategory ticketCategory, LocalDate startDate, LocalDate endDate, TicketFileDto ticketFileDto, Long inquiriesCount) {
        this.ticketId = ticketId;
        this.title = title;
        this.ticketAddress = ticketAddress;
        this.ticketCategory = ticketCategory;
        this.startDate = startDate;
        this.endDate = endDate;
        this.ticketFileDto = ticketFileDto;
        this.inquiriesCount = inquiriesCount;
    }

    public void setTicketGrades(List<TicketGradeDto> ticketGradeDtos) {
        this.ticketGradeDtos = ticketGradeDtos;
    }
}
