package com.app.spotick.service.place.inquiry;

import com.app.spotick.api.dto.place.PlaceInquiryDto;
import com.app.spotick.domain.entity.place.PlaceInquiry;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface PlaceInquiryService {
    PlaceInquiryDto.Response register(PlaceInquiryDto.Request inquiryReq,Long userId);

    Page<PlaceInquiryDto.Response> inquiryListWithPage(Long placeId, Pageable pageable);



}
