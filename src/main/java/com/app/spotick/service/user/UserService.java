package com.app.spotick.service.user;

import com.app.spotick.domain.dto.place.PlaceListDto;
import com.app.spotick.domain.dto.user.UserJoinDto;
import com.app.spotick.domain.dto.user.UserProfileDto;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.List;

public interface UserService {
    void join(UserJoinDto userJoinDto);

    UserProfileDto getUserProfile(Long userId);

    void updateNickName(Long userId, String newNickName);

    void sendAuthCodeToTel(String tel);

    void updateTel(Long userId, String newTel);

    void updatePassword(Long userId, String newPassword);

    List<PlaceListDto> findBookmarkedPlacesByUserId(Long userId);
}
