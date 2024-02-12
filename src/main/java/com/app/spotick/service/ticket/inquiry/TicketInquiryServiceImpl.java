package com.app.spotick.service.ticket.inquiry;

import com.app.spotick.api.dto.place.InquiryResponseDto;
import com.app.spotick.domain.dto.place.inquiry.UnansweredInquiryDto;
import com.app.spotick.domain.entity.ticket.Ticket;
import com.app.spotick.domain.entity.ticket.TicketInquiry;
import com.app.spotick.repository.ticket.TicketRepository;
import com.app.spotick.repository.ticket.inquiry.TicketInquiryRepository;
import lombok.RequiredArgsConstructor;
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

    @Override
    @Transactional(readOnly = true)
    public Slice<UnansweredInquiryDto> findUnanswerdInquiriesSlice(Long ticketId, Long userId, Pageable pageable) {
        return ticketInquiryRepository.findUnansweredInquiriesSlice(ticketId, userId, pageable);
    }

    @Override
    public void updateInquiryResponse(InquiryResponseDto inquiryResponseDto) {
        Ticket tmpTicket = ticketRepository.getReferenceById(inquiryResponseDto.getId());

        TicketInquiry foundInquiry = ticketInquiryRepository.findByIdAndTicket(inquiryResponseDto.getInquiryId(), tmpTicket).orElseThrow(
                NoSuchElementException::new
        );

        foundInquiry.updateResponse(inquiryResponseDto.getResponse());
    }
}
