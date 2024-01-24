package com.app.spotick.api.controller.place;

import com.app.spotick.api.dto.place.PlaceInquiryDto;
import com.app.spotick.domain.dto.user.UserDetailsDto;
import com.app.spotick.service.place.inquiry.PlaceInquiryService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/places/inquiry")
@RequiredArgsConstructor
public class PlaceInquiryRestController {
    private final PlaceInquiryService inquiryService;
    @PostMapping("/v1/register")
    public ResponseEntity<PlaceInquiryDto.Response> inquiryRegister(@AuthenticationPrincipal UserDetailsDto userDetailsDto,
                                          @RequestBody PlaceInquiryDto.Request inquiryDto){
        PlaceInquiryDto.Response inquiryDtoResp = inquiryService.register(inquiryDto, userDetailsDto.getId());
        inquiryDtoResp.setUserNickname(userDetailsDto.getNickName());
        return ResponseEntity.ok()
                .body(inquiryDtoResp);
    }



}
