package com.app.spotick.api.controller.inquiry;

import com.app.spotick.api.dto.place.InquiryResponseDto;
import com.app.spotick.api.dto.response.CommonResponse;
import com.app.spotick.api.dto.response.PageResponse;
import com.app.spotick.domain.dto.place.PlaceInquiryListDto;
import com.app.spotick.domain.dto.place.inquiry.UnansweredInquiryDto;
import com.app.spotick.domain.dto.ticket.TicketInquiryListDto;
import com.app.spotick.domain.dto.user.UserDetailsDto;
import com.app.spotick.domain.entity.place.PlaceInquiry;
import com.app.spotick.domain.pagination.Pagination;
import com.app.spotick.service.place.inquiry.PlaceInquiryService;
import com.app.spotick.service.ticket.inquiry.TicketInquiryService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
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
            e.printStackTrace();
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
            e.printStackTrace();
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @DeleteMapping("/placeDelete/{placeInquiryId}")
    public ResponseEntity<String> deletePlaceInquiry(@PathVariable("placeInquiryId") Long placeInquiryId,
                                                     @AuthenticationPrincipal UserDetailsDto userDetailsDto) {
        if (placeInquiryId == null) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body("잘못된 요청입니다.");
        }

        try {
            placeInquiryService.deleteInquiryById(placeInquiryId, userDetailsDto.getId());

            return ResponseEntity.ok("문의 내역을 삭제했습니다.");
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.badRequest().body("오류가 발생했습니다.<br>다시 시도해주세요.");
        }
    }


    @DeleteMapping("/ticketDelete/{ticketInquiryId}")
    public ResponseEntity<String> deleteTicketInquiry(@PathVariable("ticketInquiryId") Long ticketInquiryId,
                                                      @AuthenticationPrincipal UserDetailsDto userDetailsDto) {

        if (ticketInquiryId == null) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body("잘못된 요청입니다.");
        }

        try {
            ticketInquiryService.deleteInquiry(ticketInquiryId, userDetailsDto.getId());

            return ResponseEntity.ok("문의 내역을 삭제했습니다.");
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.badRequest().body("오류가 발생했습니다.<br>다시 시도해주세요.");
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
