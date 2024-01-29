package com.app.spotick.service.place.review;

import com.app.spotick.domain.dto.place.reservation.PlaceReviewRegisterDto;

public interface PlaceReviewService {
    void registerReview(PlaceReviewRegisterDto placeReviewRegisterDto);
}
