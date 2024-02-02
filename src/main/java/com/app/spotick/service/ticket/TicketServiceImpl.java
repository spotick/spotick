package com.app.spotick.service.ticket;

import com.app.spotick.domain.dto.ticket.TicketListDto;
import com.app.spotick.domain.dto.ticket.TicketRegisterDto;
import com.app.spotick.domain.entity.promotion.PromotionBoard;
import com.app.spotick.domain.entity.ticket.Ticket;
import com.app.spotick.domain.entity.user.User;
import com.app.spotick.repository.ticket.TicketRepository;
import com.app.spotick.repository.ticket.file.TicketFileRepository;
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
@Service
@Slf4j
@Transactional
@RequiredArgsConstructor
public class TicketServiceImpl implements TicketService {
    private final TicketRepository ticketRepository;
    private final TicketFileService ticketFileService;
    private final UserRepository userRepository;
    @Override
    public void registerTicket(TicketRegisterDto ticketRegisterDto, Long userId) throws IOException {
        User writer = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalStateException("존재하지 않는 회원"));
        Ticket ticket = ticketRegisterDto.toEntity();
        ticket.setUser(writer);

        ticket = ticketRepository.save(ticket);
//        사진파일 넣기
        MultipartFile ticketFile = ticketRegisterDto.getTicketFile();


        log.info("사진 업로드 전");
        ticketFileService.registerAndSaveTicketFile(ticketFile,ticket);
        log.info("사진 업로드 후");
    }

    @Override
    public void removeTicket(Long ticketId) {
        ticketRepository.deleteById(ticketId);
    }

    @Override
    public Page<TicketListDto> findTicketList(Pageable pageable) {
        return null;
    }
}
