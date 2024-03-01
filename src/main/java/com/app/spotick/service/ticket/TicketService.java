package com.app.spotick.service.ticket;

import com.app.spotick.domain.dto.ticket.TicketGradeDto;
import com.app.spotick.domain.dto.ticket.TicketListDto;
import com.app.spotick.domain.dto.ticket.TicketRegisterDto;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;

import java.io.IOException;
import java.time.LocalDate;
import java.util.List;

public interface TicketService {

    void registerTicket(TicketRegisterDto ticketRegisterDto, Long userId) throws IOException;

    Slice<TicketListDto> findTicketListPage(Pageable pageable, Long userId);

    List<TicketGradeDto> findTicketGrades(Long ticketId, LocalDate date);
}
