package com.app.spotick.service.user;

import com.app.spotick.domain.dto.user.UserJoinDto;
import com.app.spotick.domain.dto.user.UserProfileDto;

public interface UserService {
    void join(UserJoinDto userJoinDto);

    UserProfileDto getUserProfile(Long userId);

    void updateNickName(Long userId, String newNickName);

    void sendAuthCodeToTel(String tel);

    void updateTel(Long userId, String newTel);
}
