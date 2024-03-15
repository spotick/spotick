package com.app.spotick.repository.admin.place;

import com.app.spotick.api.dto.admin.AdminPlaceSearchDto;
import com.app.spotick.domain.dto.admin.AdminPlaceListDto;
import com.app.spotick.domain.dto.admin.AdminUserListDto;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;

public interface AdminPlaceQDSLRepository {
    Slice<AdminPlaceListDto> findAdminPlaceList(Pageable pageable, AdminPlaceSearchDto placeSearchDto);
}
