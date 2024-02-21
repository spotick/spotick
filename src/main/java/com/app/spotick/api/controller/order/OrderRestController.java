package com.app.spotick.api.controller.order;

import com.app.spotick.api.dto.place.PlacePaymentDto;
import com.app.spotick.api.dto.response.CommonResponse;
import com.app.spotick.domain.dto.user.UserDetailsDto;
import com.app.spotick.service.place.payment.PlacePaymentService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/order/api")
@RequiredArgsConstructor
public class OrderRestController {
    private final PlacePaymentService placePaymentService;

    @PostMapping("/place/save")
    public ResponseEntity<CommonResponse<Long>> save(@Valid @RequestBody PlacePaymentDto dto,
                                                     @AuthenticationPrincipal UserDetailsDto userDetailsDto) {
        return placePaymentService.savePayment(userDetailsDto.getId(), dto);
    }
}
