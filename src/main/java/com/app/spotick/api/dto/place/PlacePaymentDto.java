package com.app.spotick.api.dto.place;

import com.app.spotick.domain.type.payment.PaymentMethod;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import lombok.*;

@NoArgsConstructor
@Getter
public class PlacePaymentDto {
    private Save save;

    @Data
    @NoArgsConstructor
    @Valid
    public static class Save {
        @NotNull(message = "예약번호를 찾을 수 없습니다.")
        private Long reservationId;
        @NotNull(message = "결제 방식을 찾을 수 없습니다.")
        private PaymentMethod paymentMethod;
    }
}
