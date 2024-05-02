package com.app.spotick.api.controller.review;

import com.app.spotick.api.response.DataResponse;
import com.app.spotick.api.response.MessageResponse;
import com.app.spotick.domain.dto.place.review.PlaceReviewListDto;
import com.app.spotick.domain.dto.place.review.PlaceReviewRegisterDto;
import com.app.spotick.domain.dto.place.review.PlaceReviewUpdateDto;
import com.app.spotick.domain.dto.user.UserDetailsDto;
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
import org.springframework.web.bind.annotation.*;

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
    public ResponseEntity<DataResponse<?>> writeReview(@Valid @RequestBody PlaceReviewRegisterDto placeReviewRegisterDto,
                                                       BindingResult result,
                                                       @AuthenticationPrincipal UserDetailsDto userDetailsDto) {
        List<Map<String, String>> response = new ArrayList<>();

        if (result.hasErrors()) {
            List<FieldError> errors = result.getFieldErrors();

            for (FieldError error : errors) {
                Map<String, String> errorMap = new HashMap<>();
                errorMap.put("field", error.getField());
                errorMap.put("message", error.getDefaultMessage());
                response.add(errorMap);
            }

            return new ResponseEntity<>(DataResponse.builder()
                    .success(false)
                    .data(response)
                    .build(), HttpStatus.BAD_REQUEST
            );
        }

        try {
            // 실제로 존재하는 예약이었는지 검증한다.
            placeReservationService
                    .findReservationByIdAndUser(placeReviewRegisterDto.getReservationId(), userDetailsDto.getId());

            //
            placeReviewRegisterDto.setUserId(userDetailsDto.getId());
            placeReviewService.registerReview(placeReviewRegisterDto);

            return new ResponseEntity<>(DataResponse.builder()
                    .success(true)
                    .message("리뷰가 등록되었습니다.")
                    .build(), HttpStatus.OK
            );
        } catch (Exception e) {
            log.error("유저 리뷰 작성 [Err_Msg]: {}", e.getMessage());
            Map<String, String> errorMap = new HashMap<>();
            errorMap.put("server", e.getMessage());

            return new ResponseEntity<>(DataResponse.builder()
                    .success(false)
                    .data(response.add(errorMap))
                    .build(), HttpStatus.BAD_REQUEST
            );
        }
    }

    @PatchMapping("/notReviewing/{reservationId}")
    public ResponseEntity<DataResponse<?>> updateNotReviewing(@PathVariable("reservationId") Long reservationId,
                                                              @AuthenticationPrincipal UserDetailsDto userDetailsDto) {
        try {
            placeReservationService.updateNotReviewing(reservationId, userDetailsDto.getId());

            return new ResponseEntity<>(DataResponse.builder()
                    .success(true)
                    .message("작성가능한 후기에서 삭제되었습니다.")
                    .build(), HttpStatus.OK
            );
        } catch (Exception e) {
            log.error("rId: {}, uId: {}, 유저 리뷰 작성 거절 [Err_Msg]: {}", reservationId, userDetailsDto.getId(), e.getMessage());
            return new ResponseEntity<>(DataResponse.builder()
                    .success(false)
                    .message(e.getMessage())
                    .build(), HttpStatus.BAD_REQUEST
            );
        }
    }

    @PutMapping("/edit")
    public ResponseEntity<MessageResponse> updateReview(@Valid @RequestBody PlaceReviewUpdateDto placeReviewUpdateDto,
                                                        BindingResult result,
                                                        @AuthenticationPrincipal UserDetailsDto userDetailsDto) {
        System.out.println("placeReviewUpdateDto = " + placeReviewUpdateDto);

        // 받은 필드 검증 우선
        if (result.hasErrors()) {
            List<FieldError> errors = result.getFieldErrors();

            FieldError firstError = errors.get(0);
            String errorMessage = firstError.getDefaultMessage();

            return new ResponseEntity<>(MessageResponse.builder()
                    .success(false)
                    .message(errorMessage)
                    .build(), HttpStatus.BAD_REQUEST
            );
        }

        try {

            placeReviewUpdateDto.setUserId(userDetailsDto.getId());
            placeReviewService.updateReview(placeReviewUpdateDto);

            return new ResponseEntity<>(MessageResponse.builder()
                    .success(true)
                    .message("리뷰가 수정 되었습니다.")
                    .build(), HttpStatus.OK
            );
        } catch (Exception e) {
            log.error("reviewId: {}, uId: {}, 유저 리뷰 수정 [Err_Msg]: {}", placeReviewUpdateDto.getReviewId(), userDetailsDto.getId(), e.getMessage());
            return new ResponseEntity<>(MessageResponse.builder()
                    .success(false)
                    .message(e.getMessage())
                    .build(), HttpStatus.BAD_REQUEST
            );
        }
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















