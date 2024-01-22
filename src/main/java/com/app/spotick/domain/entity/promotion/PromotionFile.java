package com.app.spotick.domain.entity.promotion;

import com.app.spotick.domain.base.image.ImageBase;
import com.app.spotick.domain.entity.place.Place;
import com.app.spotick.domain.entity.user.User;
import jakarta.persistence.*;
import lombok.*;

@Entity @Table(name = "TBL_PROMOTION_FILE")
@SequenceGenerator(name = "SEQ_PROMOTION_FILE_GENERATOR", sequenceName = "SEQ_PROMOTION_FILE",allocationSize = 1)
@Getter @ToString(callSuper = true) @NoArgsConstructor(access = AccessLevel.PROTECTED)
public class PromotionFile extends ImageBase {
    @Id @GeneratedValue(generator = "SEQ_PROMOTION_FILE_GENERATOR")
    @Column(name = "PROMOTION_FILE_ID")
    private Long id;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "USER_ID")
    private User user;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "PROMOTION_ID")
    private PromotionBoard promotionBoard;

    @Builder
    public PromotionFile(String fileName, String uuid, String uploadPath, Long id, User user, PromotionBoard promotionBoard) {
        super(fileName, uuid, uploadPath);
        this.id = id;
        this.user = user;
        this.promotionBoard = promotionBoard;
    }

    public void setPromotionBoard(PromotionBoard promotionBoard){
        this.promotionBoard = promotionBoard;
    }
}
