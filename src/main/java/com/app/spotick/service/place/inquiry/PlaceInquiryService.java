package com.app.spotick.service.place.inquiry;

import com.app.spotick.api.dto.place.PlaceInquiryDto;
import com.app.spotick.domain.dto.place.PlaceInquiryListDto;
import com.app.spotick.domain.entity.place.PlaceInquiry;
import com.app.spotick.domain.entity.user.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;

public interface PlaceInquiryService {
    PlaceInquiryDto.Response register(PlaceInquiryDto.Request inquiryReq,Long userId);

    Page<PlaceInquiryDto.Response> inquiryListWithPage(Long placeId, Pageable pageable);

    Page<PlaceInquiryListDto> getInquiriesByUserId(Long userId, Pageable pageable);

    Optional<PlaceInquiry> findInquiryByIdAndUser(Long placeInquiryId, Long userId);

    void deleteInquiryById(Long placeInquiryId);

}
