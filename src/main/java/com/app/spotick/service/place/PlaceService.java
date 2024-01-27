package com.app.spotick.service.place;

import com.app.spotick.domain.dto.place.PlaceDetailDto;
import com.app.spotick.domain.dto.place.PlaceListDto;
import com.app.spotick.domain.dto.place.PlaceRegisterDto;
import com.app.spotick.domain.dto.place.reservation.PlaceReserveBasicInfoDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;

import java.io.IOException;
import java.util.List;

public interface PlaceService {

    void registerPlace(PlaceRegisterDto placeRegisterDto,Long userId) throws IOException;

    List<PlaceListDto> findPlaceListPagination(int pageRequest,Long userId);

    PlaceDetailDto findPlaceDetailById(Long placeId,Long userId);

    PlaceReserveBasicInfoDto findPlaceReserveDefaultInfo(Long placeId);
    
}
