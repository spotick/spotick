package com.app.spotick.repository.promotion;

import com.app.spotick.domain.dto.promotion.PromotionListDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Optional;

public interface PromotionQueryDslRepository {
    Page<PromotionListDto> findListWithPage(Pageable pageable);
}
