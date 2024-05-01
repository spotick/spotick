package com.app.spotick.service.place.reservation;

import com.app.spotick.domain.dto.place.reservation.PlaceReservationTimeDto;
import com.app.spotick.domain.dto.place.reservation.PlaceReserveRegisterDto;
import com.app.spotick.domain.dto.place.reservation.ReservationRequestListDto;
import com.app.spotick.domain.entity.place.PlaceReservation;
import com.app.spotick.domain.type.place.PlaceReservationStatus;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;

import java.util.List;

public interface PlaceReservationService {

    PlaceReservation findReservationByIdAndUser(Long reservationId, Long userId);

    void cancelReservation(Long reservationId);

    void deleteReservation(Long reservationId);

    void updateNotReviewing(Long reservationId);
    public void registerPlaceReservation(PlaceReserveRegisterDto placeReserveRegisterDto, Long userId);

    public boolean isReservationAvailable(PlaceReserveRegisterDto placeReserveRegisterDto);

    public List<PlaceReservationTimeDto> findReservedTimes(Long placeId, String selectedDate);

    Slice<ReservationRequestListDto> getReservationsOfPlace(Long placeId, Long userId, Pageable pageable);

    void updateReservationStatusAsHost(Long reservationId, Long userId, PlaceReservationStatus status);

    String updateReservationStatusAsUser(Long reservationId, Long userId, PlaceReservationStatus status);
}
