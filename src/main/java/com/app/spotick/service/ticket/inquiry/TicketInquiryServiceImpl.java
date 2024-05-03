package com.app.spotick.service.ticket.inquiry;

import com.app.spotick.api.dto.place.InquiryResponseDto;
import com.app.spotick.api.dto.ticket.TicketInquiryDto;
import com.app.spotick.domain.dto.place.inquiry.UnansweredInquiryDto;
import com.app.spotick.domain.dto.ticket.TicketInquiryListDto;
import com.app.spotick.domain.entity.ticket.Ticket;
import com.app.spotick.domain.entity.ticket.TicketInquiry;
import com.app.spotick.domain.entity.user.User;
import com.app.spotick.domain.type.notice.NoticeType;
import com.app.spotick.repository.ticket.TicketRepository;
import com.app.spotick.repository.ticket.inquiry.TicketInquiryRepository;
import com.app.spotick.repository.user.UserRepository;
import com.app.spotick.service.notice.NoticeService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.NoSuchElementException;

@Service
@Transactional
@RequiredArgsConstructor
public class TicketInquiryServiceImpl implements TicketInquiryService {
    private final TicketInquiryRepository ticketInquiryRepository;
    private final TicketRepository ticketRepository;
    private final UserRepository userRepository;
    private final NoticeService noticeService;

    @Override
    public Page<TicketInquiryListDto> findInquiriesPage(Long userId, Pageable pageable) {
        return ticketInquiryRepository.findInquiryListByUserId(userId, pageable);
    }

    @Override
    @Transactional(readOnly = true)
    public Slice<UnansweredInquiryDto> findUnanswerdInquiriesSlice(Long ticketId, Long userId, Pageable pageable) {
        return ticketInquiryRepository.findUnansweredInquiriesSlice(ticketId, userId, pageable);
    }

    @Override
    public void updateInquiryResponse(InquiryResponseDto inquiryResponseDto) {
        Ticket tmpTicket = ticketRepository.getReferenceById(inquiryResponseDto.getPlaceId());

        TicketInquiry foundInquiry = ticketInquiryRepository.findByIdAndTicket(inquiryResponseDto.getInquiryId(), tmpTicket).orElseThrow(
                NoSuchElementException::new
        );

        noticeService.saveNotice(NoticeType.INQUIRY_RESPONSE, foundInquiry.getUser().getId());

        foundInquiry.updateResponse(inquiryResponseDto.getResponse());
    }

    @Override
    public void deleteInquiry(Long inquiryId, Long userId) {
        User tmpUser = userRepository.getReferenceById(userId);

        TicketInquiry foundInquiry = ticketInquiryRepository.findByIdAndUser(inquiryId, tmpUser).orElseThrow(
                NoSuchElementException::new
        );

        ticketInquiryRepository.delete(foundInquiry);
    }

    @Override
    public void register(TicketInquiryDto.Request inquiryReq, Long userId) {
        User tmpUser = userRepository.getReferenceById(userId);
        Ticket ticket = ticketRepository.findById(inquiryReq.getTicketId()).orElseThrow(
                NoSuchElementException::new
        );

        TicketInquiry inquiry = TicketInquiry.builder()
                .user(tmpUser)
                .ticket(ticket)
                .title(inquiryReq.getInquiryTitle())
                .content(inquiryReq.getInquiryContent())
                .build();
        ticketInquiryRepository.save(inquiry);

        noticeService.saveNotice(NoticeType.TICKET_INQUIRY_REGISTER, ticket.getUser().getId());
    }

    @Override
    public Page<TicketInquiryDto.Response> findInquiryListPageById(Long ticketId, Pageable pageable) {
        Ticket tmpTicket = ticketRepository.getReferenceById(ticketId);

        return ticketInquiryRepository.findAllByTicketOrderByIdDesc(tmpTicket, pageable)
                .map(TicketInquiryDto.Response::from);
    }
}
