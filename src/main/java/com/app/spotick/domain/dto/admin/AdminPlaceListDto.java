package com.app.spotick.domain.dto.admin;

import com.app.spotick.domain.type.post.PostStatus;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@NoArgsConstructor
@Data
public class AdminPlaceListDto {
    private Long placeId;
    private String hostEmail;
    private String title;
    private LocalDateTime createdDate;
    private PostStatus status;

    public AdminPlaceListDto(Long placeId, String hostEmail, String title, LocalDateTime createdDate, PostStatus status) {
        this.placeId = placeId;
        this.hostEmail = hostEmail;
        this.title = title;
        this.createdDate = createdDate;
        this.status = status;
    }
}
