package com.app.spotick.domain.entity.ticket;


import com.app.spotick.domain.base.Period;
import com.app.spotick.domain.entity.user.User;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "TBL_TICKET_INQUIRY")
@SequenceGenerator(name = "SEQ_TICKET_INQUIRY_GENERATOR", sequenceName = "SEQ_TICKET_INQUIRY",allocationSize = 1)
@Getter
@ToString(callSuper = true) @NoArgsConstructor(access = AccessLevel.PROTECTED)
public class TicketInquiry extends Period {
    @Id @GeneratedValue(generator = "SEQ_TICKET_INQUIRY_GENERATOR")
    @Column(name = "TICKET_INQUIRY_ID")
    private Long id;
    String title;
    String content;
    String response;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "TICKET_ID")
    Ticket ticket;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "USER_ID")
    User user;

    @Builder
    public TicketInquiry(Long id, String title, String content, String response, Ticket ticket, User user) {
        this.id = id;
        this.title = title;
        this.content = content;
        this.response = response;
        this.ticket = ticket;
        this.user = user;
    }
}
