package com.app.spotick.service.promotion;

import com.app.spotick.domain.dto.promotion.PromotionRegisterDto;
import com.app.spotick.domain.entity.promotion.PromotionBoard;

import java.io.IOException;

public interface PromotionService {

    Long promotionBoardSave(PromotionRegisterDto promotionRegisterDto) throws IOException;

}
