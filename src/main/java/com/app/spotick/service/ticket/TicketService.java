package com.app.spotick.service.ticket;

import com.app.spotick.domain.dto.promotion.PromotionDetailDto;
import com.app.spotick.domain.dto.promotion.PromotionListDto;
import com.app.spotick.domain.dto.ticket.TicketListDto;
import com.app.spotick.domain.dto.ticket.TicketRegisterDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.io.IOException;

public interface TicketService {

    void registerTicket(TicketRegisterDto ticketRegisterDto, Long userId) throws IOException;

//    void modifyTicket(TicketModifyDto ticketModifyDto);

    void removeTicket(Long ticketId);

//    TicketDetailDto ticketDetail(Long ticketId);

    Page<TicketListDto> findTicketList(Pageable pageable);
}
