package com.app.spotick.domain.entity.ticket;

import com.app.spotick.domain.base.Period;
import jakarta.persistence.*;
import lombok.*;

import java.util.HashMap;
import java.util.Map;

@Entity @Table(name = "TBL_TICKET_GRADE")
@SequenceGenerator(name = "SEQ_TICKET_GRADE_GENERATOR", sequenceName = "SEQ_TICKET_GRADE",allocationSize = 1)
@Getter @ToString(callSuper = true) @NoArgsConstructor(access = AccessLevel.PROTECTED)
public class TicketGrade extends Period {
    @Id @GeneratedValue(generator = "SEQ_TICKET_GRADE_GENERATOR")
    @Column(name = "TICKET_GRADE_ID")
    private Long id;
    private String gradeName; // 등급명
    private Integer price;  // 티켓 가격
    private Integer maxPeople; // 등급별 최대 인원

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "TICKET_EVENT_ID")
    private Ticket ticket;

    @Builder
    public TicketGrade(Long id, String gradeName, Integer price, Integer maxPeople, Ticket ticket) {
        this.id = id;
        this.gradeName = gradeName;
        this.price = price;
        this.maxPeople = maxPeople;
        this.ticket = ticket;
    }

//    private static Map<Integer, Integer> createTicketInfo(Integer price, Integer maxPeople) {
//        Map<Integer, Integer> ticketDetails = new HashMap<>();
//        ticketDetails.put(price, maxPeople);
//        return ticketDetails;
//    }
}









