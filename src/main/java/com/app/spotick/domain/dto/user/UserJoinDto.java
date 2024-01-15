package com.app.spotick.domain.dto.user;

import com.app.spotick.domain.entity.user.User;
import com.app.spotick.domain.entity.user.UserProfileFile;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data @NoArgsConstructor
public class UserJoinDto {
    private Long id;
    private String email;
    private String password;
    private String nickName;
    private String tel;

    public UserJoinDto(User user) {
        this.id = user.getId();
        this.email = user.getEmail();
        this.password = user.getPassword();
        this.nickName = user.getNickName();
        this.tel = user.getTel();
    }

    public User toEntity(UserProfileFile userProfileFile){
        return User.builder()
                .email(email)
                .password(password)
                .nickName(nickName)
                .tel(tel)
                .userProfileFile(userProfileFile)
                .build();
    }



}
