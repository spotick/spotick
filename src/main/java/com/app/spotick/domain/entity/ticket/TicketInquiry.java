package com.app.spotick.domain.entity.ticket;


import com.app.spotick.domain.base.Period;
import com.app.spotick.domain.base.post.InquiryBase;
import com.app.spotick.domain.entity.user.User;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "TBL_TICKET_INQUIRY")
@SequenceGenerator(name = "SEQ_TICKET_INQUIRY_GENERATOR", sequenceName = "SEQ_TICKET_INQUIRY",allocationSize = 1)
@Getter
@ToString(callSuper = true) @NoArgsConstructor(access = AccessLevel.PROTECTED)
public class TicketInquiry extends InquiryBase {
    @Id @GeneratedValue(generator = "SEQ_TICKET_INQUIRY_GENERATOR")
    @Column(name = "TICKET_INQUIRY_ID")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "TICKET_ID")
    private Ticket ticket;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "USER_ID")
    private User user;

    @Builder
    public TicketInquiry(String title, String content, String response, Long id, Ticket ticket, User user) {
        super(title, content, response);
        this.id = id;
        this.ticket = ticket;
        this.user = user;
    }
}
