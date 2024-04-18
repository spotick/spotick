package com.app.spotick.domain.dto.promotion;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class PromotionRecommendListDto {
    private Long promotionId;
    private String title;
    private String subTitle;
    private FileDto thumbnailImage;

    public PromotionRecommendListDto(Long promotionId, String title, String subTitle, FileDto thumbnailImage) {
        this.promotionId = promotionId;
        this.title = title;
        this.subTitle = subTitle;
        this.thumbnailImage = thumbnailImage;
    }
}
