package com.app.spotick.domain.dto.place.inquiry;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data @NoArgsConstructor
public class UnansweredInquiryDto {
    private Long id;
    private String inquiryTitle;
    private String content;
    private String nickname;
    private String fileName;
    private String uuid;
    private String uploadPath;
    private boolean isDefaultImage;

    public UnansweredInquiryDto(Long id, String inquiryTitle, String content, String nickname, String fileName, String uuid, String uploadPath, boolean isDefaultImage) {
        this.id = id;
        this.inquiryTitle = inquiryTitle;
        this.content = content;
        this.nickname = nickname;
        this.fileName = fileName;
        this.uuid = uuid;
        this.uploadPath = uploadPath;
        this.isDefaultImage = isDefaultImage;
    }
}
