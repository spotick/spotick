package com.app.spotick.service.user;

import com.app.spotick.domain.dto.user.UserJoinDto;
import com.app.spotick.domain.dto.user.UserProfileDto;

import java.util.Optional;

public interface UserService {
    void join(UserJoinDto userJoinDto);

    Optional<UserProfileDto> getUserProfile(Long userId);
}
