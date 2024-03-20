package com.app.spotick.api.dto.admin;

import com.app.spotick.domain.type.post.PostStatus;
import lombok.Data;
import lombok.NoArgsConstructor;

public class AdminPostApproveDto {

    @Data @NoArgsConstructor
    public static class Request{
        private Boolean isApprove;
        private Long postId;
        private PostStatus status;
    }
}
