package com.app.spotick.repository.ticket;

import com.app.spotick.domain.dto.ticket.TicketManageListDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface TicketQDSLRepository {

    Page<TicketManageListDto> findHostTicketListByUserId(Long userId, Pageable pageable);
}
