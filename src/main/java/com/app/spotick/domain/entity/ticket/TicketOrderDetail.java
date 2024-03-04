package com.app.spotick.domain.entity.ticket;

import com.app.spotick.domain.base.Period;
import jakarta.persistence.*;
import lombok.*;

@Entity @Table(name = "TBL_TICKET_ORDER_DETAIL")
@SequenceGenerator(name = "SEQ_TICKET_ORDER_DETAIL_GENERATOR", sequenceName = "SEQ_TICKET_ORDER_DETAIL",allocationSize = 1)
@Getter @ToString(callSuper = true) @NoArgsConstructor(access = AccessLevel.PROTECTED)
public class TicketOrderDetail extends Period {
    @Id @GeneratedValue(generator = "SEQ_TICKET_ORDER_DETAIL_GENERATOR")
    @Column(name = "TICKET_ORDER_DETAIL_ID")
    private Long id;
    private int quantity;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "TICKET_ORDER_ID")
    private TicketOrder ticketOrder;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "TICKET_GRADE_ID")
    private TicketGrade ticketGrade;

    @Builder
    public TicketOrderDetail(Long id, int quantity, TicketOrder ticketOrder, TicketGrade ticketGrade) {
        this.id = id;
        this.quantity = quantity;
        this.ticketOrder = ticketOrder;
        this.ticketGrade = ticketGrade;
    }
}
