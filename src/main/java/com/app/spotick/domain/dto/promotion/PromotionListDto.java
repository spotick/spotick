package com.app.spotick.domain.dto.promotion;

import com.app.spotick.domain.embedded.post.PostAddress;
import com.app.spotick.domain.entity.promotion.PromotionBoard;
import com.app.spotick.domain.entity.promotion.PromotionFile;
import com.app.spotick.domain.type.promotion.PromotionCategory;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
import java.util.List;

@Data
@NoArgsConstructor
public class PromotionListDto {
    private Long promotionId;
    private Long userId;
    private String promotionTitle;
    private String promotionSubTitle;


    private List<PromotionFile> promotionMainFile;

//    @NotNull(message = "카테고리를 선택해주세요")
//    private PromotionCategory promotionCategory;


    public PromotionListDto(Long promotionId, Long userId, String promotionTitle, String promotionSubTitle, List<PromotionFile> promotionMainFile) {
        this.promotionId = promotionId;
        this.userId = userId;
        this.promotionTitle = promotionTitle;
        this.promotionSubTitle = promotionSubTitle;
        this.promotionMainFile = promotionMainFile;
    }

    //    조회수 제외됨
    public PromotionBoard toEntity(){
        return PromotionBoard.builder()
                .id(promotionId)
                .title(promotionTitle)
                .subTitle(promotionSubTitle)
                .build();
    }
}
