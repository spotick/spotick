package com.app.spotick.domain.dto.user;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data @NoArgsConstructor
public class UserModifyPwDto {
    private String email;
    private String password;
}
