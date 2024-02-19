package com.app.spotick.service.place;

import com.app.spotick.domain.dto.place.*;
import com.app.spotick.domain.dto.place.PlaceEditDto;
import com.app.spotick.domain.dto.place.reservation.PlaceReserveBasicInfoDto;
import com.app.spotick.domain.entity.place.Place;
import com.app.spotick.domain.type.post.PostStatus;

import java.io.IOException;
import java.util.List;
import java.util.Optional;

public interface PlaceService {

    void registerPlace(PlaceRegisterDto placeRegisterDto,Long userId) throws IOException;

    Slice<PlaceListDto> findPlaceListPagination(Pageable pageable,Long userId);

    PlaceDetailDto findPlaceDetailById(Long placeId,Long userId);

    PlaceReserveBasicInfoDto findPlaceReserveDefaultInfo(Long placeId);

    Optional<Place> findPlace(Long placeId, Long userId);

    void updateStatus(Long placeId, PostStatus postStatus);

    void rejectAllReservationRequests(Long placeId);

    Optional<PlaceEditDto> findPlaceInfo(Long placeId, Long userId);

    void updatePlace(PlaceEditDto placeEditDto, Long userId) throws IOException;
}
