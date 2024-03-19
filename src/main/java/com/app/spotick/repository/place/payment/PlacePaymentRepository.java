package com.app.spotick.repository.place.payment;

import com.app.spotick.domain.entity.place.PlacePayment;
import com.app.spotick.domain.entity.place.PlaceReservation;
import io.lettuce.core.dynamic.annotation.Param;
import org.jetbrains.annotations.NotNull;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface PlacePaymentRepository extends JpaRepository<PlacePayment, Long>, PlacePaymentQDSLRepository {

    Optional<PlacePayment> findById(Long id);

    @Query("""
                SELECT p.amount FROM PlacePayment p
                WHERE p.id = :orderId
            """)
    Optional<Long> findAmountById(@Param("orderId") Long orderId);

    @Query("""
                select r from PlacePayment p join p.placeReservation r where p.id = :paymentId
            """)
    PlaceReservation findReservationByPaymentId(@Param("paymentId") Long paymentId);
}
