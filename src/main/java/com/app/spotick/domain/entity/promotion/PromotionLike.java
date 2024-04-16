package com.app.spotick.domain.entity.promotion;

import com.app.spotick.domain.base.Period;
import com.app.spotick.domain.entity.compositePk.PromotionLikeId;
import com.app.spotick.domain.entity.user.User;
import jakarta.persistence.*;
import lombok.*;
import org.springframework.data.domain.Persistable;

@Entity @Table(name = "TBL_PROMOTION_LIKE")
@Getter @ToString(callSuper = true) @NoArgsConstructor(access = AccessLevel.PROTECTED)
public class PromotionLike extends Period implements Persistable<PromotionLikeId> {

    @EmbeddedId
    public PromotionLikeId id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "USER_ID", insertable = false, updatable = false)
    private User user;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "PROMOTION_ID", insertable = false, updatable = false)
    private PromotionBoard promotionBoard;

    @Builder
    public PromotionLike(PromotionLikeId id, User user, PromotionBoard promotionBoard) {
        this.id = id;
        this.user = user;
        this.promotionBoard = promotionBoard;
    }

    @Override
    public boolean isNew() {
        return getCreatedDate() == null;
    }
}
