package com.app.spotick.service.place;

import com.app.spotick.domain.dto.place.PlaceListDto;
import com.app.spotick.domain.entity.place.Place;
import com.app.spotick.domain.entity.place.PlaceBookmark;
import com.app.spotick.domain.entity.user.User;
import com.app.spotick.repository.place.PlaceRepository;
import com.app.spotick.repository.place.bookmark.PlaceBookmarkRepository;
import com.app.spotick.repository.user.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

@Service
@Transactional
@RequiredArgsConstructor
public class PlaceBookmarkServiceImpl implements PlaceBookmarkService {
    private final PlaceBookmarkRepository placeBookmarkRepository;
    private final PlaceRepository placeRepository;
    private final UserRepository userRepository;

    @Override
    public boolean bookmark(Long placeId, Long userId) {
        Place place = placeRepository.findById(placeId).orElseThrow(
                NoSuchElementException::new
        );

        User user = userRepository.findById(userId).orElseThrow(
                NoSuchElementException::new
        );

        Optional<PlaceBookmark> bookmark = placeBookmarkRepository.findByPlaceAndUser(place, user);

        if (bookmark.isEmpty()) {
            PlaceBookmark placeBookmark = PlaceBookmark.builder()
                    .place(place)
                    .user(user)
                    .build();
            placeBookmarkRepository.save(placeBookmark);
            return true;
        } else {
            placeBookmarkRepository.delete(bookmark.get());
            return false;
        }

    }

    @Override
    public boolean bookmarkCheck(Long placeId, Long userId) {
        Place place = placeRepository.findById(placeId).orElseThrow(
                NoSuchElementException::new
        );

        User user = userRepository.findById(userId).orElseThrow(
                NoSuchElementException::new
        );

        Optional<PlaceBookmark> bookmark = placeBookmarkRepository.findByPlaceAndUser(place, user);

        return bookmark.isPresent();
    }
}
