package com.app.spotick.service.place.payment;

import com.app.spotick.api.dto.place.PlacePaymentDto;
import com.app.spotick.domain.type.payment.PaymentStatus;
import org.springframework.http.ResponseEntity;

public interface PlacePaymentService {

    ResponseEntity<?> savePayment(Long userId, PlacePaymentDto dto);

    void updateStatus(Long paymentId, PaymentStatus paymentStatus);

}
