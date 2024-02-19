package com.app.spotick.repository.place.file;

import com.app.spotick.domain.dto.place.file.PlaceFileDto;
import com.app.spotick.domain.entity.place.PlaceFile;
import io.lettuce.core.dynamic.annotation.Param;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PlaceFileRepository extends JpaRepository<PlaceFile, Long> {
//    장소 사진 저장: 기본제공 메소드 사용

    @Query("""
                select new com.app.spotick.domain.dto.place.file.PlaceFileDto(
                    f.id, f.fileName, f.uuid, f.uploadPath, f.place.id
                )
                from PlaceFile f
                where f.place.id = :placeId and f.id in :fileSaveList
            """)
    List<PlaceFileDto> findPlaceFilesByPlaceIdNotFileIds(@Param("placeId") Long placeId, @Param("fileSaveList") List<Long> fileSaveList);
}
