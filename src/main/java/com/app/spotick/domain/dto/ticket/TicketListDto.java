package com.app.spotick.domain.dto.ticket;

import com.app.spotick.domain.entity.ticket.TicketFile;
import com.app.spotick.domain.type.ticket.TicketCategory;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@NoArgsConstructor
public class TicketListDto {
    private Long ticketId;
    private String ticketTitle;
    private LocalDate startDate;
    private LocalDate endDate;
    private TicketCategory ticketCategory;
    private TicketFile ticketFile;
    private double evalAvg;
    private Long evalCount;
    private Long likeCount;
    private String postAddress;

}
