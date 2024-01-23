package com.app.spotick.service.promotion;

import com.app.spotick.domain.dto.promotion.PromotionDetailDto;
import com.app.spotick.domain.dto.promotion.PromotionListDto;
import com.app.spotick.domain.dto.promotion.PromotionRegisterDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.io.IOException;

public interface PromotionService {
    void registerPromotion(PromotionRegisterDto promotionRegisterDto, Long userId) throws IOException;

//    void modifyPromotion(PromotionEditDto promotionEditDto);

    void removePromotion(Long promotionId);

    PromotionDetailDto promotionDetail(Long promotionId);

    Page<PromotionListDto> findPromotionList(Pageable pageable);
}
