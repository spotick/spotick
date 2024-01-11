package com.app.spotick.domain.entity.promotion;

import com.app.spotick.domain.base.Period;
import com.app.spotick.domain.entity.user.User;
import jakarta.persistence.*;
import lombok.*;

@Entity @Table(name = "TBL_PROMOTION_LIKE")
@SequenceGenerator(name = "SEQ_PROMOTION_LIKE_GENERATOR", sequenceName = "SEQ_PROMOTION_LIKE",allocationSize = 1)
@Getter @ToString(callSuper = true) @NoArgsConstructor(access = AccessLevel.PROTECTED)
public class PromotionLike extends Period {
    @Id @GeneratedValue(generator = "SEQ_PROMOTION_LIKE_GENERATOR")
    @Column(name = "PROMOTION_LIKE_ID")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "USER_ID")
    private User user;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "PROMOTION_ID")
    private PromotionBoard promotionBoard;

    @Builder
    public PromotionLike(Long id, User user, PromotionBoard promotionBoard) {
        this.id = id;
        this.user = user;
        this.promotionBoard = promotionBoard;
    }
}
