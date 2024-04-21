package com.app.spotick.service.place;

import com.app.spotick.domain.dto.place.*;
import com.app.spotick.domain.dto.place.PlaceEditDto;
import com.app.spotick.domain.dto.place.reservation.PlaceReserveBasicInfoDto;
import com.app.spotick.domain.entity.place.Place;
import com.app.spotick.domain.type.post.PostStatus;
import com.app.spotick.util.search.AreaFilter;
import com.app.spotick.util.type.PlaceSortType;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;

import java.io.IOException;
import java.util.Optional;

public interface PlaceService {

    void registerPlace(PlaceRegisterDto placeRegisterDto,Long userId) throws IOException;

    Slice<PlaceListDto> findPlaceListPagination(Pageable pageable, Long userId, PlaceSortType sortType, AreaFilter areaFilter, String keyword);

    PlaceDetailDto findPlaceDetailById(Long placeId,Long userId);

    PlaceReserveBasicInfoDto findPlaceReserveDefaultInfo(Long placeId);

    Optional<Place> findPlace(Long placeId, Long userId);

    void updateStatus(Long placeId, PostStatus postStatus);

    void rejectAllReservationRequests(Long placeId);

    Optional<PlaceEditDto> findPlaceInfo(Long placeId, Long userId);

    void updatePlace(PlaceEditDto placeEditDto, Long userId) throws IOException;
}
