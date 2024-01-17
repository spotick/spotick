package com.app.spotick.repository.place.bookmark;

import com.app.spotick.domain.dto.place.PlaceListDto;

import java.util.List;

public interface PlaceBookmarkQDSLRepository {
    List<PlaceListDto> findBookmarkedPlacesByUserId(Long userId);





}
