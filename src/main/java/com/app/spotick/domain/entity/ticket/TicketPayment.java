package com.app.spotick.domain.entity.ticket;

import com.app.spotick.domain.base.Period;
import com.app.spotick.domain.entity.user.User;
import com.app.spotick.domain.type.payment.PaymentMethod;
import com.app.spotick.domain.type.payment.PaymentStatus;
import jakarta.persistence.*;
import lombok.*;

@Deprecated
@Entity @Table(name = "TBL_TICKET_PAYMENT")
@SequenceGenerator(name = "SEQ_TICKET_PAYMENT_GENERATOR", sequenceName = "SEQ_TICKET_PAYMENT",allocationSize = 1)
@Getter @ToString(callSuper = true) @NoArgsConstructor(access = AccessLevel.PROTECTED)
public class TicketPayment extends Period {
    @Id @GeneratedValue(generator = "SEQ_TICKET_PAYMENT_GENERATOR")
    @Column(name = "TICKET_PAYMENT_ID")
    private Long id;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "TICKET_ORDER_ID")
    private TicketOrder ticketOrder;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "USER_ID")
    private User user;
    private Integer amount;
    @Enumerated(EnumType.STRING)
    private PaymentMethod paymentMethod;
    @Enumerated(EnumType.STRING)
    private PaymentStatus paymentStatus;

    @Builder
    public TicketPayment(Long id, TicketOrder ticketOrder, User user, Integer amount, PaymentMethod paymentMethod, PaymentStatus paymentStatus) {
        this.id = id;
        this.ticketOrder = ticketOrder;
        this.user = user;
        this.amount = amount;
        this.paymentMethod = paymentMethod;
        this.paymentStatus = paymentStatus;
    }
}
