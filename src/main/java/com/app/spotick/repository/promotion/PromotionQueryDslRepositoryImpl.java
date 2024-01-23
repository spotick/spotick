package com.app.spotick.repository.promotion;

import com.app.spotick.domain.dto.promotion.PromotionListDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public class PromotionQueryDslRepositoryImpl implements PromotionQueryDslRepository {
    @Override
    public Page<PromotionListDto> findListWithPage(Pageable pageable) {
        return null;
    }
}
