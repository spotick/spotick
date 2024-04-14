package com.app.spotick.domain.dto.promotion;

import com.app.spotick.domain.embedded.post.PostAddress;
import com.app.spotick.domain.entity.promotion.PromotionBoard;
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
public class PromotionDetailDto {
//    private Long promotionId;
//    private Long userId;
//    private String promotionTitle;
//    private String promotionSubTitle;
//    private PostAddress promotionAddress;
////    private Double promotionLat;
////    private Double promotionLng;
//    private PromotionFile promotionMainFile;
//    private Long likeCount;
//    private Boolean likeChecked;
//
//
//    public PromotionDetailDto(Long promotionId, Long userId, String promotionTitle, String promotionSubTitle, PostAddress promotionAddress, PromotionFile promotionMainFile, Long likeCount, Boolean likeChecked) {
//        this.promotionId = promotionId;
//        this.userId = userId;
//        this.promotionTitle = promotionTitle;
//        this.promotionSubTitle = promotionSubTitle;
//        this.promotionAddress = promotionAddress;
//        this.promotionMainFile = promotionMainFile;
//        this.likeCount = likeCount;
//        this.likeChecked = likeChecked;
//    }
}
