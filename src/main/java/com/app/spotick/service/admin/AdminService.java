package com.app.spotick.service.admin;

import com.app.spotick.domain.dto.admin.AdminUserListDto;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;

public interface AdminService {
    Slice<AdminUserListDto> findAdminUserList(Pageable pageable);
}
