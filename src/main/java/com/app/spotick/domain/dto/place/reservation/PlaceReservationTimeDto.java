package com.app.spotick.domain.dto.place.reservation;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
public class PlaceReservationTimeDto {
    private Long placeId;
    private Long placeReservationId;
    private LocalDateTime checkIn;
    private LocalDateTime checkOut;

    public PlaceReservationTimeDto(Long placeId, Long placeReservationId, LocalDateTime checkIn, LocalDateTime checkOut) {
        this.placeId = placeId;
        this.placeReservationId = placeReservationId;
        this.checkIn = checkIn;
        this.checkOut = checkOut;
    }
}
