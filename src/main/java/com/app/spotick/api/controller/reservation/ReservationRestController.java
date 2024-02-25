package com.app.spotick.api.controller.reservation;

import com.app.spotick.api.dto.response.CommonResponse;
import com.app.spotick.domain.dto.place.reservation.ReservationRequestListDto;
import com.app.spotick.domain.dto.user.UserDetailsDto;
import com.app.spotick.domain.entity.place.PlaceReservation;
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

import java.time.LocalDateTime;
import java.util.Objects;

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
            placeReservationService.updateReservationStatusAsHost(
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
            placeReservationService.updateReservationStatusAsHost(
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

    @GetMapping("/cancel/{reservationId}")
    public ResponseEntity<String> cancelReservation(@PathVariable("reservationId") Long reservationId,
                                                    @AuthenticationPrincipal UserDetailsDto userDetailsDto) {
        if (reservationId == null) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body("잘못된 요청입니다.");
        }

        // reservationId검증: userId 동일한지 검증 필요 -> 시간정보 찾아와서 checkIn시간 체크하여 지나지 않았는지 검증 -> accepted 검증(아닐 시 취소 불가능)
        PlaceReservation reservation = placeReservationService
                .findReservationByIdAndUser(reservationId, userDetailsDto.getId()).orElse(null);


        if (reservation.getCheckIn().isBefore(LocalDateTime.now())) {
            // checkIn시간이 현재 시간보다 이전일 경우, 이미 승인된 예약일 경우
            return ResponseEntity.status(HttpStatus.FORBIDDEN)
                    .body("예약 시간이 지나간 예약은<br>취소할 수 없습니다.");
        }

        if (reservation.getReservationStatus().equals(PlaceReservationStatus.APPROVED)) {
            // 승인된 예약은 취소 불가
            return ResponseEntity.status(HttpStatus.FORBIDDEN)
                    .body("이미 승인된 예약은<br>취소할 수 없습니다.");
        }

        try {
            placeReservationService.updateReservationStatusAsUser(reservationId, userDetailsDto.getId(), PlaceReservationStatus.CANCELLED);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.badRequest()
                    .body("예약 내역을 찾을 수 없습니다.");
        }

        return ResponseEntity.status(HttpStatus.OK)
                .body("예약이 취소되었습니다.");
    }

    @GetMapping("/delete/{reservationId}")
    public ResponseEntity<String> deleteReservation(@PathVariable("reservationId") Long reservationId,
                                                    @AuthenticationPrincipal UserDetailsDto userDetailsDto) {
        if (reservationId == null) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body("잘못된 요청입니다.");
        }

        // reservationId검증: userId 동일한지 검증 필요 -> 시간정보 찾아와서 checkOut시간 체크하여 지나갔는지 검증 -> accepted, rejected, cancled아닐 시 삭제 불가.
        PlaceReservation reservation = placeReservationService
                .findReservationByIdAndUser(reservationId, userDetailsDto.getId()).orElse(null);

        if (reservation == null) {
            // 예약 정보를 찾을 수 없을 시
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body("예약 정보를 찾을 수 없습니다.");
        }

        if (reservation.getReservationStatus() != PlaceReservationStatus.CANCELLED) {
            // 캔슬 된 경우가 아니라면 아래 과정을 거쳐야 함
            if (reservation.getCheckOut().isAfter(LocalDateTime.now())) {
                // checkIn시간이 현재 시간보다 이전일 경우, 이미 승인된 예약일 경우
                return ResponseEntity.status(HttpStatus.FORBIDDEN)
                        .body("예약시간이 지나지 않은 예약은<br>삭제할 수 없습니다.");
            }
        }

        if (reservation.getReservationStatus().equals(PlaceReservationStatus.PENDING)
            || reservation.getReservationStatus().equals(PlaceReservationStatus.WAITING_PAYMENT)) {
            // 예약이 해지되지 못하고 유효한 상태일 시 삭제불가
            return ResponseEntity.status(HttpStatus.FORBIDDEN)
                    .body("예약이 해지되어있지 않습니다.<br>예약을 취소하고 삭제를 시도하여주십시오.");
        }

        try {
            placeReservationService.updateReservationStatusAsUser(reservationId, userDetailsDto.getId(), PlaceReservationStatus.DELETED);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body("예약 내역을 찾을 수 없습니다.");
        }

        return ResponseEntity.status(HttpStatus.OK)
                .body("예약내역이 삭제되었습니다.");
    }
}
