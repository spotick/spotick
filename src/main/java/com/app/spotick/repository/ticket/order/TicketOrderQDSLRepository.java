package com.app.spotick.repository.ticket.order;

import com.app.spotick.api.dto.ticket.TicketOrderDto;

public interface TicketOrderQDSLRepository {

    TicketOrderDto.Info findOrderInfoById(Long orderId);

}
