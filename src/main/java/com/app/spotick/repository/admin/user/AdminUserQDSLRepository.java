package com.app.spotick.repository.admin.user;

import com.app.spotick.domain.dto.admin.AdminUserListDto;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;

public interface AdminUserQDSLRepository {

    Slice<AdminUserListDto> findAdminUserList(Pageable pageable);

}
