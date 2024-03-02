package com.app.spotick.domain.entity.compositePk;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.Objects;

@Embeddable
@NoArgsConstructor
public class TicketLikeId implements Serializable {
    @Column(name = "TICKET_ID")
    private Long ticketId;
    @Column(name = "USER_ID")
    private Long userId;

    public TicketLikeId(Long ticketId, Long userId) {
        this.ticketId = ticketId;
        this.userId = userId;
    }
}
