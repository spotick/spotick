package com.app.spotick.controller.place;

import com.app.spotick.api.dto.response.ReviewResponse;
import com.app.spotick.domain.dto.place.reservation.PlaceReviewRegisterDto;
import com.app.spotick.domain.dto.user.UserDetailsDto;
import com.app.spotick.domain.entity.place.PlaceReservation;
import com.app.spotick.service.place.reservation.PlaceReservationService;
import com.app.spotick.service.place.review.PlaceReviewService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/reviews")
@RequiredArgsConstructor
public class ReviewRestController {
    private final PlaceReviewService placeReviewService;
    private final PlaceReservationService placeReservationService;

    @PostMapping("/write")
    public ResponseEntity<ReviewResponse> writeReview(@Validated @RequestBody PlaceReviewRegisterDto placeReviewRegisterDto,
                                              BindingResult result,
                                              @AuthenticationPrincipal UserDetailsDto userDetailsDto) {
        // 해야될 검증 : reservationId와 userId를 통해 실존하는지 알맞는지 검증, 벨리데이션을 이용한 에러검출 -> 이후 등록
        PlaceReservation foundReservation = placeReservationService.findReservationByIdAndUser(placeReviewRegisterDto.getReservationId(), userDetailsDto.getId()).orElse(null);

        if (foundReservation == null) {
            ReviewResponse response = new ReviewResponse(false);
            response.addError("error", "예약 내역을 확인 할 수 없습니다.<br>상황이 지속될 시 문의해주시길 바랍니다.");

            return ResponseEntity.status(HttpStatus.FORBIDDEN)
                    .body(response);
        }

        if (result.hasErrors()) {
            List<FieldError> errors = result.getFieldErrors();
            ReviewResponse response = new ReviewResponse(false);

            for (FieldError error : errors) {
                response.addError(error.getField(), error.getDefaultMessage());
            }

            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(response);
        }

        placeReviewRegisterDto.setUserId(userDetailsDto.getId());
        placeReviewService.registerReview(placeReviewRegisterDto);

        return ResponseEntity.status(HttpStatus.OK)
                .body(new ReviewResponse(true));
    }
}
