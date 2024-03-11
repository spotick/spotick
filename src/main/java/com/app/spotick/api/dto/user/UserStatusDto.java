package com.app.spotick.api.dto.user;

import com.app.spotick.domain.type.user.UserStatus;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data @NoArgsConstructor
public class UserStatusDto {
    private Long userId;
    private UserStatus status;

    public UserStatusDto(Long userId, UserStatus status) {
        this.userId = userId;
        this.status = status;
    }
}
