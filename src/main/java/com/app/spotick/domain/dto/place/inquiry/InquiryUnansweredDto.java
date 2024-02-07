package com.app.spotick.domain.dto.place.inquiry;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data @NoArgsConstructor
public class InquiryUnansweredDto {
    private Long id;
    private Long userId;
    private Long placeId;
    private String inquiryTitle;
    private String content;
    private String nickName;
    private String fileName;
    private String uuid;
    private String uploadPath;
    private boolean isDefaultImage;

    public InquiryUnansweredDto(Long id, Long userId, Long placeId, String inquiryTitle, String content, String nickName, String fileName, String uuid, String uploadPath, boolean isDefaultImage) {
        this.id = id;
        this.userId = userId;
        this.placeId = placeId;
        this.inquiryTitle = inquiryTitle;
        this.content = content;
        this.nickName = nickName;
        this.fileName = fileName;
        this.uuid = uuid;
        this.uploadPath = uploadPath;
        this.isDefaultImage = isDefaultImage;
    }
}
