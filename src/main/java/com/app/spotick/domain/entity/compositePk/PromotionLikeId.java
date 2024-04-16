package com.app.spotick.domain.entity.compositePk;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Embeddable
@NoArgsConstructor
public class PromotionLikeId implements Serializable {
    @Column(name = "PROMOTION_ID")
    private Long promotionId;
    @Column(name = "USER_ID")
    private Long userId;

    public PromotionLikeId(Long promotionId, Long userId) {
        this.promotionId = promotionId;
        this.userId = userId;
    }
}
