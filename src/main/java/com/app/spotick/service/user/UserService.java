package com.app.spotick.service.user;

import com.app.spotick.domain.dto.place.PlaceListDto;
import com.app.spotick.domain.dto.place.PlaceManageListDto;
import com.app.spotick.domain.dto.place.PlaceReservationListDto;
import com.app.spotick.domain.dto.place.reservation.PlaceReservedNotReviewedDto;
import com.app.spotick.domain.dto.place.review.ContractedPlaceDto;
import com.app.spotick.domain.dto.place.review.MypageReviewListDto;
import com.app.spotick.domain.dto.user.UserJoinDto;
import com.app.spotick.domain.dto.user.UserProfileDto;
import com.app.spotick.domain.entity.user.UserProfileFile;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Optional;

public interface UserService {
//    일반 회원 등록
    void join(UserJoinDto userJoinDto);

//    oauth2연결로 프로필사진이 있는 경우 회원 등록
    void join(UserJoinDto userJoinDto, UserProfileFile userProfileFile);

    UserProfileDto getUserProfile(Long userId);

    void updateNickName(Long userId, String newNickName);

    void sendAuthCodeToTel(String tel);

    void updateTel(Long userId, String newTel);

    void updatePassword(Long userId, String newPassword);

    Page<PlaceListDto> findBookmarkedPlacesByUserId(Long userId, Pageable pageable);

    Page<PlaceReservationListDto> findReservationsByUserId(Long userId, Pageable pageable);

    Page<PlaceReservedNotReviewedDto> findPlacesNotReviewed(Long userId, Pageable pageable);

    Page<MypageReviewListDto> findReviewedList(Long userId, Pageable pageable);

    Page<PlaceManageListDto> findHostPlacesPage(Long userId, Pageable pageable);

    Optional<ContractedPlaceDto> findPlaceBriefly(Long placeId, Long userId);

    boolean isValidEmail(String email);
    boolean isValidNickname(String nickname);

    boolean checkUserByNicknameAndTel(String nickname, String tel);


}
