package com.app.spotick.repository.place.modifyRequest;

import com.app.spotick.domain.entity.place.Place;
import com.app.spotick.domain.entity.place.PlaceModifyRequest;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface PlaceModifyReqRepository extends JpaRepository<PlaceModifyRequest,Long> {

    @Query("""
        SELECT mr FROM PlaceModifyRequest mr
        JOIN FETCH mr.changedPlace
        JOIN FETCH mr.originalPlace
        WHERE mr.changedPlace =:changedPlace
        """)
    Optional<PlaceModifyRequest> findByChangedPlace(@Param("changedPlace") Place changedPlace);
}
