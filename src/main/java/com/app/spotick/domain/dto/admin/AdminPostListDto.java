package com.app.spotick.domain.dto.admin;

import com.app.spotick.domain.type.post.PostStatus;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@NoArgsConstructor
@Data
public class AdminPostListDto {
    private Long postId;
    private String hostEmail;
    private String title;
    private LocalDateTime createdDate;
    private PostStatus status;

    public AdminPostListDto(Long postId, String hostEmail, String title, LocalDateTime createdDate, PostStatus status) {
        this.postId = postId;
        this.hostEmail = hostEmail;
        this.title = title;
        this.createdDate = createdDate;
        this.status = status;
    }
}
