package com.app.spotick.service.place.bookmark;

import com.app.spotick.domain.entity.compositePk.PlaceBookmarkId;
import com.app.spotick.domain.entity.place.Place;
import com.app.spotick.domain.entity.place.PlaceBookmark;
import com.app.spotick.domain.entity.user.User;
import com.app.spotick.repository.place.PlaceRepository;
import com.app.spotick.repository.place.bookmark.PlaceBookmarkRepository;
import com.app.spotick.repository.user.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@Transactional
@RequiredArgsConstructor
public class PlaceBookmarkServiceImpl implements PlaceBookmarkService {
    private final PlaceBookmarkRepository placeBookmarkRepository;
    private final PlaceRepository placeRepository;
    private final UserRepository userRepository;

    @Override
    public void bookmark(Long placeId, Long userId) {
        // 프록시 객체를 활용하여 쿼리 수 단축
        Place tmpPlace = placeRepository.getReferenceById(placeId);
        User tmpuser = userRepository.getReferenceById(userId);

        PlaceBookmarkId id = new PlaceBookmarkId(placeId, userId);

        PlaceBookmark placeBookmark = PlaceBookmark.builder()
                .id(id)
                .place(tmpPlace)
                .user(tmpuser)
                .build();
        placeBookmarkRepository.save(placeBookmark);
    }

    @Override
    public void undoBookmark(Long placeId, Long userId) {
        Place tmpPlace = placeRepository.getReferenceById(placeId);
        User tmpUser = userRepository.getReferenceById(userId);

        placeBookmarkRepository.deleteByPlaceAndUser(tmpPlace, tmpUser);
    }

    @Override
    public boolean bookmarkCheck(Long placeId, Long userId) {
        // 프록시 객체를 활용하여 쿼리 수 단축
        Place tmpPlace = placeRepository.getReferenceById(placeId);
        User tmpUser = userRepository.getReferenceById(userId);

        Optional<PlaceBookmark> bookmark = placeBookmarkRepository.findByPlaceAndUser(tmpPlace, tmpUser);

        return bookmark.isPresent();
    }
}
