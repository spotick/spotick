package com.app.spotick.api.dto.admin;

import com.app.spotick.domain.type.post.PostStatus;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class AdminPostSearchDto {
    private String email;
    private String postTitle;
    private PostStatus status;
}
