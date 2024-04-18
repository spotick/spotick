package com.app.spotick.service.promotion;

import com.app.spotick.domain.dto.promotion.PromotionDetailDto;
import com.app.spotick.domain.dto.promotion.PromotionListDto;
import com.app.spotick.domain.dto.promotion.PromotionRecommendListDto;
import com.app.spotick.domain.dto.promotion.PromotionRegisterDto;
import com.app.spotick.domain.type.promotion.PromotionCategory;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.lang.Nullable;

import java.io.IOException;
import java.util.List;

public interface PromotionService {

    Long promotionBoardSave(PromotionRegisterDto promotionRegisterDto) throws IOException;

    PromotionDetailDto getPromotionBoardById(Long promotionId, Long userId);

    Slice<PromotionListDto> getPromotionBoards(Pageable pageable, PromotionCategory category);

    Slice<PromotionListDto> getPromotionBoardsOfUser(Pageable pageable, Long writerId, Long promotionId);

    List<PromotionRecommendListDto> getRecommendPromotionBoards();

}
