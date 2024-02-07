package com.app.spotick.domain.dto.place.inquiry;

import com.app.spotick.domain.dto.place.PlaceFileDto;
import com.app.spotick.domain.embedded.post.PostAddress;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data @NoArgsConstructor
public class PlaceInquiriesManageDto {
    private Long id;
    private String title;
    private Integer price;
    private PostAddress placeAddress;
    private PlaceFileDto placeFileDto;
    private double evalAvg;
    private Long evalCount;
    private Long bookmarkCount;
    private List<InquiryUnansweredDto> inquiryDtos;

    public PlaceInquiriesManageDto(Long id, String title, Integer price, PostAddress placeAddress, PlaceFileDto placeFileDto, double evalAvg, Long evalCount, Long bookmarkCount, List<InquiryUnansweredDto> inquiryDtos) {
        this.id = id;
        this.title = title;
        this.price = price;
        this.placeAddress = placeAddress;
        this.placeFileDto = placeFileDto;
        this.evalAvg = evalAvg;
        this.evalCount = evalCount;
        this.bookmarkCount = bookmarkCount;
        this.inquiryDtos = inquiryDtos;
    }
}
