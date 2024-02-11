package com.app.spotick.api.dto.place;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data @NoArgsConstructor
public class InquiryResponseDto {
    private Long id;
    private Long inquiryId;
    @NotBlank(message = "답변을 입력해주세요.")
    private String response;
}
