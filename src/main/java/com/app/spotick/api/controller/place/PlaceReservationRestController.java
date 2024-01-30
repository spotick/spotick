package com.app.spotick.api.controller.place;

import com.app.spotick.domain.dto.place.reservation.PlaceReserveRegisterDto;
import com.app.spotick.service.place.reservation.PlaceReservationService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/reservations/*")
public class PlaceReservationRestController {
    private final PlaceReservationService reservationService;

    @PostMapping("/v1/check")
    public ResponseEntity<Boolean> reservationCheck(@RequestBody PlaceReserveRegisterDto placeReserveRegisterDto){

        boolean reservationAvailable = !reservationService.isReservationAvailable(placeReserveRegisterDto);
        System.out.println("reservationAvailable = " + reservationAvailable);
        return ResponseEntity.ok(reservationAvailable);
    }



}
















