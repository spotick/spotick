package com.app.spotick.repository.admin.place;

import com.app.spotick.domain.entity.place.Place;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;

@Repository
public interface AdminPlaceRepository extends JpaRepository<Place, Long>,AdminPlaceQDSLRepository{

    //    장소 수정 승인시 원본의 생성날짜를 바뀔 장소에 적용하기 위해 직접 쿼리를 생성하여 적용시킨다.
    @Modifying
    @Query("UPDATE Place p SET p.createdDate = :originalCreatedDate WHERE p.id = :changedPlaceId")
    void updateCreatedDateWithOriginal(@Param("originalCreatedDate") LocalDateTime originalCreatedDate, @Param("changedPlaceId") Long changedPlaceId);
}
