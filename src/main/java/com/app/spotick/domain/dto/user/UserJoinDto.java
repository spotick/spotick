package com.app.spotick.domain.dto.user;

import com.app.spotick.domain.entity.user.User;
import com.app.spotick.domain.entity.user.UserProfileFile;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Map;
import java.util.UUID;

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
                .id(id)
                .email(email)
                .password(password)
                .nickName(nickName)
                .tel(tel)
                .userProfileFile(userProfileFile)
                .build();
    }

    public static UserJoinDto fromKakao(Map<String,Object> kakaoAccount) {
        UserJoinDto userJoinDto = new UserJoinDto();
        Map<String, Object> kakaoProfile = (Map<String, Object>) kakaoAccount.get("profile");
        String email = (String)kakaoAccount.get("email");
        String nickname = (String)kakaoProfile.get("nickname");
        String profileImgUrl = (String)kakaoProfile.get("profile_image_url");
        boolean isDefaultImg = (boolean)kakaoProfile.get("is_default_image");

        userJoinDto.setEmail(email);
        userJoinDto.setNickName(nickname);
        userJoinDto.setPassword(UUID.randomUUID().toString());

        return userJoinDto;
    }



}
