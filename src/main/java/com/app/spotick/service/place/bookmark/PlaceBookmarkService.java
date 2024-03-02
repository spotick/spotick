package com.app.spotick.service.place.bookmark;

public interface PlaceBookmarkService {
    boolean bookmark(Long placeId, Long userId);

    boolean bookmarkCheck(Long placeId, Long userId);

}
