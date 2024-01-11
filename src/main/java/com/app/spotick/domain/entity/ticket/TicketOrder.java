package com.app.spotick.domain.entity.ticket;

import com.app.spotick.domain.base.Period;
import com.app.spotick.domain.entity.user.User;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;

@Entity @Table(name = "TBL_TICKET_ORDER")
@SequenceGenerator(name = "SEQ_TICKET_ORDER_GENERATOR", sequenceName = "SEQ_TICKET_ORDER",allocationSize = 1)
@Getter @ToString(callSuper = true) @NoArgsConstructor(access = AccessLevel.PROTECTED)
public class TicketOrder extends Period {
    @Id @GeneratedValue(generator = "SEQ_TICKET_ORDER_GENERATOR")
    @Column(name = "TICKET_ORDER_ID")
    private Long id;
    private LocalDate eventDate;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "TICKET_EVENT_ID")
    private Ticket ticket;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "USER_ID")
    private User user;

    @Builder
    public TicketOrder(Long id, LocalDate eventDate, Ticket ticket, User user) {
        this.id = id;
        this.eventDate = eventDate;
        this.ticket = ticket;
        this.user = user;
    }
}
