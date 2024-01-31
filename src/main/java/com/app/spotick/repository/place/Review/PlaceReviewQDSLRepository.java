package com.app.spotick.repository.place.Review;

import com.app.spotick.domain.dto.place.review.MypageReviewListDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface PlaceReviewQDSLRepository {

    Page<MypageReviewListDto> findReviewsByUserId(Long userId, Pageable pageable);
}
