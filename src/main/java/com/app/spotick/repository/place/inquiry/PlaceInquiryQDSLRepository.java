package com.app.spotick.repository.place.inquiry;

import com.app.spotick.domain.dto.place.PlaceInquiryListDto;
import com.app.spotick.domain.dto.place.inquiry.UnansweredInquiryDto;
import com.app.spotick.domain.entity.place.PlaceInquiry;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;

public interface PlaceInquiryQDSLRepository {
    Page<PlaceInquiry> inquiryListWithPage(Long placeId, Pageable pageable);

    Page<PlaceInquiryListDto> findInquiriesByUserId(Long userId, Pageable pageable);

    Slice<UnansweredInquiryDto> findUnansweredInquiriesPage(Long placeId, Long userId, Pageable pageable);
}
