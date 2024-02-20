package com.app.spotick.service.place.payment;

import com.app.spotick.api.dto.place.PlacePaymentDto;
import com.app.spotick.api.dto.response.CommonResponse;
import org.springframework.http.ResponseEntity;

public interface PlacePaymentService {

    ResponseEntity<CommonResponse<Long>> savePayment(Long userId, PlacePaymentDto dto);

}
