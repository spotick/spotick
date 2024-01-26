package com.app.spotick.domain.dto.place.reservation;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data @NoArgsConstructor
public class PlaceReserveRegisterDto {
    private Long placeReservationId;
    private Long placeId;
    private String reservationCheckIn;
    private String reservationCheckOut;
    private String reservationContent; // 예약 전달 내용
    private Integer reservationVisitors;
    private Integer reservationAmount;
}
