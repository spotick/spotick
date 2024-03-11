package com.app.spotick.service.admin;

import com.app.spotick.api.dto.admin.AdminUserSearchDto;
import com.app.spotick.api.dto.user.UserStatusDto;
import com.app.spotick.domain.dto.admin.AdminPlaceListDto;
import com.app.spotick.domain.dto.admin.AdminUserListDto;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;

import java.util.List;

public interface AdminService {
    Slice<AdminUserListDto> findAdminUserList(Pageable pageable, AdminUserSearchDto userSearchDto);
    Slice<AdminPlaceListDto> findAdminPlaceList(Pageable pageable);

    void updateUsersStatus(List<UserStatusDto> userStatusDtoList);
}
