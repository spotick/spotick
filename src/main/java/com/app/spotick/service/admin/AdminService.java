package com.app.spotick.service.admin;

import com.app.spotick.api.dto.admin.AdminPostApproveDto;
import com.app.spotick.api.dto.admin.AdminPostSearchDto;
import com.app.spotick.api.dto.admin.AdminUserAuthorityConfigDto;
import com.app.spotick.api.dto.admin.AdminUserSearchDto;
import com.app.spotick.api.dto.user.UserStatusDto;
import com.app.spotick.domain.dto.admin.AdminPostListDto;
import com.app.spotick.domain.dto.admin.AdminUserListDto;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;

import java.util.List;

public interface AdminService {
    Slice<AdminUserListDto> findAdminUserList(Pageable pageable, AdminUserSearchDto userSearchDto);
    Slice<AdminPostListDto> findAdminPlaceList(Pageable pageable, AdminPostSearchDto placeSearchDto);
    void updateUsersStatus(List<UserStatusDto> userStatusDtoList);

    void approveOrRejectPlace(AdminPostApproveDto.Request approveDto);

    void grantOrRevokeUserAuthority(AdminUserAuthorityConfigDto configDto);

    Slice<AdminPostListDto> findAdminTicketList(Pageable pageable,AdminPostSearchDto placeSearchDto);

    void approveOrRejectTicket(AdminPostApproveDto.Request approveDto);
}
