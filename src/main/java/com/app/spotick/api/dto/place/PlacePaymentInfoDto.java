package com.app.spotick.api.dto.place;

import com.app.spotick.domain.type.payment.PaymentMethod;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
public class PlacePaymentInfoDto {
    private Long placePaymentId;
    private Integer amount;
    private PaymentMethod paymentMethod;

    private Long reservationId;
    private String placeTitle;
    private LocalDateTime checkIn;
    private LocalDateTime checkOut;

    private Long userId;
    private String nickname;
    private String tel;
    private String email;

    public PlacePaymentInfoDto(Long placePaymentId, Integer amount, PaymentMethod paymentMethod, Long reservationId, String placeTitle, LocalDateTime checkIn, LocalDateTime checkOut, Long userId, String nickname, String tel, String email) {
        this.placePaymentId = placePaymentId;
        this.amount = amount;
        this.paymentMethod = paymentMethod;
        this.reservationId = reservationId;
        this.placeTitle = placeTitle;
        this.checkIn = checkIn;
        this.checkOut = checkOut;
        this.userId = userId;
        this.nickname = nickname;
        this.tel = tel;
        this.email = email;
    }
}
