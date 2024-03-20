package com.app.spotick.domain.entity.ticket;

import com.app.spotick.domain.base.Period;
import com.app.spotick.domain.type.post.PostModifyStatus;
import jakarta.persistence.*;
import lombok.*;

@Entity @Table(name = "TBL_TICKET_MODIFY_REQUEST")
@SequenceGenerator(name = "SEQ_TICKET_MODIFY_REQUEST_GENERATOR", sequenceName = "SEQ_TICKET_MODIFY_REQUEST",allocationSize = 1)
@Getter @ToString(callSuper = true) @NoArgsConstructor(access = AccessLevel.PROTECTED)
public class TicketModifyRequest extends Period {
    @Id @GeneratedValue(generator = "SEQ_TICKET_MODIFY_REQUEST_GENERATOR")
    @Column(name = "TICKET_MODIFY_REQUEST_ID")
    private Long id;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "ORIGINAL_TICKET_ID")
    private Ticket originalTicket;    //수정 요청된 원본 장소
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "CHANGED_TICKET_ID")
    private Ticket changedTicket;     //승인 후 변경될 장소아이디
    @Enumerated(EnumType.STRING)
    private PostModifyStatus ticketModifyStatus;

    @Builder
    public TicketModifyRequest(Long id, Ticket originalTicket, Ticket changedTicket, PostModifyStatus ticketModifyStatus) {
        this.id = id;
        this.originalTicket = originalTicket;
        this.changedTicket = changedTicket;
        this.ticketModifyStatus = ticketModifyStatus;
    }

    public void setTicketModifyStatus(PostModifyStatus ticketModifyStatus){
        this.ticketModifyStatus = ticketModifyStatus;
    }
}


