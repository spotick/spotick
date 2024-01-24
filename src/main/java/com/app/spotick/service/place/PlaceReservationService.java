package com.app.spotick.service.place;

import com.app.spotick.domain.entity.place.PlaceReservation;

import java.util.Optional;

public interface PlaceReservationService {

    Optional<PlaceReservation> findReservationByIdAndUser(Long reservationId, Long userId);

    void cancelReservation(Long reservationId);

    void deleteReservation(Long reservationId);
}
