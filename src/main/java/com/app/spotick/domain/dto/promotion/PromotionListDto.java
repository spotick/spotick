package com.app.spotick.domain.dto.promotion;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class PromotionListDto {
    private Long promotionId;
    private String title;
    private FileDto thumbnailImage;

    public PromotionListDto(Long promotionId, String title, FileDto thumbnailImage) {
        this.promotionId = promotionId;
        this.title = title;
        this.thumbnailImage = thumbnailImage;
    }
}
