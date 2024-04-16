package com.app.spotick.domain.dto.promotion;

import com.app.spotick.domain.embedded.post.PostAddress;
import com.app.spotick.domain.entity.promotion.PromotionBoard;
import com.app.spotick.domain.type.promotion.PromotionCategory;
import com.app.spotick.domain.type.user.UserStatus;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Data
@NoArgsConstructor
public class PromotionDetailDto {
    private Long promotionId;
    private Long userId;
    private String nickName;
    private FileDto userProfileImage;
    private LocalDateTime createdDate;
    private String title;
    private String subTitle;
    private String content;
    private FileDto thumbnailImage;
    private PromotionCategory category;
    private Long likeCount;
    private boolean isLiked;

    public PromotionDetailDto(Long promotionId, Long userId, String nickName, FileDto userProfileImage, LocalDateTime createdDate, String title, String subTitle, String content, FileDto thumbnailImage, PromotionCategory category, Long likeCount, boolean isLiked) {
        this.promotionId = promotionId;
        this.userId = userId;
        this.nickName = nickName;
        this.userProfileImage = userProfileImage;
        this.createdDate = createdDate;
        this.title = title;
        this.subTitle = subTitle;
        this.content = content;
        this.thumbnailImage = thumbnailImage;
        this.category = category;
        this.likeCount = likeCount;
        this.isLiked = isLiked;
    }
}
