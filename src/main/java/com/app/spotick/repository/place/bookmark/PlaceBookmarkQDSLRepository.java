package com.app.spotick.repository.place.bookmark;

import com.app.spotick.domain.dto.place.PlaceListDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface PlaceBookmarkQDSLRepository {
    Page<PlaceListDto> findBookmarkedPlacesByUserId(Long userId, Pageable pageable);



}
