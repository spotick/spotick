package com.app.spotick.api.dto.admin;

import com.app.spotick.domain.type.post.PostStatus;
import lombok.Data;
import lombok.NoArgsConstructor;

public class AdminPlaceApproveDto {

    @Data @NoArgsConstructor
    public static class Request{
        private Boolean isApprove;
        private Long placeId;
        private PostStatus status;
    }
}
