package com.app.spotick.domain.dto.promotion;

import com.app.spotick.customValidation.annotation.ValidFile;
import com.app.spotick.domain.entity.promotion.PromotionBoard;
import com.app.spotick.domain.type.promotion.PromotionCategory;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.web.multipart.MultipartFile;

@Data
@NoArgsConstructor
public class PromotionEditDto {
    private Long promotionId;
    private Long userId;
    @NotBlank(message = "제목은 필수 입력사항입니다.")
    @Size(max = 250, message = "입력한 글자수가 너무 많습니다.")
    private String title;

    private String subTitle;

    @NotBlank(message = "내용을 작성해주세요.")
    private String content;
    @NotNull(message = "카테고리를 선택해주세요.")
    private PromotionCategory category;

    private FileDto fileDto;

    // 새로 추가한 파일의 경우
    private MultipartFile file;

    public PromotionEditDto(Long promotionId, String title, String subTitle, String content, PromotionCategory category, FileDto fileDto) {
        this.promotionId = promotionId;
        this.title = title;
        this.subTitle = subTitle;
        this.content = content;
        this.category = category;
        this.fileDto = fileDto;
    }

    public PromotionBoard toEntity() {
        return PromotionBoard.builder()
                .id(promotionId)
                .title(title)
                .content(content)
                .promotionCategory(category)
                .build();
    }
}
