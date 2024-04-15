package com.app.spotick.repository.promotion;

import com.app.spotick.domain.dto.promotion.PromotionDetailDto;

import java.util.Optional;

public interface PromotionQDSLRepository {

    Optional<PromotionDetailDto> findPromotionById(Long promotionId, Long userId);

}
