package com.app.spotick.domain.dto.place;

import com.app.spotick.domain.embedded.post.PostAddress;
import com.app.spotick.domain.type.place.PlaceReservationStatus;
import jakarta.persistence.Column;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
@Data @NoArgsConstructor
public class PlaceInquiryListDto {
    private Long id;
    private Long inquiryId;
    private String placeTitle;
    private Integer price;
    private PostAddress placeAddress;
    private PlaceFileDto placeFileDto;
    private double evalAvg;
    private Long evalCount;
    private Long bookmarkCount;
    private String inquiryTitle;
    private String content;
    private String response;

    public PlaceInquiryListDto(Long id, Long inquiryId, String placeTitle, Integer price, PostAddress placeAddress, PlaceFileDto placeFileDto, double evalAvg, Long evalCount, Long bookmarkCount, String inquiryTitle, String content, String response) {
        this.id = id;
        this.inquiryId = inquiryId;
        this.placeTitle = placeTitle;
        this.price = price;
        this.placeAddress = placeAddress;
        this.placeFileDto = placeFileDto;
        this.evalAvg = evalAvg;
        this.evalCount = evalCount;
        this.bookmarkCount = bookmarkCount;
        this.inquiryTitle = inquiryTitle;
        this.content = content;
        this.response = response;
    }
}
