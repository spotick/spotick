package com.app.spotick.domain.dto.place;

import com.app.spotick.domain.entity.place.PlaceReview;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Data @NoArgsConstructor
public class PlaceReviewListDto {
    private Long reviewId;
    private String content;
    private Integer score;
    private Long placeId;
    private Long userId;
    private String userNickname;
    private String createdDate;

    public PlaceReviewListDto(Long reviewId, String content, Integer score, Long placeId, Long userId, String userNickname, LocalDateTime createdDate) {
        this.reviewId = reviewId;
        this.content = content;
        this.score = score;
        this.placeId = placeId;
        this.userId = userId;
        this.userNickname = userNickname;
        this.createdDate = createdDate.format(DateTimeFormatter.ofPattern("yyyy-MM-dd"));
    }


}

















