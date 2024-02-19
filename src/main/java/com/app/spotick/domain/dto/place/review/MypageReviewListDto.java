package com.app.spotick.domain.dto.place.review;

import com.app.spotick.domain.dto.place.file.PlaceFileDto;
import com.app.spotick.domain.embedded.post.PostAddress;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data @NoArgsConstructor
public class MypageReviewListDto {
    private Long id;
    private Long reservationId;
    private Long reviewId;
    private String title;
    private Integer price;
    private PostAddress placeAddress;
    private PlaceFileDto placeFileDto;
    private double evalAvg;
    private Long evalCount;
    private Long bookmarkCount;
    private LocalDateTime createdDate;
    private int score;
    private String content;

    public MypageReviewListDto(Long id, Long reservationId, Long reviewId, String title, Integer price, PostAddress placeAddress, PlaceFileDto placeFileDto, double evalAvg, Long evalCount, Long bookmarkCount, LocalDateTime createdDate, int score, String content) {
        this.id = id;
        this.reservationId = reservationId;
        this.reviewId = reviewId;
        this.title = title;
        this.price = price;
        this.placeAddress = placeAddress;
        this.placeFileDto = placeFileDto;
        this.evalAvg = evalAvg;
        this.evalCount = evalCount;
        this.bookmarkCount = bookmarkCount;
        this.createdDate = createdDate;
        this.score = score;
        this.content = content;
    }
}
