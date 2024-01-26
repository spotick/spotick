package com.app.spotick.domain.entity.promotion;

import com.app.spotick.domain.base.post.PostBase;
import com.app.spotick.domain.embedded.post.PostAddress;
import com.app.spotick.domain.entity.user.User;
import com.app.spotick.domain.type.post.PostStatus;
import com.app.spotick.domain.type.promotion.PromotionCategory;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;

@Entity @Table(name = "TBL_PROMOTION_BOARD")
@SequenceGenerator(name = "SEQ_PROMOTION_BOARD_GENERATOR", sequenceName = "SEQ_PROMOTION_BOARD",allocationSize = 1)
@Getter @ToString(callSuper = true) @NoArgsConstructor(access = AccessLevel.PROTECTED)
public class PromotionBoard extends PostBase {
    @Id @GeneratedValue(generator = "SEQ_PROMOTION_BOARD_GENERATOR")
    @Column(name = "PROMOTION_ID")
    private Long id;
    private String subTitle;
    @Lob
    private String content;
    private LocalDate startDate;
    private LocalDate endDate;
    @Embedded
    private PostAddress promotionAddress;
    private PromotionCategory promotionCategory;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "USER_ID")
    private User user;

    @Builder
    public PromotionBoard(String title, int viewCount, Double lat, Double lng, Long id, String subTitle, String content, LocalDate startDate, LocalDate endDate, PostAddress promotionAddress,PromotionCategory promotionCategory, User user) {
        super(title, viewCount, lat, lng);
        this.id = id;
        this.subTitle = subTitle;
        this.content = content;
        this.startDate = startDate;
        this.endDate = endDate;
        this.promotionAddress = promotionAddress;
        this.promotionCategory = promotionCategory;
        this.user = user;
    }


    public void setWriter(User user){
        this.user = user;
    }


}
