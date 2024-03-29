package com.app.spotick.domain.entity.ticket;


import com.app.spotick.domain.base.post.PostBase;
import com.app.spotick.domain.dto.ticket.TicketEditDto;
import com.app.spotick.domain.embedded.post.PostAddress;
import com.app.spotick.domain.entity.user.User;
import com.app.spotick.domain.type.post.PostStatus;
import com.app.spotick.domain.type.ticket.TicketCategory;
import com.app.spotick.domain.type.ticket.TicketRatingType;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Entity @Table(name = "TBL_TICKET_EVENT")
@SequenceGenerator(name = "SEQ_TICKET_EVENT_GENERATOR", sequenceName = "SEQ_TICKET_EVENT",allocationSize = 1)
@Getter @ToString(callSuper = true) @NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Ticket extends PostBase {
    @Id @GeneratedValue(generator = "SEQ_TICKET_EVENT_GENERATOR")
    @Column(name = "TICKET_EVENT_ID")
    private Long id;
    @Column(length = 2000)
    private String content;
    private LocalDate startDate;
    private LocalDate endDate;
    @Enumerated(EnumType.STRING)
    private TicketCategory ticketCategory;
    private String bankName;
    private String accountNumber; //계좌번호
    private String accountHolder; //예금주

    @Enumerated(EnumType.STRING)
    private PostAddress ticketEventAddress;
    @Enumerated(EnumType.STRING)
    private TicketRatingType ticketRatingType;
    @Enumerated(EnumType.STRING)
    private PostStatus ticketEventStatus;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "USER_ID")
    private User user;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "TICKET_FILE_ID")
    private TicketFile ticketFile;

    @OneToMany(mappedBy = "ticket", fetch = FetchType.LAZY)
    private List<TicketGrade> ticketGrades = new ArrayList<>();

    @Builder
    public Ticket(String title, int viewCount, Double lat, Double lng, Long id, String content, LocalDate startDate, LocalDate endDate, TicketCategory ticketCategory, String bankName, String accountNumber, String accountHolder, PostAddress ticketEventAddress, TicketRatingType ticketRatingType, PostStatus ticketEventStatus, User user, TicketFile ticketFile) {
        super(title, viewCount, lat, lng);
        this.id = id;
        this.content = content;
        this.startDate = startDate;
        this.endDate = endDate;
        this.ticketCategory = ticketCategory;
        this.bankName = bankName;
        this.accountNumber = accountNumber;
        this.accountHolder = accountHolder;
        this.ticketEventAddress = ticketEventAddress;
        this.ticketRatingType = ticketRatingType;
        this.ticketEventStatus = ticketEventStatus;
        this.user = user;
        this.ticketFile = ticketFile;
    }

    public void setUser(User user){
        this.user = user;
    }

    public void setStatus(PostStatus ticketEventStatus) {
        this.ticketEventStatus = ticketEventStatus;
    }

    public void setFile(TicketFile ticketFile) {
        this.ticketFile = ticketFile;
    }

    public void setTicketEventStatus(PostStatus ticketEventStatus){
        this.ticketEventStatus = ticketEventStatus;
    }

    @Deprecated
    public void updateTicket(TicketEditDto ticketEditDto) {
        editBase(ticketEditDto.getTitle(), ticketEditDto.getPlaceLat(), ticketEditDto.getPlaceLng());
        this.content = ticketEditDto.getContent();
        this.ticketCategory = ticketEditDto.getCategory();
        this.bankName = ticketEditDto.getBankName();
        this.accountNumber = ticketEditDto.getAccountNumber();
        this.accountHolder = ticketEditDto.getAccountHolder();
        this.ticketEventAddress = new PostAddress(ticketEditDto.getPlaceAddress(), ticketEditDto.getPlaceAddressDetail());
        this.ticketRatingType = ticketEditDto.getTicketRatingType();
        this.ticketEventStatus = PostStatus.MODIFICATION_REQUESTED;
    }

    public void updateStatus(PostStatus ticketEventStatus) {
        this.ticketEventStatus = ticketEventStatus;
    }

    public void replaceTicket(Ticket updatedTicket){
        editBase(updatedTicket.getTitle(), updatedTicket.getLat(), updatedTicket.getLng());
        this.content = updatedTicket.getContent();
        this.ticketCategory = updatedTicket.getTicketCategory();
        this.bankName = updatedTicket.getBankName();
        this.accountNumber = updatedTicket.getAccountNumber();
        this.accountHolder = updatedTicket.getAccountHolder();
        this.ticketEventAddress = new PostAddress(updatedTicket.getTicketEventAddress().getAddress(), updatedTicket.getTicketEventAddress().getAddressDetail());
        this.ticketRatingType = updatedTicket.getTicketRatingType();
        setViewCount(updatedTicket.getViewCount());
        this.ticketFile = updatedTicket.getTicketFile();
    }
}











