package com.app.spotick.controller.dummy;

import com.app.spotick.domain.embedded.post.PostAddress;
import com.app.spotick.domain.entity.ticket.Ticket;
import com.app.spotick.domain.entity.ticket.TicketFile;
import com.app.spotick.domain.entity.ticket.TicketGrade;
import com.app.spotick.domain.entity.user.User;
import com.app.spotick.domain.type.post.PostStatus;
import com.app.spotick.domain.type.ticket.TicketCategory;
import com.app.spotick.domain.type.ticket.TicketRatingType;
import com.app.spotick.repository.ticket.TicketRepository;
import com.app.spotick.repository.ticket.file.TicketFileRepository;
import com.app.spotick.repository.ticket.grade.TicketGradeRepository;
import com.app.spotick.repository.user.UserRepository;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.UUID;

@Component
@RequiredArgsConstructor
public class TicketDummyController {
    private final UserRepository userRepository;
    private final TicketFileRepository ticketFileRepository;
    private final TicketRepository ticketRepository;
    private final TicketGradeRepository ticketGradeRepository;

    private final Random random = new Random();

    @PostConstruct
    public void createTicketDummy() {
        User user = userRepository.findById(2L).get();
        for (int i = 0; i < 70; i++) {
            TicketFile ticketFile = TicketFile.builder()
                    .uuid(UUID.randomUUID().toString())
                    .fileName("장소사진" + i)
                    .uploadPath("path" + i)
                    .build();
            ticketFile = ticketFileRepository.save(ticketFile);

            Ticket ticket = Ticket.builder()
                    .title("티켓테스트" + i)
                    .content("티켓 내용" + i)
                    .startDate(LocalDate.of(2024, 12, 25))
                    .endDate(LocalDate.of(2024, 12, 27))
                    .ticketCategory(TicketCategory.CLASSIC_DANCE)
                    .bankName("00은행")
                    .accountHolder("예금주" + i)
                    .accountNumber("123456-00-789123")
                    .ticketEventAddress(new PostAddress("서울특별시 강남구 테헤란로 " + i, "장소 상세주소" + i))
                    .lat(37.5546788388674)
                    .lng(126.970606917394)
                    .user(user)
                    .ticketFile(ticketFile)
                    .ticketRatingType(TicketRatingType.ALL)
                    .ticketEventStatus(PostStatus.APPROVED)
                    .build();
            ticket = ticketRepository.save(ticket);

            List<TicketGrade> ticketGrades = new ArrayList<>();

            for (int j = 0; j < 3; j++) {
                ticketGrades.add(
                        TicketGrade.builder()
                                .gradeName(i + "티켓등급" + j)
                                .price((random.nextInt(10) + 1) * 10000)
                                .maxPeople((j + 1) * 10)
                                .ticket(ticket)
                                .build()
                );
            }
            ticketGradeRepository.saveAll(ticketGrades);
        }
    }
}
