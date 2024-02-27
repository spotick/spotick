package com.app.spotick.domain.dto.ticket;

import com.app.spotick.domain.entity.ticket.TicketFile;
import com.app.spotick.domain.type.ticket.TicketCategory;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class TicketListDto {
    private Long ticketId;
    private String ticketTitle;
    private String content;
    private TicketCategory ticketCategory;
    private TicketFile ticketFiles;
    private double evalAvg;
    private Long evalCount;
    private Long bookmarkCount;

}
