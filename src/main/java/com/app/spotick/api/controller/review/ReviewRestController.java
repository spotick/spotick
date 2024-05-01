package com.app.spotick.api.controller.review;

import com.app.spotick.api.response.CommonResponse;
import com.app.spotick.api.response.ReviewResponse;
import com.app.spotick.domain.dto.place.review.PlaceReviewListDto;
import com.app.spotick.domain.dto.place.review.PlaceReviewRegisterDto;
import com.app.spotick.domain.dto.place.review.PlaceReviewUpdateDto;
import com.app.spotick.domain.dto.user.UserDetailsDto;
import com.app.spotick.domain.entity.place.PlaceReservation;
import com.app.spotick.domain.entity.place.PlaceReview;
import com.app.spotick.service.place.reservation.PlaceReservationService;
import com.app.spotick.service.place.review.PlaceReviewService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
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
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/reviews/api")
@RequiredArgsConstructor
@Slf4j
public class ReviewRestController {
    private final PlaceReviewService placeReviewService;
    private final PlaceReservationService placeReservationService;

    @PostMapping("/write")
    public ResponseEntity<CommonResponse<?>> writeReview(@Valid @RequestBody PlaceReviewRegisterDto placeReviewRegisterDto,
                                                         BindingResult result,
                                                         @AuthenticationPrincipal UserDetailsDto userDetailsDto) {
        List<Map<String, String>> response = new ArrayList<>();

        try {
            placeReservationService
                    .findReservationByIdAndUser(placeReviewRegisterDto.getReservationId(), userDetailsDto.getId());

            if (result.hasErrors()) {
                List<FieldError> errors = result.getFieldErrors();

                for (FieldError error : errors) {
                    Map<String, String> errorMap = new HashMap<>();
                    errorMap.put("field", error.getField());
                    errorMap.put("message", error.getDefaultMessage());
                    response.add(errorMap);
                }

                return new ResponseEntity<>(CommonResponse.builder()
                        .success(false)
                        .data(response)
                        .build(), HttpStatus.BAD_REQUEST
                );
            }

            placeReviewRegisterDto.setUserId(userDetailsDto.getId());
            placeReviewService.registerReview(placeReviewRegisterDto);

            return new ResponseEntity<>(CommonResponse.builder()
                    .success(true)
                    .message("리뷰가 등록되었습니다.")
                    .build(), HttpStatus.OK
            );
        } catch (Exception e) {
            log.error("유저 리뷰 작성 [Err_Msg]: {}", e.getMessage());
            Map<String, String> errorMap = new HashMap<>();
            errorMap.put("server", e.getMessage());

            return new ResponseEntity<>(CommonResponse.builder()
                    .success(false)
                    .data(response.add(errorMap))
                    .build(), HttpStatus.BAD_REQUEST
            );
        }
    }

    @PatchMapping("/notReviewing/{reservationId}")
    public ResponseEntity<CommonResponse<?>> updateNotReviewing(@PathVariable("reservationId") Long reservationId,
                                                     @AuthenticationPrincipal UserDetailsDto userDetailsDto) {
        try {
            placeReservationService.updateNotReviewing(reservationId);

            return new ResponseEntity<>(CommonResponse.builder()
                    .success(true)
                    .message("작성가능한 후기에서 삭제되었습니다.")
                    .build(), HttpStatus.OK
            );
        } catch (Exception e) {
            log.error("유저 리뷰 작성 거절 [Err_Msg]: {}", e.getMessage());

            return new ResponseEntity<>(CommonResponse.builder()
                    .success(false)
                    .message(e.getMessage())
                    .build(), HttpStatus.BAD_REQUEST
            );
        }
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















