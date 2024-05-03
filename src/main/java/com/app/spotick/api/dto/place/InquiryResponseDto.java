package com.app.spotick.api.dto.place;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data @NoArgsConstructor
public class InquiryResponseDto {
    private Long id;
    private Long inquiryId;
    @NotBlank(message = "답변을 입력해주세요.")
    @Size(min = 10, message = "최소 10자 이상 입력해주세요.")
    @Size(max = 200, message = "최대 200자로 답변을 제한해주세요.")
    private String response;
}
