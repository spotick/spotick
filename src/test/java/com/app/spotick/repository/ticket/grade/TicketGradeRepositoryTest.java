package com.app.spotick.repository.ticket.grade;

import com.app.spotick.domain.entity.ticket.Ticket;
import com.app.spotick.domain.entity.ticket.TicketGrade;
import com.app.spotick.domain.entity.user.User;
import com.app.spotick.domain.type.ticket.TicketCategory;
import com.app.spotick.domain.type.user.UserStatus;
import com.app.spotick.repository.ticket.TicketRepository;
import com.app.spotick.repository.user.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Commit;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Transactional
@Commit
class TicketGradeRepositoryTest {

    @Autowired
    private UserRepository userRepository;
    @Autowired
    private TicketRepository ticketRepository;
    @Autowired
    private TicketGradeRepository ticketGradeRepository;

    User user;
    Ticket ticket;
    Ticket ticket2;
    List<TicketGrade> ticketGradeList = new ArrayList<>();

    @BeforeEach
    void setUp() {
        user = User.builder()
                .id(1L)
                .email("bbb")
                .password("1234")
                .nickName("컨텐츠프로바이더")
                .tel("01022222222")
                .userStatus(UserStatus.ACTIVATE)
                .build();
        userRepository.save(user);

        ticket = Ticket.builder()
                .id(1L)
                .content("테스트용")
                .startDate(LocalDate.of(2024, 4, 5))
                .endDate(LocalDate.of(2024, 4, 6))
                .ticketCategory(TicketCategory.CLASSIC_DANCE)
                .bankName("테스트")
                .user(user)
                .build();
        ticketRepository.save(ticket);

        for (int i = 0; i < 4; i++) {
            TicketGrade ticketGrade = TicketGrade.builder()
                    .id((long) i + 1)
                    .gradeName("등급" + i)
                    .price(10000 * (i + 1))
                    .maxPeople(i + 10)
                    .ticket(ticket)
                    .build();
            ticketGradeList.add(ticketGrade);
        }
        ticketGradeRepository.saveAll(ticketGradeList);

        ticket2 = Ticket.builder()
                .id(2L)
                .content("테스트용2")
                .startDate(LocalDate.of(2024, 4, 5))
                .endDate(LocalDate.of(2024, 4, 6))
                .ticketCategory(TicketCategory.CLASSIC_DANCE)
                .bankName("테스트2")
                .user(user)
                .build();
        ticketRepository.save(ticket2);

        for (int i = 4; i < 8; i++) {
            TicketGrade ticketGrade = TicketGrade.builder()
                    .id((long) i + 1)
                    .gradeName("등급" + i)
                    .price(10000 * (i + 1))
                    .maxPeople(i + 10)
                    .ticket(ticket2)
                    .build();
            ticketGradeList.add(ticketGrade);
        }
        ticketGradeRepository.saveAll(ticketGradeList);
    }

    @Test
    void setupTest() {

    }

    @Test
    void findTicketGradePriceByTicketIdAndGradeIds() {
        Long ticketId = 1L;
        List<Long> gradeIds = List.of(1L, 3L, 5L, 2L);

        List<Integer> result = ticketGradeRepository.findTicketGradePriceByTicketIdAndGradeIds(ticketId, gradeIds);

        System.out.println("result = " + result);
    }
}