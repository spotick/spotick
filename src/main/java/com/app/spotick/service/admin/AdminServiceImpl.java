package com.app.spotick.service.admin;

import com.app.spotick.api.dto.admin.AdminPlaceApproveDto;
import com.app.spotick.api.dto.admin.AdminUserSearchDto;
import com.app.spotick.api.dto.user.UserStatusDto;
import com.app.spotick.domain.dto.admin.AdminPlaceListDto;
import com.app.spotick.domain.dto.admin.AdminUserListDto;
import com.app.spotick.domain.entity.place.Place;
import com.app.spotick.domain.entity.place.PlaceModifyRequest;
import com.app.spotick.domain.type.post.PostModifyStatus;
import com.app.spotick.domain.type.post.PostStatus;
import com.app.spotick.repository.admin.place.AdminPlaceRepository;
import com.app.spotick.repository.admin.user.AdminUserRepository;
import com.app.spotick.repository.place.bookmark.PlaceBookmarkRepository;
import com.app.spotick.repository.place.inquiry.PlaceInquiryRepository;
import com.app.spotick.repository.place.modifyRequest.PlaceModifyReqRepository;
import com.app.spotick.repository.place.reservation.PlaceReservationRepository;
import com.app.spotick.service.user.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
@RequiredArgsConstructor
public class AdminServiceImpl implements AdminService{
    private final AdminUserRepository adminUserRepository;
    private final AdminPlaceRepository adminPlaceRepository;
    private final PlaceModifyReqRepository placeModifyReqRepository;
    private final PlaceBookmarkRepository placeBookmarkRepository;
    private final PlaceInquiryRepository placeInquiryRepository;
    private final PlaceReservationRepository placeReservationRepository;
    private final UserService userService;


    @Override
    @Transactional(readOnly = true)
    public Slice<AdminUserListDto> findAdminUserList(Pageable pageable, AdminUserSearchDto userSearchDto){
        return adminUserRepository.findAdminUserList(pageable,userSearchDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Slice<AdminPlaceListDto> findAdminPlaceList(Pageable pageable) {
        return adminPlaceRepository.findAdminPlaceList(pageable);
    }

    @Override
    public void updateUsersStatus(List<UserStatusDto> userStatusDtoList) {
        for (UserStatusDto userStatusDto : userStatusDtoList){
            userService.updateUserStatus(userStatusDto);
        }
    }

    @Override
    public void approveOrRejectPlace(AdminPlaceApproveDto.Request approveDto) {
        switch (approveDto.getStatus()){
            case REGISTRATION_PENDING -> {
                Place place = adminPlaceRepository.findById(approveDto.getPlaceId())
                        .orElseThrow(() -> new IllegalStateException("존재하지 않는 장소 id"));
                PostStatus changedStatus = approveDto.getIsApprove()?
                        PostStatus.APPROVED:PostStatus.REJECTED;
                place.setPlaceStatus(changedStatus);
            }
            case MODIFICATION_REQUESTED ->{
                handlePlaceModification(approveDto);
            }
        }
    }

    private void handlePlaceModification(AdminPlaceApproveDto.Request approveDto){
        Place reqPlace = adminPlaceRepository.getReferenceById(approveDto.getPlaceId());

        PlaceModifyRequest modifyRequest = placeModifyReqRepository.findByChangedPlace(reqPlace)
                .orElseThrow(() -> new IllegalStateException("잘못된 장소 정보"));
        Place changedPlace = modifyRequest.getChangedPlace();
        Place originalPlace = modifyRequest.getOriginalPlace();

//        거절된 경우 먼저 처리후 early return
        if(!approveDto.getIsApprove()){
            changedPlace.setPlaceStatus(PostStatus.REJECTED);
            originalPlace.setPlaceStatus(PostStatus.APPROVED);
            modifyRequest.setPlaceModifyStatus(PostModifyStatus.REJECTED);
            return ;
        }

//       조회수, 북마크, 문의, 예약 테이블의 장소들을 모두 기존 장소에서 바뀔 장소로 변경
        placeInquiryRepository.bulkUpdateInquiryPlace(originalPlace,changedPlace);
        placeBookmarkRepository.bulkUpdateBookmarkPlace(originalPlace,changedPlace);
        placeReservationRepository.bulkUpdateReservationPlace(originalPlace,changedPlace);

        changedPlace.setViewCount(originalPlace.getViewCount());
        changedPlace.setPlaceStatus(PostStatus.APPROVED);
        originalPlace.setPlaceStatus(PostStatus.REPLACED);
        modifyRequest.setPlaceModifyStatus(PostModifyStatus.APPROVED);
    }


}



















