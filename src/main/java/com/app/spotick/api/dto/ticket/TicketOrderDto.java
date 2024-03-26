package com.app.spotick.api.dto.ticket;

import com.app.spotick.api.dto.bootpay.BootpayItemDto;
import com.app.spotick.domain.type.payment.PaymentMethod;
import jakarta.validation.Valid;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class TicketOrderDto {
    @Data
    @NoArgsConstructor
    @Valid
    public static class Save {
        private Long ticketId;
        private LocalDate eventDate;
        private PaymentMethod paymentMethod;

        private List<TicketOrderDetailDto> ticketOrderDetailDtoList = new ArrayList<>();
    }

    @Data
    @NoArgsConstructor
    public static class Info {
        private Long orderId;
        private Integer amount;
        private PaymentMethod paymentMethod;

        private String ticketTitle;

        private Long userId;
        private String nickname;
        private String tel;
        private String email;

        private List<BootpayItemDto> bootpayItemDtoList = new ArrayList<>();

        public Info(Long orderId, Integer amount, PaymentMethod paymentMethod, String ticketTitle, Long userId, String nickname, String tel, String email, List<BootpayItemDto> bootpayItemDtoList) {
            this.orderId = orderId;
            this.amount = amount;
            this.paymentMethod = paymentMethod;
            this.ticketTitle = ticketTitle;
            this.userId = userId;
            this.nickname = nickname;
            this.tel = tel;
            this.email = email;
            this.bootpayItemDtoList = bootpayItemDtoList;
        }
    }
}
