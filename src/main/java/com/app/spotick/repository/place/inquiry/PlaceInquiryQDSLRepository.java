package com.app.spotick.repository.place.inquiry;

import com.app.spotick.domain.dto.place.PlaceInquiryListDto;
import com.app.spotick.domain.dto.place.inquiry.InquiryUnansweredDto;
import com.app.spotick.domain.entity.place.PlaceInquiry;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface PlaceInquiryQDSLRepository {
    Page<PlaceInquiry> inquiryListWithPage(Long placeId, Pageable pageable);

    Page<PlaceInquiryListDto> findInquiriesByUserId(Long userId, Pageable pageable);

    Page<InquiryUnansweredDto> findUnansweredInquiriesPage(Long placeId, Long userId, Pageable pageable);
}
