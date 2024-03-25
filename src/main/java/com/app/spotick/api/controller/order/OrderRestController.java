package com.app.spotick.api.controller.order;

import com.app.spotick.api.dto.place.PlacePaymentDto;
import com.app.spotick.api.dto.ticket.TicketOrderDto;
import com.app.spotick.domain.dto.user.UserDetailsDto;
import com.app.spotick.service.place.payment.PlacePaymentService;
import com.app.spotick.service.ticket.order.TicketOrderService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/order/api")
@RequiredArgsConstructor
public class OrderRestController {
    private final PlacePaymentService placePaymentService;
    private final TicketOrderService ticketOrderService;

    @PostMapping("/place/save")
    public ResponseEntity<?> savePlace(@Valid @RequestBody PlacePaymentDto dto,
                                       @AuthenticationPrincipal UserDetailsDto userDetailsDto) {
        return placePaymentService.savePayment(userDetailsDto.getId(), dto);
    }

    @PostMapping("/ticket/save")
    public ResponseEntity<?> saveTicket(@Valid @RequestBody TicketOrderDto dto,
                                        @AuthenticationPrincipal UserDetailsDto userDetailsDto) {
        return ticketOrderService.saveTicketOrder(dto, userDetailsDto.getId());
    }
}
