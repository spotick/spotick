package com.app.spotick.repository.place;

import com.app.spotick.domain.entity.place.PlaceFile;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.web.bind.annotation.RequestParam;

@Repository
public interface PlaceFileRepository extends JpaRepository<PlaceFile,Long> {
//    장소 사진 저장: 기본제공 메소드 사용
}
