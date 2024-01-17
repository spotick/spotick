package com.app.spotick.service.place;

import com.app.spotick.domain.dto.place.PlaceListDto;
import com.app.spotick.repository.place.bookmark.PlaceBookmarkRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
@RequiredArgsConstructor
public class PlaceBookmarkServiceImpl implements PlaceBookmarkService {
    private final PlaceBookmarkRepository placeBookmarkRepository;
}
