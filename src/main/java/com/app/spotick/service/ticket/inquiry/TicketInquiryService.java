package com.app.spotick.service.ticket.inquiry;

import com.app.spotick.api.dto.place.InquiryResponseDto;
import com.app.spotick.api.dto.ticket.TicketInquiryDto;
import com.app.spotick.domain.dto.place.inquiry.UnansweredInquiryDto;
import com.app.spotick.domain.dto.ticket.TicketInquiryListDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;

public interface TicketInquiryService {

    Page<TicketInquiryListDto> findInquiriesPage(Long userId, Pageable pageable);

    Slice<UnansweredInquiryDto> findUnanswerdInquiriesSlice(Long ticketId, Long userId, Pageable pageable);

    void updateInquiryResponse(InquiryResponseDto inquiryResponseDto);

    void deleteInquiry(Long inquiryId, Long userId);

    void register(TicketInquiryDto.Request inquiryReq, Long userId);

    Page<TicketInquiryDto.Response> findInquiryListPageById(Long ticketId, Pageable pageable);

}
