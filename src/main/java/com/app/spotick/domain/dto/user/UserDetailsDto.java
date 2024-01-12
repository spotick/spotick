package com.app.spotick.domain.dto.user;

import com.app.spotick.domain.entity.user.User;
import com.app.spotick.domain.type.user.UserStatus;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor @Data
public class UserDetailsDto {
    private Long id;
    private String email;
    private String password;
    private String nickName;
    private String tel;
    private UserStatus userStatus;

    public UserDetailsDto(User user) {
        this.id = user.getId();
        this.email = user.getEmail();
        this.password = user.getPassword();
        this.nickName = user.getNickName();
        this.tel = user.getTel();
        this.userStatus = user.getUserStatus();
    }
}
