package com.app.spotick.repository.place.reservation;

import com.app.spotick.domain.dto.place.PlaceReservationListDto;
import com.app.spotick.domain.entity.place.PlaceReservation;
import io.lettuce.core.dynamic.annotation.Param;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PlaceReservationRepository extends JpaRepository<PlaceReservation, Long>, PlaceReservationQDSLRepository {

}
