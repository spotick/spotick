package com.app.spotick.domain.dto.ticket;

import com.app.spotick.domain.entity.ticket.Ticket;
import com.app.spotick.domain.entity.ticket.TicketFile;
import com.app.spotick.domain.type.ticket.TicketCategory;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
public class TicketListDto {
    private Long ticketId;
    private String ticketTitle;
    private String content;
    private TicketCategory ticketCategory;
    private List<TicketFile> ticketFiles;



    public TicketListDto(Long ticketId, String ticketTitle, String content, TicketCategory ticketCategory, List<TicketFile> ticketFiles) {
        this.ticketId = ticketId;
        this.ticketTitle = ticketTitle;
        this.content = content;
        this.ticketCategory = ticketCategory;
        this.ticketFiles = ticketFiles;
    }

    //    조회수 제외됨
    public Ticket toEntity(){
        return Ticket.builder()
                .id(ticketId)
                .title(ticketTitle)
                .content(content)
                .ticketCategory(ticketCategory)
                .build();
    }
}
