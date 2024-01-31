package com.app.spotick.service.place.review;

import com.app.spotick.domain.dto.place.review.PlaceReviewRegisterDto;
import com.app.spotick.domain.dto.place.review.PlaceReviewUpdateDto;
import com.app.spotick.domain.entity.place.PlaceReview;

import java.util.Optional;

public interface PlaceReviewService {
    void registerReview(PlaceReviewRegisterDto placeReviewRegisterDto);

    void updateReview(PlaceReviewUpdateDto placeReviewUpdateDto);

    Optional<PlaceReview> findReview(Long reviewId, Long userId);
}
