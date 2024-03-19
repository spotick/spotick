package com.app.spotick.api.dto.admin;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data @NoArgsConstructor
public class AdminUserAuthorityConfigDto {
    private Long userId;
    private Boolean isGranted;
}
