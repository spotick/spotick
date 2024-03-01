package com.app.spotick.service.place.inquiry;

import com.app.spotick.api.dto.place.PlaceInquiryDto;
import com.app.spotick.api.dto.place.InquiryResponseDto;
import com.app.spotick.domain.dto.place.PlaceInquiryListDto;
import com.app.spotick.domain.dto.place.inquiry.UnansweredInquiryDto;
import com.app.spotick.domain.entity.place.Place;
import com.app.spotick.domain.entity.place.PlaceInquiry;
import com.app.spotick.domain.entity.user.User;
import com.app.spotick.domain.type.notice.NoticeType;
import com.app.spotick.repository.place.PlaceRepository;
import com.app.spotick.repository.place.inquiry.PlaceInquiryRepository;
import com.app.spotick.repository.user.UserRepository;
import com.app.spotick.service.notice.NoticeService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.NoSuchElementException;
import java.util.Optional;

@Service
@Transactional
@RequiredArgsConstructor
public class PlaceInquiryServiceImpl implements PlaceInquiryService {
    private final PlaceInquiryRepository inquiryRepository;
    private final UserRepository userRepository;
    private final PlaceRepository placeRepository;
    private final NoticeService noticeService;
    @Override
    public PlaceInquiryDto.Response register(PlaceInquiryDto.Request inquiryReq,Long userId) {
        User userProxy = userRepository.getReferenceById(userId);
        Place place = placeRepository.findById(inquiryReq.getPlaceId())
                .orElseThrow(IllegalStateException::new);

        PlaceInquiry placeInquiry = PlaceInquiry.builder()
                .user(userProxy)
                .place(place)
                .title(inquiryReq.getInquiryTitle())
                .content(inquiryReq.getInquiryContent())
                .build();
        PlaceInquiry savedInquiry = inquiryRepository.save(placeInquiry);

        noticeService.saveNotice(NoticeType.INQUIRY_REGISTER,place.getUser().getId(),null,null);

        return PlaceInquiryDto.Response.from(savedInquiry);
    }
    @Override
    public Page<PlaceInquiryDto.Response> inquiryListWithPage(Long placeId, Pageable pageable) {
        return inquiryRepository.inquiryListWithPage(placeId,pageable)
                .map(PlaceInquiryDto.Response::from);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<PlaceInquiryListDto> getInquiriesByUserId(Long userId, Pageable pageable) {
        return inquiryRepository.findInquiriesByUserId(userId, pageable);
    }

    @Override
    public void deleteInquiryById(Long placeInquiryId,  Long userId) {
        User tmpUser = userRepository.getReferenceById(userId);

        PlaceInquiry foundInquiry = inquiryRepository.findByIdAndUser(placeInquiryId, tmpUser).orElseThrow(
                NoSuchElementException::new
        );

        inquiryRepository.delete(foundInquiry);
    }

    @Override
    public void updateInquiryResponse(InquiryResponseDto inquiryResponseDto) {
        Place tmpPlace = placeRepository.getReferenceById(inquiryResponseDto.getId());

        PlaceInquiry foundInquiry = inquiryRepository.findByIdAndPlace(inquiryResponseDto.getInquiryId(), tmpPlace).orElseThrow(
                NoSuchElementException::new
        );

        noticeService.saveNotice(NoticeType.INQUIRY_RESPONSE, foundInquiry.getUser().getId(), null, null);

        foundInquiry.updateResponse(inquiryResponseDto.getResponse());
    }

    @Override
    public Slice<UnansweredInquiryDto> findUnanswerdInquiriesSlice(Long placeId, Long userId, Pageable pageable) {
        return inquiryRepository.findUnansweredInquiriesPage(placeId, userId, pageable);
    }
}











