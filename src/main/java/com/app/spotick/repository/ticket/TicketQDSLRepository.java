package com.app.spotick.repository.ticket;

import com.app.spotick.domain.dto.ticket.TicketInfoDto;
import com.app.spotick.domain.dto.ticket.TicketManageListDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Optional;

public interface TicketQDSLRepository {

    Page<TicketManageListDto> findHostTicketListByUserId(Long userId, Pageable pageable);

    Optional<TicketInfoDto> findTicketInfoByTicketId(Long ticketId, Long userId);
}
