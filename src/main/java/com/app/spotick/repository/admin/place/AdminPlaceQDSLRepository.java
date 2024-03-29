package com.app.spotick.repository.admin.place;

import com.app.spotick.api.dto.admin.AdminPostSearchDto;
import com.app.spotick.domain.dto.admin.AdminPostListDto;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;

public interface AdminPlaceQDSLRepository {
    Slice<AdminPostListDto> findAdminPlaceList(Pageable pageable, AdminPostSearchDto placeSearchDto);
}
