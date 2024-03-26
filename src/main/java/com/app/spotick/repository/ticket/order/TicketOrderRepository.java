package com.app.spotick.repository.ticket.order;

import com.app.spotick.domain.entity.ticket.TicketOrder;
import io.lettuce.core.dynamic.annotation.Param;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface TicketOrderRepository extends JpaRepository<TicketOrder, Long>, TicketOrderQDSLRepository {
    @Query("""
                SELECT o.amount FROM TicketOrder o
                WHERE o.id = :orderId
            """)
    Optional<Long> findAmountById(@Param("orderId") Long orderId);

}
