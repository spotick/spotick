package com.app.spotick.service.ticket;

import com.app.spotick.domain.dto.ticket.TicketGradeDto;
import com.app.spotick.domain.dto.ticket.TicketGradeRegisterDto;
import com.app.spotick.domain.dto.ticket.TicketListDto;
import com.app.spotick.domain.dto.ticket.TicketRegisterDto;
import com.app.spotick.domain.entity.promotion.PromotionBoard;
import com.app.spotick.domain.entity.ticket.Ticket;
import com.app.spotick.domain.entity.ticket.TicketGrade;
import com.app.spotick.domain.entity.user.User;
import com.app.spotick.repository.ticket.TicketRepository;
import com.app.spotick.repository.ticket.file.TicketFileRepository;
import com.app.spotick.repository.ticket.grade.TicketGradeRepository;
import com.app.spotick.repository.user.UserRepository;
import com.app.spotick.service.ticket.file.TicketFileService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Slf4j
@Transactional
@RequiredArgsConstructor
public class TicketServiceImpl implements TicketService {
    private final TicketRepository ticketRepository;
    private final TicketGradeRepository ticketGradeRepository;
    private final TicketFileService ticketFileService;
    private final UserRepository userRepository;
    @Override
    public void registerTicket(TicketRegisterDto ticketRegisterDto, Long userId) throws IOException {
        User tmpUser = userRepository.getReferenceById(userId);

        Ticket ticketEntity = ticketRegisterDto.toEntity();
        ticketEntity.setUser(tmpUser);

        // 티켓 이벤트 db저장
        Ticket savedTicket = ticketRepository.save(ticketEntity);

        // 티켓 이벤트 파일 저장
        ticketFileService.registerAndSaveTicketFile(ticketRegisterDto.getTicketFile(), savedTicket);

        // ticketGrades를 엔티티 타입으로 변환 후 entity리스트로 변환 -> 이후 saveAll
        List<TicketGrade> ticketGradeEntities = ticketRegisterDto.getTicketGrades().stream()
                .map(dto -> {
                    TicketGrade ticketGradeEntity = dto.toEntity();
                    ticketGradeEntity.setTicket(savedTicket);
                    return ticketGradeEntity;
                })
                .toList();
        ticketGradeRepository.saveAll(ticketGradeEntities);
    }

    @Override
    public void removeTicket(Long ticketId) {
        ticketRepository.deleteById(ticketId);
    }

    @Override
    public Page<TicketListDto> findTicketList(Pageable pageable) {
        return null;
    }

    @Override
    public List<TicketGradeDto> findTicketGrades(Long ticketId, LocalDate date) {
        return ticketGradeRepository.findTicketGradesByTicketId(ticketId, date);
    }
}
