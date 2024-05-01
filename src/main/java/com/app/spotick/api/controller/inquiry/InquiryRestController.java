package com.app.spotick.api.controller.inquiry;

import com.app.spotick.api.dto.place.InquiryResponseDto;
import com.app.spotick.api.response.CommonResponse;
import com.app.spotick.api.response.PageResponse;
import com.app.spotick.domain.dto.place.PlaceInquiryListDto;
import com.app.spotick.domain.dto.place.inquiry.UnansweredInquiryDto;
import com.app.spotick.domain.dto.ticket.TicketInquiryListDto;
import com.app.spotick.domain.dto.user.UserDetailsDto;
import com.app.spotick.domain.pagination.Pagination;
import com.app.spotick.service.place.inquiry.PlaceInquiryService;
import com.app.spotick.service.ticket.inquiry.TicketInquiryService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.NoSuchElementException;

@RestController
@RequestMapping("/inquiries/api")
@RequiredArgsConstructor
@Slf4j
public class InquiryRestController {
    private final PlaceInquiryService placeInquiryService;
    private final TicketInquiryService ticketInquiryService;

    @GetMapping("/places")
    public ResponseEntity<PageResponse<PlaceInquiryListDto>> getPlaceInquiries(@RequestParam(value = "page", defaultValue = "1") int page,
                                                                               @AuthenticationPrincipal UserDetailsDto userDetailsDto) {

        try {
            Pageable pageable = PageRequest.of(page - 1, 10);

            Page<PlaceInquiryListDto> placeInquiryDtos = placeInquiryService.getInquiriesByUserId(userDetailsDto.getId(), pageable);
            Pagination<PlaceInquiryListDto> pagination = new Pagination<>(5, pageable, placeInquiryDtos);

            PageResponse<PlaceInquiryListDto> response = new PageResponse<>(placeInquiryDtos, pagination);

            return new ResponseEntity<>(response, HttpStatus.OK);
        } catch (Exception e) {
            log.error("유저 장소문의 내역 [Err_Msg]: {}", e.getMessage());
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/tickets")
    public ResponseEntity<PageResponse<TicketInquiryListDto>> getTicketInquiries(@RequestParam(value = "page", defaultValue = "1") int page,
                                                                                 @AuthenticationPrincipal UserDetailsDto userDetailsDto) {
        try {
            Pageable pageable = PageRequest.of(page - 1, 10);

            Page<TicketInquiryListDto> ticketInquiryDtos = ticketInquiryService.findInquiriesPage(userDetailsDto.getId(), pageable);
            Pagination<TicketInquiryListDto> pagination = new Pagination<>(5, pageable, ticketInquiryDtos);

            PageResponse<TicketInquiryListDto> response = new PageResponse<>(ticketInquiryDtos, pagination);

            return new ResponseEntity<>(response, HttpStatus.OK);
        } catch (Exception e) {
            log.error("유저 티켓문의 내역 [Err_Msg]: {}", e.getMessage());
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @DeleteMapping("/placeDelete/{placeInquiryId}")
    public ResponseEntity<CommonResponse<?>> deletePlaceInquiry(@PathVariable("placeInquiryId") Long placeInquiryId,
                                                                @AuthenticationPrincipal UserDetailsDto userDetailsDto) {
        try {
            placeInquiryService.deleteInquiryById(placeInquiryId, userDetailsDto.getId());

            return new ResponseEntity<>(CommonResponse.builder()
                    .success(true)
                    .data(placeInquiryId)
                    .message("문의 내역을 삭제했습니다.")
                    .build(), HttpStatus.OK);
        } catch (Exception e) {
            log.error("장소 문의 삭제 [Err_Msg]: {}", e.getMessage());
            return new ResponseEntity<>(CommonResponse.builder()
                    .success(false)
                    .message("문의 내역을 삭제에 실패했습니다.")
                    .build(), HttpStatus.BAD_REQUEST);
        }
    }

    @DeleteMapping("/ticketDelete/{ticketInquiryId}")
    public ResponseEntity<CommonResponse<?>> deleteTicketInquiry(@PathVariable("ticketInquiryId") Long ticketInquiryId,
                                                      @AuthenticationPrincipal UserDetailsDto userDetailsDto) {
        try {
            ticketInquiryService.deleteInquiry(ticketInquiryId, userDetailsDto.getId());

            return new ResponseEntity<>(CommonResponse.builder()
                    .success(true)
                    .data(ticketInquiryId)
                    .message("문의 내역을 삭제했습니다.")
                    .build(), HttpStatus.OK);
        } catch (Exception e) {
            log.error("티켓 문의 삭제 [Err_Msg]: {}", e.getMessage());
            return new ResponseEntity<>(CommonResponse.builder()
                    .success(false)
                    .message("문의 내역을 삭제에 실패했습니다.")
                    .build(), HttpStatus.BAD_REQUEST);
        }
    }

    @GetMapping("/getPlace/{placeId}")
    public ResponseEntity<CommonResponse<Slice<UnansweredInquiryDto>>> getUnansweredInquiriesOfPlace(@PathVariable("placeId") Long placeId,
                                                                                                     @AuthenticationPrincipal UserDetailsDto userDetailsDto,
                                                                                                     @RequestParam(name = "page", defaultValue = "0") int page) {

        Pageable pageable = PageRequest.of(page, 10);

        Slice<UnansweredInquiryDto> contentsSlice = placeInquiryService.findUnanswerdInquiriesSlice(placeId, userDetailsDto.getId(), pageable);

        if (contentsSlice.isEmpty()) {
            return ResponseEntity.noContent().build();
        }

        return ResponseEntity.ok(new CommonResponse<>(true, "조회 성공", contentsSlice));
    }

    @GetMapping("/getTicket/{ticketId}")
    public ResponseEntity<CommonResponse<Slice<UnansweredInquiryDto>>> getUnansweredInquiriesOfTicket(@PathVariable("ticketId") Long ticketId,
                                                                                                      @AuthenticationPrincipal UserDetailsDto userDetailsDto,
                                                                                                      @RequestParam(name = "page", defaultValue = "0") int page) {

        Pageable pageable = PageRequest.of(page, 10);

        Slice<UnansweredInquiryDto> contentsSlice = ticketInquiryService.findUnanswerdInquiriesSlice(ticketId, userDetailsDto.getId(), pageable);

        if (contentsSlice.isEmpty()) {
            return ResponseEntity.noContent().build();
        }

        return ResponseEntity.ok(new CommonResponse<>(true, "조회 성공", contentsSlice));
    }

    @PatchMapping("/responsePlaceInquiry")
    public ResponseEntity<String> updatePlaceResponse(@Valid @RequestBody InquiryResponseDto inquiryResponseDto,
                                                      BindingResult result) {
        if (result.hasErrors()) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body("답변을 제대로 입력해주세요.");
        }

        try {
            placeInquiryService.updateInquiryResponse(inquiryResponseDto);

            return ResponseEntity.status(HttpStatus.OK)
                    .body("답변이 작성되었습니다.");
        } catch (NoSuchElementException e) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN)
                    .body("오류가 발생했습니다. 다시 시도해주세요.");
        }
    }

    @PatchMapping("/responseTicketInquiry")
    public ResponseEntity<String> updateTicketResponse(@Valid @RequestBody InquiryResponseDto inquiryResponseDto,
                                                       BindingResult result) {
        if (result.hasErrors()) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body("답변을 제대로 입력해주세요.");
        }

        try {
            ticketInquiryService.updateInquiryResponse(inquiryResponseDto);
            return ResponseEntity.status(HttpStatus.OK)
                    .body("답변이 작성되었습니다.");
        } catch (NoSuchElementException e) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN)
                    .body("오류가 발생했습니다.<br>다시 시도해주세요.");
        }
    }
}
