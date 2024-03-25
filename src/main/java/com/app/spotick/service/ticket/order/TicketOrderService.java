package com.app.spotick.service.ticket.order;

import com.app.spotick.api.dto.ticket.TicketOrderDto;
import org.springframework.http.ResponseEntity;

public interface TicketOrderService {

    ResponseEntity<?> saveTicketOrder(TicketOrderDto dto, Long userId);

}
