package com.app.spotick.service.place;

import com.app.spotick.domain.dto.place.PlaceListDto;

import java.util.List;

public interface PlaceBookmarkService {
    boolean bookmark(Long placeId, Long userId);

    boolean bookmarkCheck(Long placeId, Long userId);
}
