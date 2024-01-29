package com.app.spotick.service.user;

import com.app.spotick.domain.dto.place.PlaceListDto;
import com.app.spotick.domain.dto.place.PlaceReservationListDto;
import com.app.spotick.domain.dto.place.reservation.PlaceReservedNotReviewedDto;
import com.app.spotick.domain.dto.user.UserJoinDto;
import com.app.spotick.domain.dto.user.UserProfileDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface UserService {
    void join(UserJoinDto userJoinDto);

    UserProfileDto getUserProfile(Long userId);

    void updateNickName(Long userId, String newNickName);

    void sendAuthCodeToTel(String tel);

    void updateTel(Long userId, String newTel);

    void updatePassword(Long userId, String newPassword);

    Page<PlaceListDto> findBookmarkedPlacesByUserId(Long userId, Pageable pageable);

    Page<PlaceReservationListDto> findReservationsByUserId(Long userId, Pageable pageable);

    Page<PlaceReservedNotReviewedDto> findPlacesNotReviewed(Long userId, Pageable pageable);

}
