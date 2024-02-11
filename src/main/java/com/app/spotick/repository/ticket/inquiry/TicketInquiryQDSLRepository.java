package com.app.spotick.repository.ticket.inquiry;

import com.app.spotick.domain.dto.place.inquiry.UnansweredInquiryDto;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;

public interface TicketInquiryQDSLRepository {

    Slice<UnansweredInquiryDto> findUnansweredInquiriesSlice(Long ticketId, Long userId, Pageable pageable);

}
