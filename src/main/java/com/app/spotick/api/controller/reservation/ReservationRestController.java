package com.app.spotick.api.controller.reservation;

import com.app.spotick.api.dto.response.CommonResponse;
import com.app.spotick.domain.dto.place.reservation.ReservationRequestListDto;
import com.app.spotick.domain.dto.user.UserDetailsDto;
import com.app.spotick.domain.type.place.PlaceReservationStatus;
import com.app.spotick.service.place.reservation.PlaceReservationService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/reservation/api")
@RequiredArgsConstructor
public class ReservationRestController {
    private final PlaceReservationService placeReservationService;

    @GetMapping("/get/{placeId}")
    public ResponseEntity<CommonResponse<Slice<ReservationRequestListDto>>> getReservationsOfPlace(@PathVariable("placeId") Long placeId,
                                                                                                   @AuthenticationPrincipal UserDetailsDto userDetailsDto,
                                                                                                   @RequestParam(name = "page", defaultValue = "0") int page) {
        // placeId 검증은 없으면 요청 자체를 할 수 없으므로 검증은 무의미 할 것으로 판단.
        // placeId와 userId를 통해 place의 주인을 쿼리에서 검증해내므로 결과는 거기서 판단될 것.

        Pageable pageable = PageRequest.of(page, 10);

        Slice<ReservationRequestListDto> contentsSlice = placeReservationService.getReservationsOfPlace(placeId, userDetailsDto.getId(), pageable);

        if (contentsSlice.isEmpty()) {
            return ResponseEntity.noContent().build();
        }

        return ResponseEntity.ok(new CommonResponse<>(true, "조회 성공", contentsSlice));
    }

    @PatchMapping("/approve/{reservationId}")
    public ResponseEntity<String> approveReservation(@PathVariable("reservationId") Long reservationId,
                                                     @AuthenticationPrincipal UserDetailsDto userDetailsDto) {

        try {
            placeReservationService.updateReservationStatus(
                    reservationId,
                    userDetailsDto.getId(),
                    PlaceReservationStatus.WAITING_PAYMENT // 결제를 할 수 있도록 승인 하는 것이 우선, 결제 이후 승인이 됨
            );
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body("요청을 찾을 수 없습니다. 나중에 다시 시도해주세요.");
        }

        return ResponseEntity.ok("예약 요청을 승인했습니다.");
    }

    @PatchMapping("/reject/{reservationId}")
    public ResponseEntity<String> rejectReservation(@PathVariable("reservationId") Long reservationId,
                                                    @AuthenticationPrincipal UserDetailsDto userDetailsDto) {

        try {
            placeReservationService.updateReservationStatus(
                    reservationId,
                    userDetailsDto.getId(),
                    PlaceReservationStatus.REJECTED
            );
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body("요청을 찾을 수 없습니다. 나중에 다시 시도해주세요.");
        }

        return ResponseEntity.ok("예약 요청을 거절했습니다.");
    }
}
