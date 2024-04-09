package com.app.spotick.domain.dto.ticket;

import com.app.spotick.domain.dto.ticket.grade.TicketGradeSaleInfoDto;
import com.app.spotick.domain.embedded.post.PostAddress;
import com.app.spotick.domain.type.ticket.TicketCategory;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.List;

@Data @NoArgsConstructor
public class TicketInfoDto {
    private Long ticketId;
    private String title;
    private PostAddress ticketAddress;
    private TicketCategory ticketCategory;
    private LocalDate startDate;
    private LocalDate endDate;

    public TicketInfoDto(Long ticketId, String title, PostAddress ticketAddress, TicketCategory ticketCategory, LocalDate startDate, LocalDate endDate) {
        this.ticketId = ticketId;
        this.title = title;
        this.ticketAddress = ticketAddress;
        this.ticketCategory = ticketCategory;
        this.startDate = startDate;
        this.endDate = endDate;
    }
}
