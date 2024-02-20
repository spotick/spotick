package com.app.spotick.repository.place.payment;

import com.app.spotick.domain.entity.place.PlacePayment;
import org.jetbrains.annotations.NotNull;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface PlacePaymentRepository extends JpaRepository<PlacePayment, Long> {

    Optional<PlacePayment> findById(Long id);
}
