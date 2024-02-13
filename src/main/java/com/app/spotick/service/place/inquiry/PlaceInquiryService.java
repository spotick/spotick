package com.app.spotick.service.place.inquiry;

import com.app.spotick.api.dto.place.PlaceInquiryDto;
import com.app.spotick.api.dto.place.InquiryResponseDto;
import com.app.spotick.domain.dto.place.PlaceInquiryListDto;
import com.app.spotick.domain.dto.place.inquiry.UnansweredInquiryDto;
import com.app.spotick.domain.entity.place.PlaceInquiry;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;

import java.util.Optional;

public interface PlaceInquiryService {
    PlaceInquiryDto.Response register(PlaceInquiryDto.Request inquiryReq,Long userId);

    Page<PlaceInquiryDto.Response> inquiryListWithPage(Long placeId, Pageable pageable);

    Page<PlaceInquiryListDto> getInquiriesByUserId(Long userId, Pageable pageable);

    void deleteInquiryById(Long placeInquiryId, Long userId);

    void updateInquiryResponse(InquiryResponseDto inquiryResponseDto);

    Slice<UnansweredInquiryDto> findUnanswerdInquiriesSlice(Long placeId, Long userId, Pageable pageable);

}
