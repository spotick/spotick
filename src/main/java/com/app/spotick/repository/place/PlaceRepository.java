package com.app.spotick.repository.place;

import com.app.spotick.domain.entity.place.Place;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PlaceRepository extends JpaRepository<Place, Long> {
//    장소 등록 : 기본제공 메소드로 처리
}
