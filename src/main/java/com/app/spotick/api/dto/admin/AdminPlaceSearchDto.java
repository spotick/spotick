package com.app.spotick.api.dto.admin;

import com.app.spotick.domain.type.post.PostStatus;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class AdminPlaceSearchDto {
    private String email;
    private String placeTitle;
    private PostStatus status;
}
