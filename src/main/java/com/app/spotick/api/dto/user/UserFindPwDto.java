package com.app.spotick.api.dto.user;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data @NoArgsConstructor
public class UserFindPwDto {
    private String email;
    private String certCode;
}
