package com.app.spotick.domain.dto.user;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data @NoArgsConstructor
public class UserJoinDto {
    private Long id;
    private String email;
    private String password;
    private String nickName;
    private String tel;
}
