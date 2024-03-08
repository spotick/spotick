package com.app.spotick.repository.admin.place;

import com.app.spotick.domain.entity.place.Place;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AdminPlaceRepository extends JpaRepository<Place, Long>,AdminPlaceQDSLRepository{
}
