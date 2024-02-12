package com.app.spotick.service.ticket;

import com.app.spotick.domain.dto.promotion.PromotionDetailDto;
import com.app.spotick.domain.dto.promotion.PromotionListDto;
import com.app.spotick.domain.dto.ticket.TicketGradeDto;
import com.app.spotick.domain.dto.ticket.TicketListDto;
import com.app.spotick.domain.dto.ticket.TicketRegisterDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.io.IOException;
import java.time.LocalDate;
import java.util.List;

public interface TicketService {

    void registerTicket(TicketRegisterDto ticketRegisterDto, Long userId) throws IOException;

//    void modifyTicket(TicketModifyDto ticketModifyDto);

    void removeTicket(Long ticketId);

//    TicketDetailDto ticketDetail(Long ticketId);

    Page<TicketListDto> findTicketList(Pageable pageable);

    List<TicketGradeDto> findTicketGrades(Long ticketId, LocalDate date);
}
