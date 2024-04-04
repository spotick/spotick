package com.app.spotick.service.place.bookmark;

public interface PlaceBookmarkService {
    void bookmark(Long placeId, Long userId);

    void undoBookmark(Long placeId, Long userId);

    boolean bookmarkCheck(Long placeId, Long userId);

}
