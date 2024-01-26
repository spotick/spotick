package com.app.spotick.repository.promotion.file;

import com.app.spotick.domain.entity.place.PlaceFile;
import com.app.spotick.domain.entity.promotion.PromotionFile;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PromotionFileRepository extends JpaRepository<PromotionFile,Long> {
//    장소 사진 저장: 기본제공 메소드 사용
}
