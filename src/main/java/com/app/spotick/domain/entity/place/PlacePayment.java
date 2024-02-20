package com.app.spotick.domain.entity.place;

import com.app.spotick.domain.base.Period;
import com.app.spotick.domain.entity.user.User;
import com.app.spotick.domain.type.payment.PaymentMethod;
import com.app.spotick.domain.type.payment.PaymentStatus;
import jakarta.persistence.*;
import lombok.*;

@Entity @Table(name = "TBL_PLACE_PAYMENT")
@SequenceGenerator(name = "SEQ_PLACE_PAYMENT_GENERATOR", sequenceName = "SEQ_PLACE_PAYMENT",allocationSize = 1)
@Getter @ToString(callSuper = true) @NoArgsConstructor(access = AccessLevel.PROTECTED)
public class PlacePayment extends Period {
    @Id @GeneratedValue(generator = "SEQ_PLACE_PAYMENT_GENERATOR")
    @Column(name = "PLACE_PAYMENT_ID")
    private Long id;
    private Integer amount;
    @Enumerated(EnumType.STRING)
    private PaymentMethod paymentMethod;
    @Enumerated(EnumType.STRING)
    private PaymentStatus paymentStatus;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "USER_ID")
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "RESERVATION_ID")
    private PlaceReservation placeReservation;

    @Builder
    public PlacePayment(Long id, Integer amount, PaymentMethod paymentMethod, PaymentStatus paymentStatus, User user, PlaceReservation placeReservation) {
        this.id = id;
        this.amount = amount;
        this.paymentMethod = paymentMethod;
        this.paymentStatus = paymentStatus;
        this.user = user;
        this.placeReservation = placeReservation;
    }

    public void updatePaymentStatus(PaymentStatus paymentStatus) {
        this.paymentStatus = paymentStatus;
    }
}
