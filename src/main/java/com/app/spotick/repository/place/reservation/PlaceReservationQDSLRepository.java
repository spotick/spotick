package com.app.spotick.repository.place.reservation;

import com.app.spotick.domain.dto.place.PlaceReservationListDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface PlaceReservationQDSLRepository {
    Page<PlaceReservationListDto> findReservationsByUserId(Long userId, Pageable pageable);
}
