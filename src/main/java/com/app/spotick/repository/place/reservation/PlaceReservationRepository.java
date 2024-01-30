package com.app.spotick.repository.place.reservation;

import com.app.spotick.domain.entity.place.PlaceReservation;
import com.app.spotick.domain.entity.user.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.Optional;

@Repository
public interface PlaceReservationRepository extends JpaRepository<PlaceReservation, Long>, PlaceReservationQDSLRepository {

    Optional<PlaceReservation> findByIdAndUser(Long Id, User user);


    /**
     * 사용자가 선택한 장소 예약이 가능한지 확인하는 메소드
     * @return true -> 예약 불가, false -> 예약 가능
     */
    @Query("""
                SELECT EXISTS (
                    SELECT 1 FROM PlaceReservation p 
                    WHERE p.place.id = :placeId        
                    AND (p.checkIn < :checkOut AND p.checkOut > :checkIn)
                )
            """)
    boolean isOverlappingReservation(@Param("placeId") Long placeId, @Param("checkIn") LocalDateTime checkIn,
                                     @Param("checkOut") LocalDateTime checkOut);

}
