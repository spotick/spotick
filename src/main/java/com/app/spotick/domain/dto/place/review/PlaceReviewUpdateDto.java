package com.app.spotick.domain.dto.place.review;

import jakarta.validation.constraints.*;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class PlaceReviewUpdateDto {
    private Long reviewId;
    private Long userId;

    @NotNull(message = "평가 점수를 선택해주세요.")
    @Min(value = 1, message = "평가는 반드시 1 이상이어야 합니다.")
    @Max(value = 5, message = "평가는 반드시 5 이하여야 합니다.")
    private Integer score;

    @NotBlank(message = "내용을 입력해주세요.")
    @Size(min = 10, message = "내용은 최소 10자 이상 입력해주세요.")
    @Size(max = 200, message = "내용은 최대 200자로 제한해주세요.")
    private String content;
}
