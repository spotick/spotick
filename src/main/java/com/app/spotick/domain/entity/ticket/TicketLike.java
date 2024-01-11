package com.app.spotick.domain.entity.ticket;

import com.app.spotick.domain.base.Period;
import com.app.spotick.domain.entity.user.User;
import jakarta.persistence.*;
import lombok.*;

@Entity @Table(name = "TBL_TICKET_LIKE")
@SequenceGenerator(name = "SEQ_TICKET_LIKE_GENERATOR", sequenceName = "SEQ_TICKET_LIKE",allocationSize = 1)
@Getter @ToString(callSuper = true) @NoArgsConstructor(access = AccessLevel.PROTECTED)
public class TicketLike extends Period {
    @Id @GeneratedValue(generator = "SEQ_TICKET_LIKE_GENERATOR")
    @Column(name = "TICKET_LIKE_ID")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "USER_ID")
    private User user;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "TICKET_ID")
    private Ticket ticket;

    @Builder
    public TicketLike(Long id, User user, Ticket ticket) {
        this.id = id;
        this.user = user;
        this.ticket = ticket;
    }
}
