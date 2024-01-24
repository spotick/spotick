package com.app.spotick.service.place.inquiry;

import com.app.spotick.api.dto.place.PlaceInquiryDto;

public interface PlaceInquiryService {
    PlaceInquiryDto.Response register(PlaceInquiryDto.Request inquiryReq,Long userId);
}
