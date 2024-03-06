package com.app.spotick.repository.admin;

import com.app.spotick.domain.dto.admin.AdminUserListDto;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;

public interface AdminQDSLRepository {

    Slice<AdminUserListDto> findAdminUserList(Pageable pageable);

}
