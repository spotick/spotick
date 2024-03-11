package com.app.spotick.api.dto.admin;

import com.app.spotick.domain.type.user.UserStatus;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data @NoArgsConstructor
public class AdminUserSearchDto {
    private String email;
    private String nickName;
    private UserStatus status;
}
