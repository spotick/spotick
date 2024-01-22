package com.app.spotick.service.promotion;

import com.app.spotick.domain.dto.promotion.PromotionRegisterDto;

import java.io.IOException;

public interface PromotionService {
    void registerPromotion(PromotionRegisterDto promotionRegisterDto, Long userId) throws IOException;
}
