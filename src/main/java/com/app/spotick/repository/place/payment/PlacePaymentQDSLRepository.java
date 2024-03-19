package com.app.spotick.repository.place.payment;

import com.app.spotick.api.dto.place.PlacePaymentInfoDto;

import java.util.Optional;

public interface PlacePaymentQDSLRepository {
    Optional<PlacePaymentInfoDto> findPaymentDetailById(Long paymentId);

}
