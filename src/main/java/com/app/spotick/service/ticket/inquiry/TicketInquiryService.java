package com.app.spotick.service.ticket.inquiry;

import com.app.spotick.api.dto.place.InquiryResponseDto;
import com.app.spotick.domain.dto.place.inquiry.UnansweredInquiryDto;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;

public interface TicketInquiryService {

    Slice<UnansweredInquiryDto> findUnanswerdInquiriesSlice(Long ticketId, Long userId, Pageable pageable);

    void updateInquiryResponse(InquiryResponseDto inquiryResponseDto);
}
