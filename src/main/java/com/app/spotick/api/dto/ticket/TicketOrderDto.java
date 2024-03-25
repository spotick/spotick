package com.app.spotick.api.dto.ticket;

import com.app.spotick.domain.type.payment.PaymentMethod;
import jakarta.validation.Valid;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Getter
@NoArgsConstructor
public class TicketOrderDto {
    private Save save;

    @Data
    @NoArgsConstructor
    @Valid
    public static class Save {
        private Long ticketId;
        private LocalDate eventDate;
        private PaymentMethod paymentMethod;

        private List<TicketOrderDetailDto> ticketOrderDetailDtoList = new ArrayList<>();
    }
}
