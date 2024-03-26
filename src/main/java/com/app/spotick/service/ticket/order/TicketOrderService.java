package com.app.spotick.service.ticket.order;

import com.app.spotick.api.dto.ticket.TicketOrderDto;
import com.app.spotick.domain.type.payment.PaymentStatus;
import org.springframework.http.ResponseEntity;

public interface TicketOrderService {

    ResponseEntity<?> saveTicketOrder(TicketOrderDto.Save saveDto, Long userId);

    void declineOrder(Long orderId, PaymentStatus paymentStatus);

}
