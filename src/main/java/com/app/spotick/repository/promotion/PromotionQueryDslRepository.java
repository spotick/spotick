package com.app.spotick.repository.promotion;

import com.app.spotick.domain.dto.promotion.PromotionDetailDto;
import com.app.spotick.domain.dto.promotion.PromotionListDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Optional;

public interface PromotionQueryDslRepository {

    Optional<PromotionDetailDto> findPromotionDetailById(Long promotionId, Long userId);
}
