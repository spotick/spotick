package com.app.spotick.controller.place;

import com.app.spotick.api.response.ReviewResponse;
import com.app.spotick.domain.dto.place.review.PlaceReviewListDto;
import com.app.spotick.domain.dto.place.review.PlaceReviewRegisterDto;
import com.app.spotick.domain.dto.place.review.PlaceReviewUpdateDto;
import com.app.spotick.domain.dto.user.UserDetailsDto;
import com.app.spotick.domain.entity.place.PlaceReservation;
import com.app.spotick.domain.entity.place.PlaceReview;
import com.app.spotick.service.place.reservation.PlaceReservationService;
import com.app.spotick.service.place.review.PlaceReviewService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.List;

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
        PlaceReservation foundReservation = placeReservationService
                .findReservationByIdAndUser(placeReviewRegisterDto.getReservationId(), userDetailsDto.getId());

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

    @PatchMapping("/notReviewing/{reservationId}")
    public ResponseEntity<String> updateNotReviewing(@PathVariable("reservationId") Long reservationId,
                                                     @AuthenticationPrincipal UserDetailsDto userDetailsDto) {

        if (reservationId == null) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body("잘못된 요청입니다.");
        }

        PlaceReservation reservation = placeReservationService
                .findReservationByIdAndUser(reservationId, userDetailsDto.getId());

        if (reservation == null) {
            // 예약 정보를 찾을 수 없을 시
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body("예약 정보를 찾을 수 없습니다.<br>문제가 지속될 시 문의해주세요.");
        }

        placeReservationService.updateNotReviewing(reservationId);

        return ResponseEntity.status(HttpStatus.OK)
                .body("작성가능한 후기에서 삭제되었습니다.");
    }

    @PatchMapping("/update")
    public ResponseEntity<ReviewResponse> updateReview(@Validated @RequestBody PlaceReviewUpdateDto placeReviewUpdateDto,
                                                       BindingResult result,
                                                       @AuthenticationPrincipal UserDetailsDto userDetailsDto) {
        System.out.println("placeReviewUpdateDto = " + placeReviewUpdateDto);

        PlaceReview foundReview = placeReviewService.findReview(placeReviewUpdateDto.getReviewId(), userDetailsDto.getId()).orElse(null);

        if (foundReview == null) {
            ReviewResponse response = new ReviewResponse(false);
            response.addError("error", "예약 내역을 확인 할 수 없습니다.<br>상황이 지속될 시 문의해주시길 바랍니다.");

            return ResponseEntity.status(HttpStatus.FORBIDDEN)
                    .body(response);
        }

        if (placeReviewUpdateDto.getScore().equals(foundReview.getScore())
            && placeReviewUpdateDto.getContent().equals(foundReview.getContent())) {
            ReviewResponse response = new ReviewResponse(false);
            response.addError("error", "변경된 내용이 없습니다.");

            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(response);
        }

        LocalDateTime currentDateTime = LocalDateTime.now();
        LocalDateTime createdDateTime = foundReview.getCreatedDate();

        long daysDifference = ChronoUnit.DAYS.between(createdDateTime, currentDateTime);

        if (daysDifference > 7) {
            ReviewResponse response = new ReviewResponse(false);
            response.addError("error", "작성 후 일주일이 지난 리뷰는 수정할 수 없습니다.");

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

        placeReviewService.updateReview(placeReviewUpdateDto);

        return ResponseEntity.status(HttpStatus.OK)
                .body(new ReviewResponse(true));
    }

    @GetMapping("/place/{placeId}/list")
    public ResponseEntity<Slice<PlaceReviewListDto>> getPlacesReviewList(@PathVariable("placeId") Long placeId,
                                                                         @PageableDefault(page = 1,
                                                                                 size = 10, sort = "id",
                                                                                 direction = Sort.Direction.DESC
                                                                         ) Pageable pageable) {
        PageRequest pageRequest = PageRequest.of(pageable.getPageNumber() - 1, 5, pageable.getSort());
        Slice<PlaceReviewListDto> placeReviewSlice = placeReviewService.findPlaceReviewSlice(placeId, pageRequest);
        return ResponseEntity.ok(placeReviewSlice);
    }


}















