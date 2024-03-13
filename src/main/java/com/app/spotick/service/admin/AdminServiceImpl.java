package com.app.spotick.service.admin;

import com.app.spotick.api.dto.admin.AdminPlaceApproveDto;
import com.app.spotick.api.dto.admin.AdminUserSearchDto;
import com.app.spotick.api.dto.user.UserStatusDto;
import com.app.spotick.domain.dto.admin.AdminPlaceListDto;
import com.app.spotick.domain.dto.admin.AdminUserListDto;
import com.app.spotick.domain.entity.place.Place;
import com.app.spotick.domain.type.post.PostStatus;
import com.app.spotick.repository.admin.place.AdminPlaceRepository;
import com.app.spotick.repository.admin.user.AdminUserRepository;
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

            }
        }
    }
}
