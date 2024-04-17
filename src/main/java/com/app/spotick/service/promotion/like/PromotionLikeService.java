package com.app.spotick.service.promotion.like;

public interface PromotionLikeService {
    void doLike(Long promotionId, Long userId);

    void undoLike(Long promotionId, Long userId);
}
