package com.app.spotick.repository.ticket.inquiry;

import com.app.spotick.domain.dto.place.inquiry.UnansweredInquiryDto;
import com.app.spotick.domain.dto.ticket.TicketInquiryListDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;

public interface TicketInquiryQDSLRepository {

    Page<TicketInquiryListDto> findInquiryListByUserId(Long userId, Pageable pageable);

    Slice<UnansweredInquiryDto> findUnansweredInquiriesSlice(Long ticketId, Long userId, Pageable pageable);

}
